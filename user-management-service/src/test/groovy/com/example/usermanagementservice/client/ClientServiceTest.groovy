package com.example.usermanagementservice.client

import spock.lang.Specification
import spock.lang.Subject

class ClientServiceTest extends Specification {

    def mockClientManagementServiceClient = Mock(ClientManagementServiceClient.class)
    @Subject
    ClientService clientService = new ClientService(mockClientManagementServiceClient)

    def "should get a client given its id"() {
        given:
        def clientId = '1'

        when:
        def client = clientService.getClient(clientId)

        then:
        1 * mockClientManagementServiceClient.getClientById("1") >> {
            def mockClient = new Client()
            mockClient.setId("1")
            mockClient
        }

        expect:
        client.id == "1"
    }
}