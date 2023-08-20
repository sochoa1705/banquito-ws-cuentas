package ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountTransactionRS {

    private String uniqueKey;
    private String transactionType;
    private String creditorAccount;
    private String debtorAccount;
    private Float amount;
    private String reference;
    private Date creationDate;
    private Date bookingDate;
    private Date valueDate;
    private Boolean applyTax;
    private String parentTransactionKey;
    private String state;

}
