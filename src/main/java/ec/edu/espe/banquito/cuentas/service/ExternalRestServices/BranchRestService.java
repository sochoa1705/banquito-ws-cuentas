package ec.edu.espe.banquito.cuentas.service.ExternalRestServices;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ec.edu.espe.banquito.cuentas.controller.DTO.ExternalRestModel.CodeSwiftRS;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BranchRestService {

    private final RestTemplate restTemplate;

    public CodeSwiftRS sendObtainCodesOfCountryAndBranchRequest(String uniqueKey) {
        String url = "http://localhost:8082/api/v1/branch/" + uniqueKey;

        ResponseEntity<CodeSwiftRS> response = restTemplate.getForEntity(url, CodeSwiftRS.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al obtener la información de branch desde el servicio externo");
        }

        return response.getBody();
    }

}
