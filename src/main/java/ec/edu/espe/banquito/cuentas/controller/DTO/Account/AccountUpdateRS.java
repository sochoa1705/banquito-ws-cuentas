package ec.edu.espe.banquito.cuentas.controller.DTO.Account;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountUpdateRS {

    private String codeInternalAccount;
    private String accountAlias;
    private String state;
    private Boolean allowTransactions;
    private Float maxAmountTransactions;
    private Date lastModifiedDate;
    private Date closedDate;
}
