package ec.edu.espe.arquitectura.wscuentas.service.Account;

import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountInformationRS;
import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountRQ;
import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountRS;
import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountTransactionRS;
import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountUpdateRQ;
import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountUpdateStateRQ;
import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountsUserRS;
import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountUpdateRS;
import ec.edu.espe.arquitectura.wscuentas.controller.DTO.ExternalRestModel.CodeSwiftRS;
import ec.edu.espe.arquitectura.wscuentas.model.Account.Account;
import ec.edu.espe.arquitectura.wscuentas.repository.AccountRepository;
import ec.edu.espe.arquitectura.wscuentas.service.ExternalRestServices.BranchRestService;
import ec.edu.espe.arquitectura.wscuentas.service.ExternalRestServices.ClientRestService;
import ec.edu.espe.arquitectura.wscuentas.service.ExternalRestServices.ProductRestService;
import lombok.RequiredArgsConstructor;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    // private static final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final ClientRestService clientRestService;
    private final ProductRestService productRestService;
    private final BranchRestService branchRestService;

    public List<AccountsUserRS> getAccountsOfUser(String accountHolderCode) {
        List<Account> existsAccounts = accountRepository.findByAccountHolderCode(accountHolderCode);

        if (existsAccounts == null) {
            throw new RuntimeException("No tiene cuentas el usuario");
        }

        List<AccountsUserRS> accounts = new ArrayList<>();

        for (Account account : existsAccounts) {
            accounts.add(transformToAccountsUserRS(account));
        }

        return accounts;
    }

    public AccountInformationRS getAccountInformation(String codeInternalAccount) {
        Account existsAccount = accountRepository.findByCodeInternalAccount(codeInternalAccount);

        if (existsAccount == null) {
            throw new RuntimeException("No existe la cuenta");
        }

        return this.transformToAccountInformationRS(existsAccount);
    }

    public AccountRS create(AccountRQ accountRQ) {
        // Transform AccountRQ to Account
        Account newAccount = transformOfAccountRQ(accountRQ);

        Account existsAccount = accountRepository.findByAccountHolderCodeAndProductAccountId(
                newAccount.getAccountHolderCode(),
                newAccount.getProductAccountId());

        if (existsAccount != null) {
            throw new RuntimeException("El usuario/compania ya tiene una cuenta de este tipo");
        } else {
            accountRepository.save(newAccount);
            return this.transformToAccountRS(newAccount);
        }
    }

    public AccountUpdateRS update(AccountUpdateRQ accountUpdateRQ) {
        Account account = accountRepository.findByCodeInternalAccount(accountUpdateRQ.getCodeInternalAccount());

        if (account == null) {
            throw new RuntimeException("La cuenta no existe");
        }

        switch (account.getState()) {
            case "ACT":
                account.setState(account.getState());
                break;
            case "SUS":
            case "BLO":
            case "INA":
                account.setBlockedBalance(account.getAvailableBalance());
                account.setState(account.getState());
                account.setClosedDate(new Date());
                break;
            default:
                throw new RuntimeException("Estado no válido: " + account.getState());
        }
        account.setName(accountUpdateRQ.getAccountAlias());
        account.setAllowTransactions(account.getAllowTransactions());
        account.setMaxAmountTransactions(account.getMaxAmountTransactions());
        account.setLastModifiedDate(new Date());
        accountRepository.save(account);

        return transformToAccountUpdateRS(account);
    }

    public void updateStateDependsClient(AccountUpdateStateRQ accountUpdateStateRQ) {
        List<Account> accountsOfClient = accountRepository.findByAccountHolderCode(accountUpdateStateRQ.getAccountHolderCode());

        if (accountsOfClient.isEmpty()) {
            return;
        }

        for (Account account : accountsOfClient) {
            switch (accountUpdateStateRQ.getState()) {
                case "ACT":
                    account.setState("ACT");
                    break;
                case "SUS":
                case "BLO":
                case "INA":
                    account.setBlockedBalance(account.getAvailableBalance());
                    account.setState(accountUpdateStateRQ.getState());
                    account.setClosedDate(new Date());
                    break;
                default:
                    throw new RuntimeException("Estado no válido: " + accountUpdateStateRQ.getState());
            }
            accountRepository.save(account);
        }
    }

    private Account transformOfAccountRQ(AccountRQ accountRQ) {
        Account account = Account.builder()
                .productAccountId(accountRQ.getProductAccountId())
                .branchId(accountRQ.getBranchId())
                .uniqueKey(UUID.randomUUID().toString())
                .codeInternalAccount(generateNextAccountCode())
                .codeInternationalAccount(generateSwiftCode(accountRQ.getBranchId()))
                .accountHolderType(accountRQ.getAccountHolderType())
                .accountHolderCode(accountRQ.getAccountHolderCode())
                .name(accountRQ.getAccountAlias())
                .totalBalance(0.0f)
                .availableBalance(0.0f)
                .blockedBalance(0.0f)
                .state("ACT")
                .creationDate(new Date())
                .activationDate(new Date())
                .allowTransactions(true)
                .maxAmountTransactions(300.0f)
                .interestRate(5.0f) // Default of Bco Pichincha
                .build();

        return account;
    }

    private AccountInformationRS transformToAccountInformationRS(Account account) {
        AccountInformationRS accountInformationRS = AccountInformationRS.builder()
                .productAccount(productRestService.sendObtainNameProductRequest(
                        account.getProductAccountId()))
                .codeInternalAccount(account.getCodeInternalAccount())
                .codeInternationalAccount(account.getCodeInternationalAccount())
                .accountHolderType(account.getAccountHolderType())
                .clientAccount(clientRestService.sendObtainInformationClientRequest(
                        account.getAccountHolderType(),
                        account.getAccountHolderCode()))
                .accountAlias(account.getName())
                .totalBalance(account.getTotalBalance())
                .availableBalance(account.getAvailableBalance())
                .blockedBalance(account.getBlockedBalance())
                .state(account.getState())
                .allowTransactions(account.getAllowTransactions())
                .maxAmountTransactions(account.getMaxAmountTransactions())
                .interestRate(account.getInterestRate())
                .build();

        return accountInformationRS;
    }

    private AccountUpdateRS transformToAccountUpdateRS(Account account) {
        AccountUpdateRS accountUpdateRS = AccountUpdateRS.builder()
                .codeInternalAccount(account.getCodeInternalAccount())
                .accountAlias(account.getName())
                .state(account.getState())
                .allowTransactions(account.getAllowTransactions())
                .maxAmountTransactions(account.getMaxAmountTransactions())
                .lastModifiedDate(account.getLastModifiedDate())
                .closedDate(account.getClosedDate())
                .build();

        return accountUpdateRS;
    }

    private AccountRS transformToAccountRS(Account account) {
        AccountRS accountRS = AccountRS.builder()
        .codeInternalAccount(account.getCodeInternalAccount())
        .accountHolderType(account.getAccountHolderType())
        .state(account.getState())
        .allowTransactions(account.getAllowTransactions())
        .maxAmountTransactions(account.getMaxAmountTransactions())
        .interestRate(account.getInterestRate())
        .build();

        return accountRS;
    }

    private AccountsUserRS transformToAccountsUserRS(Account account) {
        AccountsUserRS accountsUserRS = AccountsUserRS.builder()
        .codeInternalAccount(account.getCodeInternalAccount())
        .totalBalance(account.getTotalBalance())
        .accountAlias(account.getName())
        .build();

        return accountsUserRS;
    }

    private String generateNextAccountCode() {
        Account lastAccount = accountRepository.findTopByOrderByCodeInternalAccountDesc();
        if (lastAccount != null) {
            String lastCodeInternalAccount = lastAccount.getCodeInternalAccount();
            // Assuming accountCode is numeric and needs to be incremented
            int nextAccountCode = Integer.parseInt(lastCodeInternalAccount) + 1;
            return String.valueOf("00" + nextAccountCode);
        } else {
            // If no accounts exist, start with a default value
            return "00137979";
        }
    }

    private String generateSwiftCode(String branchUniqueKey) {
        CodeSwiftRS codes = branchRestService.sendObtainCodesOfCountryAndBranchRequest(branchUniqueKey);

        String swiftCode = "BQUI" + codes.getCountryCode() + "CH" + codes.getBranchCode();

        if (swiftCode.length() < 8 || swiftCode.length() > 11) {
            throw new IllegalArgumentException("Codigo swift invalido");
        }

        return swiftCode;
    }

}
