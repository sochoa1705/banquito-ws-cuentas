package ec.edu.espe.arquitectura.wscuentas.service.Account;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountTransactionRQ;
import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountTransactionRS;
import ec.edu.espe.arquitectura.wscuentas.model.Account.Account;
import ec.edu.espe.arquitectura.wscuentas.model.Account.AccountTransaction;
import ec.edu.espe.arquitectura.wscuentas.repository.AccountRepository;
import ec.edu.espe.arquitectura.wscuentas.repository.AccountTransactionRepository;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AccountTransactionService {

    private final AccountRepository accountRepository;
    private final AccountTransactionRepository accountTransactionRepository;

    @Transactional
    public void create(AccountTransactionRQ accountTransactionRQ) {

        Account creditorAccount = null;
        Account debtorAccount = null;
        Boolean fixedBalance = false;
        String creditorAccountInternationalCode = null;
        String debtorAccountInternationalCode = null;
        Float totalDebtorTransactions = 0.0f;

        if (accountTransactionRQ.getCreditorAccount() != null) {
            creditorAccount = accountRepository.findByCodeInternalAccount(accountTransactionRQ.getCreditorAccount());
            if (creditorAccount == null) {
                throw new RuntimeException("No existe la cuenta del creditor");
            }

            if (!creditorAccount.getState().equals("ACT")) {
                throw new RuntimeException("La cuenta del creditor no esta disponible");
            }
        }

        if (accountTransactionRQ.getDebtorAccount() != null) {
            debtorAccount = accountRepository.findByCodeInternalAccount(accountTransactionRQ.getDebtorAccount());
            if (debtorAccount == null) {
                throw new RuntimeException("No existe la cuenta del debtor");
            }

            if (!debtorAccount.getState().equals("ACT")) {
                throw new RuntimeException("La cuenta del debtor no esta disponible");
            }

            List<AccountTransaction> debtorTransactions = accountTransactionRepository
                    .findByAccountIdAndTransactionTypeAndDebtorAccount(
                            debtorAccount.getId(),
                            "DEB",
                            debtorAccount.getCodeInternalAccount());

            totalDebtorTransactions = debtorTransactions.stream()
                    .map(AccountTransaction::getAmount)
                    .reduce(0.0f, Float::sum);
        }

        switch (accountTransactionRQ.getTransactionType()) {
            case "CRED":
                // Save code international of creditor
                creditorAccountInternationalCode = creditorAccount.getCodeInternationalAccount();

                if (debtorAccount != null) {
                    if ((debtorAccount.getAvailableBalance() - accountTransactionRQ.getAmount()) < 0) {
                        throw new RuntimeException("No tiene suficiente dinero para la operacion");
                    }

                    if (totalDebtorTransactions + accountTransactionRQ.getAmount() > debtorAccount
                            .getMaxAmountTransactions()) {
                        throw new RuntimeException("El monto total de transacciones excede el límite permitido");
                    }

                    if (!debtorAccount.getAllowTransactions()) {
                        throw new RuntimeException("No tiene permitido realizar transacciones.");
                    }
                    // Save code international of debtor
                    debtorAccountInternationalCode = debtorAccount.getCodeInternationalAccount();
                    // Generate withdrawal to update balance
                    withdrawal(debtorAccount, accountTransactionRQ.getAmount(), fixedBalance);
                    // Generate withdrawal transaction
                    AccountTransaction withdrawalTransaction = transformOfAccountTransactionRQ(
                            accountTransactionRQ,
                            debtorAccount.getId(),
                            "DEB",
                            creditorAccountInternationalCode,
                            debtorAccountInternationalCode);
                    accountTransactionRepository.save(withdrawalTransaction);
                }

                deposit(creditorAccount, accountTransactionRQ.getAmount());
                // Generate deposit transaction
                AccountTransaction depositTransaction = transformOfAccountTransactionRQ(
                        accountTransactionRQ,
                        creditorAccount.getId(),
                        "CRED",
                        creditorAccountInternationalCode,
                        debtorAccountInternationalCode);
                accountTransactionRepository.save(depositTransaction);
                break;
            case "DEB":
                if ((debtorAccount.getAvailableBalance() - accountTransactionRQ.getAmount()) < 0) {
                    fixedBalance = true;
                }

                if (totalDebtorTransactions + accountTransactionRQ.getAmount() > debtorAccount
                        .getMaxAmountTransactions()) {
                    throw new RuntimeException("El monto total de transacciones excede el límite permitido");
                }

                if (!debtorAccount.getAllowTransactions()) {
                    throw new RuntimeException("No tiene permitido realizar transacciones.");
                }
                // Save code international of debtor
                debtorAccountInternationalCode = debtorAccount.getCodeInternationalAccount();
                withdrawal(debtorAccount, accountTransactionRQ.getAmount(), fixedBalance);
                // Generate withdrawal transaction
                AccountTransaction withdrawalTransaction = transformOfAccountTransactionRQ(
                        accountTransactionRQ,
                        debtorAccount.getId(),
                        "DEB",
                        creditorAccountInternationalCode,
                        debtorAccountInternationalCode);
                accountTransactionRepository.save(withdrawalTransaction);
                break;
            default:
                throw new RuntimeException(
                        "Tipo de transaccion no válida: " + accountTransactionRQ.getTransactionType());
        }
    }

    public List<AccountTransactionRS> getTransactionsOfAccount(String accountInternalCode) {
        Account account = accountRepository.findByCodeInternalAccount(accountInternalCode);

        if (account == null) {
            throw new RuntimeException("La cuenta no pudo ser encontrada");
        }

        List<AccountTransactionRS> transactionRSList = transformToAccountTransactionRS(
                account.getAccountTransactions());

        return transactionRSList;

    }

    private void deposit(Account account, Float amount) {
        Float updatedBalance = account.getAvailableBalance() + amount;

        account.setAvailableBalance(updatedBalance);
        account.setTotalBalance(updatedBalance);
        accountRepository.save(account);
    }

    private void withdrawal(Account account, Float amount, Boolean fixedBalance) {
        Float updatedBalance = fixedBalance ? 0.00f : account.getAvailableBalance() - amount;

        account.setAvailableBalance(updatedBalance);
        account.setTotalBalance(updatedBalance);
        accountRepository.save(account);
    }

    private AccountTransaction transformOfAccountTransactionRQ(AccountTransactionRQ accountTransactionRQ,
            Integer accountId,
            String typeTransaction,
            String creditorAccountInternationalCode,
            String debtorAccountInternationalCode) {

        AccountTransaction accountTransaction = AccountTransaction.builder()
                .accountId(accountId)
                .uniqueKey(UUID.randomUUID().toString())
                .transactionType(typeTransaction)
                .reference(accountTransactionRQ.getReference())
                .amount(accountTransactionRQ.getAmount())
                .creditorBankCode(creditorAccountInternationalCode)
                .creditorAccount(accountTransactionRQ.getCreditorAccount())
                .debtorBankCode(debtorAccountInternationalCode)
                .debtorAccount(accountTransactionRQ.getDebtorAccount())
                .creationDate(new Date())
                .bookingDate(new Date())
                .valueDate(new Date())
                .applyTax(false)
                .parentTransactionKey(accountTransactionRQ.getParentTransactionKey())
                .state("EXE")
                .notes(null)
                .build();

        return accountTransaction;
    }

    private List<AccountTransactionRS> transformToAccountTransactionRS(List<AccountTransaction> accountTransactions) {
        List<AccountTransactionRS> accountTransactionListRS = new ArrayList<>();

        for (AccountTransaction accountTransaction : accountTransactions) {
            AccountTransactionRS accountTransactionRS = AccountTransactionRS.builder()
                    .uniqueKey(accountTransaction.getUniqueKey())
                    .transactionType(accountTransaction.getTransactionType())
                    .creditorAccount(accountTransaction.getCreditorAccount())
                    .debtorAccount(accountTransaction.getDebtorAccount())
                    .amount(accountTransaction.getAmount())
                    .reference(accountTransaction.getReference())
                    .creationDate(accountTransaction.getCreationDate())
                    .bookingDate(accountTransaction.getBookingDate())
                    .valueDate(accountTransaction.getValueDate())
                    .applyTax(accountTransaction.getApplyTax())
                    .parentTransactionKey(accountTransaction.getParentTransactionKey())
                    .state(accountTransaction.getState())
                    .build();

            accountTransactionListRS.add(accountTransactionRS);
        }

        return accountTransactionListRS;
    }
}
