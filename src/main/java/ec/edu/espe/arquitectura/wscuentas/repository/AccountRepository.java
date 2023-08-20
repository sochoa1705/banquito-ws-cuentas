package ec.edu.espe.arquitectura.wscuentas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.arquitectura.wscuentas.model.Account.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByCodeInternalAccount(String codeInternalAccount);

    List<Account> findByAccountHolderCode(String accountHolderCode);
    
    Account findByAccountHolderCodeAndProductAccountId(String accountHolderCode, String productAccountId);

    Account findTopByOrderByCodeInternalAccountDesc();

}