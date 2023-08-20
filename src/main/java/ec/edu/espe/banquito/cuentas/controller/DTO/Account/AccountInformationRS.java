package ec.edu.espe.banquito.cuentas.controller.DTO.Account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountInformationRS {

    private String productAccount;
    private String codeInternalAccount;
    private String codeInternationalAccount;
    private String accountHolderType;
    private Object clientAccount;
    private String accountAlias;
    private Float totalBalance;
    private Float availableBalance;
    private Float blockedBalance;
    private String state;
    private Boolean allowTransactions;
    private Float maxAmountTransactions;
    private Float interestRate;
}
