package ec.edu.espe.banquito.cuentas.controller.DTO.Account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRS {

    private String branchId;
    private String accountHolderType;
    private String accountHolderCode;
    private String accountAlias;
    private Boolean allowTransactions;
    private Float maxAmountTransactions;
}
