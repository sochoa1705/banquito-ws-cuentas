package ec.edu.espe.banquito.cuentas.service.Account;

import ec.edu.espe.banquito.cuentas.controller.DTO.Account.AccountInformationRS;
import ec.edu.espe.banquito.cuentas.controller.DTO.Account.AccountRQ;
import ec.edu.espe.banquito.cuentas.controller.DTO.Account.AccountUpdateRQ;
import ec.edu.espe.banquito.cuentas.controller.DTO.Account.AccountUpdateRS;
import ec.edu.espe.banquito.cuentas.model.Account;
import ec.edu.espe.banquito.cuentas.repository.AccountRepository;
import ec.edu.espe.banquito.cuentas.service.ExternalRest.ClientRestService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRestService clientRestService;

    public AccountInformationRS getAccountInformation(String codeInternalAccount) {
        Account existsAccount = accountRepository.findByCodeInternalAccount(codeInternalAccount);

        if (existsAccount == null) {
            throw new RuntimeException("No existe la cuenta");
        }

        return this.transformToAccountInformationRS(existsAccount);
    }

    public void create(AccountRQ accountRQ) {
        // Transform AccountRQ to Account
        Account newAccount = this.transformOfAccountRQ(accountRQ);

        Account existsAccount = accountRepository.findByAccountHolderCodeAndProductAccountId(
                newAccount.getAccountHolderCode(),
                newAccount.getProductAccountId());

        if (existsAccount != null) {
            throw new RuntimeException("El usuario/compania ya tiene una cuenta de este tipo");
        } else {
            accountRepository.save(newAccount);
        }
    }

    public AccountUpdateRS update(AccountUpdateRQ accountUpdateRQ) {
        Account account = accountRepository.findByCodeInternalAccount(accountUpdateRQ.getCodeInternalAccount());

        if (account == null) {
            throw new RuntimeException("La cuenta no existe");
        }

        switch (account.getState()) {
            case "ACT":
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

        return this.transformToAccountUpdateRS(account);
    }

    private Account transformOfAccountRQ(AccountRQ accountRQ) {
        Account account = Account.builder()
                .productAccountId(accountRQ.getProductAccountId())
                .branchId(accountRQ.getBranchId())
                .uniqueKey(UUID.randomUUID().toString())
                .codeInternalAccount(generateNextAccountCode())
                .codeInternationalAccount("FALTA")
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
                .productAccount(null) ///////
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
}
