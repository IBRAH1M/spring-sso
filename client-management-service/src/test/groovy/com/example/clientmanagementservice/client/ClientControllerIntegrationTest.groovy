package com.example.clientmanagementservice.client

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

@Slf4j
class ClientControllerIntegrationTest extends Specification {

    def baseApiUrl = '/api/v1/clients'
    def mockClientService = Mock(ClientService.class)
    @Subject
    ClientController clientController = new ClientController(mockClientService)
    protected MockMvc mockMvc = standaloneSetup(clientController).build()

    def setup() {
    }

    def "should return a client given its id"() {
        given:
        def clientId = "1"

        when:
        def response = mockMvc.perform(get("$baseApiUrl/$clientId")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse()
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        1 * mockClientService.get("1") >> new Client("1")
        response.status == 200
        content.id == "1"
    }
}
