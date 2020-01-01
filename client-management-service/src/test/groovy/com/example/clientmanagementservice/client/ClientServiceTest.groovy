package com.example.clientmanagementservice.client

import spock.lang.Specification
import spock.lang.Subject

class ClientServiceTest extends Specification {

    def mockClientRepository = Mock(ClientRepository.class)
    @Subject
    ClientService clientService = new ClientService(mockClientRepository)

    def "should get a client from the database with the given id"() {
        given:
        def clientId = '1'

        when:
        def client = clientService.get(clientId)

        then:
        1 * mockClientRepository.findById("1") >> {
            def mockClient = new Client()
            mockClient.setId("1")
            Optional.of(mockClient)
        }

        expect:
        client.id == "1"
    }
}