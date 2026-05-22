package org.example.assignment_backend_one;

import org.example.assignment_backend_one.Controllers.CustomerController;
import org.example.assignment_backend_one.Models.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class AssignmentBackendOneApplicationTests {
    @Autowired
    private CustomerController customer;

    @Test
    void contextTest() throws Exception{
        assertThat(customer).isNotNull();
    }

    @LocalServerPort
    private int port;

    // detta har ändrat sit sen filmerna. typ så här denna går inte igenom helt men typ så här man skriver. det är oki att fråga ai exakt hur testerna ska se ut enligt sigrun
    @Autowired
    private RestTestClient restTestClient;
// man ska helst inte testa delete functioner
    // vi ska testa våra Service metoder och inte våra controllers, det kommer bli lättare då.
    @Test
    void getAllCustomers() throws Exception{
        String s = restTestClient.get().
                uri("http://localhost:%d".formatted(port) +"/allcustomers")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();
        System.out.println(s);
               // .jsonPath("$[0].id").isEqualTo(1); denna kan vändas om man använder @RestController

    }


}
