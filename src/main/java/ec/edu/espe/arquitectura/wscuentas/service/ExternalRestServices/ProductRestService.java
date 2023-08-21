package ec.edu.espe.arquitectura.wscuentas.service.ExternalRestServices;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductRestService {

    private final RestTemplate restTemplate;

    public String sendObtainNameProductRequest(String uniqueKey) {
        String url = "https://banquito-ws-productos-pasivos-ntsumodxxq-uc.a.run.app/api/v1/product-account/name/" + uniqueKey;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al obtener la información del producto desde el servicio externo");
        }

        return response.getBody();
    }

}
