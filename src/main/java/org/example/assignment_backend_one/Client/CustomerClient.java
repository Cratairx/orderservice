package org.example.assignment_backend_one.Client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class CustomerClient {
    private final RestClient restClient;

    public CustomerClient(@Value("${customer-service.base-url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public boolean customerExists(Long customerId) {
        try {
            restClient.get().uri("/customers/{id}", customerId).retrieve().toBodilessEntity();
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}
