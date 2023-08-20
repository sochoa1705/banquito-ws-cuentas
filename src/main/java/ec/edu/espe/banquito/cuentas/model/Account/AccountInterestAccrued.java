package ec.edu.espe.banquito.cuentas.model.Account;

import java.util.Date;
import java.util.List;

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
@Table(name = "ACCOUNT_INTEREST_ACCRUED")
public class AccountInterestAccrued {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ACCOUNT_INTEREST_ACCRUED_ID", nullable = false)
    private Integer id;

    @Column(name = "ACCOUNT_ID", nullable = false)
    private Integer accountId;

    @Column(name = "UNIQUE_KEY", nullable = false, length = 36)
    private String uniqueKey;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXECUTION_DATE", nullable = false)
    private Date executionDate;

    @Column(name = "AMOUNT", nullable = false, precision = 2)
    private Float amount;

    @Column(name = "INTEREST_RATE", nullable = false, precision = 5)
    private Float interestRate;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID", insertable = false, updatable = false)
    private Account account;

}
