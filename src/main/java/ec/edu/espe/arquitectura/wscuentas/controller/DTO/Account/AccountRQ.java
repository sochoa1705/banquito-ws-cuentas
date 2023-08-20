package ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account;

import ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account.AccountRQ;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRQ {

    private String productAccountId;
    private String branchId;
    private String accountHolderType;
    private String accountHolderCode;
    private String accountAlias;

}
