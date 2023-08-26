package ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRS {

    private String codeInternalAccount;
    private String accountHolderType;
    private String state;
    private Boolean allowTransactions;
    private Float maxAmountTransactions;
    private Float interestRate;
}
