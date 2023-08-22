package ec.edu.espe.arquitectura.wscuentas.controller.DTO.ExternalRestModel;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeSwiftRS {

    private String countryCode;
    private String branchCode;
}
