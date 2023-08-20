package ec.edu.espe.arquitectura.wscuentas.service.ExternalRestServices;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientRestService {

    private final RestTemplate restTemplate;

    public Object sendObtainInformationClientRequest(String typeClient, String uniqueKey) {
        String url;

        if (typeClient.equals("CUS")) {
            url = "http://localhost:8081/api/v1/customers/informationforaccount/" + uniqueKey;
        } else {
            url = "http://localhost:8081/api/v1/group-company/informationforaccount/" + uniqueKey;
        }

        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al obtener la información del cliente desde el servicio externo");
        }

        return response.getBody();
    }

}
