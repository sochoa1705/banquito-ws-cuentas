package ec.edu.espe.banquito.cuentas.controller.DTO.Account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountUpdateRQ {

    private String codeInternalAccount;
    private String accountAlias;
    private String state;
    private Boolean allowTransactions;
    private Float maxAmountTransactions;
    
}
