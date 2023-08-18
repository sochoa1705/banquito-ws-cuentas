package ec.edu.espe.banquito.cuentas.service.Account;

import ec.edu.espe.banquito.cuentas.controller.DTO.Account.AccountRQ;
import ec.edu.espe.banquito.cuentas.model.Account;
import ec.edu.espe.banquito.cuentas.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Boolean verifyIfExistsAccount(String accountHolderCode) {
        Account account = accountRepository.findByAccountHolderCode(accountHolderCode);
        Boolean exists;

        exists = account != null ? true : false;
        
        return exists;
    }

    public void create(AccountRQ accountRQ) {
        // Transform AccountRQ to Account
        Account newAccount = this.transformOfAccountRQ(accountRQ);

        Account existsAccount = accountRepository.findByAccountHolderCode(newAccount.getAccountHolderCode());

        if (existsAccount != null) {
            throw new RuntimeException("El usuario/compania ya tiene cuenta");
        } else {
            accountRepository.save(newAccount);
        }
    }

    private Account transformOfAccountRQ(AccountRQ accountRQ) {
        Account account = Account.builder()
                .branchId(accountRQ.getBranchId())
                .uniqueKey(UUID.randomUUID().toString())
                .codeInternalAccount(generateAccountInternalCode(
                        accountRQ.getAccountHolderType(),
                        accountRQ.getAccountHolderCode(),
                        accountRQ.getBranchId()))
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
                .allowOverdraft(accountRQ.getAllowOverdraft())
                .maxOverdraft(0.0f)
                .interestRate(5.0f) // Default of Bco Pichincha
                .build();

        return account;
    }

    private String generateAccountInternalCode(String holderType, String uuid, String branchId) {
        String holderTypePrefix = holderType.substring(0, Math.min(holderType.length(), 3));
        String uuidPrefix = uuid.substring(0, Math.min(uuid.length(), 3));
        String branchIdPrefix = branchId.substring(0, Math.min(branchId.length(), 2));

        return holderTypePrefix + uuidPrefix + branchIdPrefix;
    }
}
