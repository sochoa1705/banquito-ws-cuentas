package ec.edu.espe.arquitectura.wscuentas.controller.DTO.Account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountUpdateStateRQ {

    private String accountHolderCode;
    private String state;
    
}
