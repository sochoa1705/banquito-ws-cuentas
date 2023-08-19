package ec.edu.espe.banquito.cuentas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ACCOUNT")
public class Account {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ACCOUNT_ID", nullable = false)
    private Integer id;

    @Column(name = "PRODUCT_ACCOUNT_ID", length = 36)
    private String productAccountId;

    @Column(name = "BRANCH_ID", nullable = false, length = 36)
    private String branchId;

    @Column(name = "UNIQUE_KEY", nullable = false, length = 36)
    private String uniqueKey;

    @Column(name = "CODE_INTERNAL_ACCOUNT", nullable = false, length = 8)
    private String codeInternalAccount;

    @Column(name = "CODE_INTERNATIONAL_ACCOUNT", nullable = false, length = 16)
    private String codeInternationalAccount;

    @Column(name = "ACCOUNT_HOLDER_TYPE", nullable = false, length = 3)
    private String accountHolderType;

    @Column(name = "ACCOUNT_HOLDER_CODE", nullable = false, length = 36)
    private String accountHolderCode;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "TOTAL_BALANCE", nullable = false, precision = 2)
    private Float totalBalance;

    @Column(name = "AVAILABLE_BALANCE", nullable = false, precision = 2)
    private Float availableBalance;

    @Column(name = "BLOCKED_BALANCE", nullable = false, precision = 2)
    private Float blockedBalance;

    @Column(name = "STATE", nullable = false, length = 3)
    private String state;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTIVATION_DATE")
    private Date activationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFIED_DATE")
    private Date lastModifiedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_INTEREST_CALCULATION_DATE")
    private Date lastInterestCalculationDate;

    @Column(name = "ALLOW_TRANSACTIONS", nullable = false)
    private Boolean allowTransactions;

    @Column(name = "MAX_AMOUNT_TRANSACTIONS", nullable = false, precision = 2)
    private Float maxAmountTransactions;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CLOSED_DATE")
    private Date closedDate;

    @Column(name = "INTEREST_RATE", nullable = false, precision = 2)
    private Float interestRate;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

}
