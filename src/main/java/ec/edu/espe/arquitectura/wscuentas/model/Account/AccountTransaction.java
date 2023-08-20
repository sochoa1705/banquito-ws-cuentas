package ec.edu.espe.arquitectura.wscuentas.model.Account;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ACCOUNT_TRANSACTION")
public class AccountTransaction {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ACCOUNT_TRANSACTION_ID", nullable = false)
    private Integer id;

    @Column(name = "ACCOUNT_ID", nullable = false)
    private Integer accountId;

    @Column(name = "UNIQUE_KEY", nullable = false, length = 36)
    private String uniqueKey;

    @Column(name = "TRANSACTION_TYPE", nullable = false, length = 12)
    private String transactionType;

    @Column(name = "REFERENCE", length = 50)
    private String reference;

    @Column(name = "AMOUNT", nullable = false, precision = 2)
    private Float amount;

    @Column(name = "CREDITOR_BANK_CODE", length = 20)
    private String creditorBankCode;

    @Column(name = "CREDITOR_ACCOUNT", length = 16)
    private String creditorAccount;

    @Column(name = "DEBTOR_BANK_CODE", length = 20)
    private String debtorBankCode;

    @Column(name = "DEBTOR_ACCOUNT", length = 16)
    private String debtorAccount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BOOKING_DATE", nullable = false)
    private Date bookingDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VALUE_DATE", nullable = false)
    private Date valueDate;

    @Column(name = "APPLY_TAX", nullable = false)
    private Boolean applyTax;

    @Column(name = "PARENT_TRANSACTION_KEY", length = 36)
    private String parentTransactionKey;

    @Column(name = "STATE", nullable = false, length = 3)
    private String state;

    @Column(name = "NOTES", length = 200)
    private String notes;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID", insertable = false, updatable = false)
    private Account account;

}
