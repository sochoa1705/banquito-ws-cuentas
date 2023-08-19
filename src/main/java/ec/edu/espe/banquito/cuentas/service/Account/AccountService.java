package ec.edu.espe.banquito.cuentas.service.Account;

import ec.edu.espe.banquito.cuentas.controller.DTO.Account.AccountRQ;
import ec.edu.espe.banquito.cuentas.controller.DTO.Account.AccountRS;
import ec.edu.espe.banquito.cuentas.model.Account;
import ec.edu.espe.banquito.cuentas.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    // public AccountRS getAccount(String codeInternalAccount) {
    //     Account existsAccount = accountRepository.findByCodeInternalAccount(codeInternalAccount);

    //     if (existsAccount == null) {
    //         throw new RuntimeException("No existe la cuenta");
    //     }

    //     return this.transformToAccountRS(existsAccount);
    // }

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

    private String generateNextAccountCode() {
        Account lastAccount = accountRepository.findTopByOrderByCodeInternalAccountDesc();
        if (lastAccount != null) {
            String lastCodeInternalAccount = lastAccount.getCodeInternalAccount();
            // Assuming accountCode is numeric and needs to be incremented
            int nextAccountCode = Integer.parseInt(lastCodeInternalAccount) + 1;
            return String.valueOf("00"+nextAccountCode);
        } else {
            // If no accounts exist, start with a default value
            return "00137979";
        }
    }
}
