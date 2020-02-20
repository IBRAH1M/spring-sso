package com.example.clientmanagementservice

import com.example.clientmanagementservice.client.ClientController
import com.example.clientmanagementservice.client.ClientDto
import com.example.clientmanagementservice.client.ClientService
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ClientManagementServiceContractsBase extends Specification {

    private MockMvc mockMvc
    private ClientService clientService
    @Subject
    private ClientController clientController

    void setup() {
        clientService = Mock(ClientService.class)

        setupMockData()

        clientController = new ClientController(clientService)
        mockMvc = standaloneSetup(clientController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build()
        RestAssuredMockMvc.mockMvc(mockMvc)
    }

    private void setupMockData() {
        def client = ClientDto.builder().id("CLIENT_ID").name("NAME").nameAr("NAME_AR").build()
        clientService.save(_) >> { args ->
            def c = args.get(0) as ClientDto
            client
        }

        clientService.update(_) >> { args ->
            def c = args.get(0) as ClientDto
            client
        }

        clientService.getAll(_, _) >> { args ->
            Pageable page = args.get(0) as Pageable
            String q = args.get(1) as String

            new PageImpl<>(List.of(client))
        }

        clientService.get(_) >> { args ->
            def c = args.get(0) as String
            client
        }

        clientService.delete(_) >> { args ->
            def c = args.get(0) as String
        }
    }
}
