package ec.edu.espe.banquito.cuentas.controller.DTO.ExternalRestModel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeSwiftRS {

    private String branchCode;
    private String countryCode;

}
