package ec.edu.espe.banquito.cuentas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.banquito.cuentas.model.Account.AccountTransaction;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Integer> {


}