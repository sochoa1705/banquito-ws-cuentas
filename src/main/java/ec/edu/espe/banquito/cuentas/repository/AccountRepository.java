package ec.edu.espe.banquito.cuentas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.cuentas.model.Account.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByCodeInternalAccount(String codeInternalAccount);
    
    Account findByAccountHolderCodeAndProductAccountId(String accountHolderCode, String productAccountId);

    Account findTopByOrderByCodeInternalAccountDesc();

}