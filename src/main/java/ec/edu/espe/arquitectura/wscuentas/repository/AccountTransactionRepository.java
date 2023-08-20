package ec.edu.espe.arquitectura.wscuentas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.arquitectura.wscuentas.model.Account.AccountTransaction;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Integer> {

    List<AccountTransaction> findByAccountIdAndTransactionTypeAndDebtorAccount(
            Integer accountId,
            String transactionType,
            String debtorAccount);
}