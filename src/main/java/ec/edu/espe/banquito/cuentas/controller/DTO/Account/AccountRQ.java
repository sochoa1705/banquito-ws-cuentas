package ec.edu.espe.banquito.cuentas.controller.DTO.Account;

import ec.edu.espe.banquito.cuentas.controller.DTO.Account.AccountRQ;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRQ {

    private String branchId;
    private String accountHolderType;
    private String accountHolderCode;
    private String accountAlias;
    private Boolean allowOverdraft;
}
