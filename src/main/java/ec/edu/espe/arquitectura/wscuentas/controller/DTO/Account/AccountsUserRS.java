package ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountsUserRS {

    private String codeInternalAccount;
    private String accountAlias;
    private Float totalBalance;
}
