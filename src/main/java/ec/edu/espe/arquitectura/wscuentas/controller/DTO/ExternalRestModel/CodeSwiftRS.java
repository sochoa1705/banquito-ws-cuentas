package ec.edu.espe.arquitectura.wscuentas.controller.DTO.ExternalRestModel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeSwiftRS {

    private String countryCode;
    private String branchCode;
}
