package ec.edu.espe.banquito.cuentas.repository;

import ec.edu.espe.banquito.cuentas.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByCodeInternalAccount(String codeInternalAccount);
    
    Account findByAccountHolderCodeAndProductAccountId(String accountHolderCode, String productAccountId);

    Account findTopByOrderByCodeInternalAccountDesc();

}