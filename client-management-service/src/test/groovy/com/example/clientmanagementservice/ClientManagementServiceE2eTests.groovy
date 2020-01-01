package com.example.clientmanagementservice

import com.example.clientmanagementservice.client.ClientController
import groovy.util.logging.Slf4j
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@Slf4j
@SpringBootTest
class ClientManagementServiceE2eTests extends Specification {

    @Autowired(required = false)
    private ClientController clientController

    @Test
    def "when context is loaded then all expected beans are created"() {
        expect: "the ClientController is created"
        clientController
    }
}
