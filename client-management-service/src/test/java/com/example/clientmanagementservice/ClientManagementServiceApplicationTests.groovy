package com.example.clientmanagementservice

import com.example.clientmanagementservice.client.ClientController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ClientManagementServiceApplicationTests extends Specification {

    @Autowired(required = false)
    private ClientController clientController

    def "when context is loaded then all expected beans are created"() {
        expect: "the ClientController is created"
        false
    }
}
