package ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountTransactionRQ {

    private String creditorAccount;
    private String debtorAccount;
    private String transactionType;
    private String parentTransactionKey;
    private Float amount;
    private String reference;

}
