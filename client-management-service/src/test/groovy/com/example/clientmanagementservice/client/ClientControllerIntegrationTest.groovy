package com.example.clientmanagementservice.client

import com.example.clientmanagementservice.client.exception.ResourceNotFoundException
import groovy.json.JsonSlurper
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.http.HttpStatus.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class ClientControllerIntegrationTest extends Specification {

    def baseApiUrl = '/api/v1/clients'
    def mockClientService = Mock(ClientService.class)
    @Subject
    ClientController clientController = new ClientController(mockClientService)
    protected MockMvc mockMvc = standaloneSetup(clientController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build()

    def "should save a client"() {
        given:
        def clientToSave = '{"id":""}'

        when:
        def response = mockMvc.perform(post("$baseApiUrl/")
                .content(clientToSave)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        1 * mockClientService.save(_) >> ClientDto.builder().id("1").build()

        expect:
        response.status == CREATED.value()
        content.id == "1"
    }

    def "should not save a client when id is given in the request"() {
        given:
        def clientToSave = '{"id":"ID"}'

        when:
        def response = mockMvc.perform(post("$baseApiUrl/")
                .content(clientToSave)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        0 * mockClientService.save(_)

        expect:
        response.status == BAD_REQUEST.value()
    }

    def "should update a client"() {
        given:
        def clientToUpdate = '{"id":"1"}'

        when:
        def response = mockMvc.perform(put("$baseApiUrl/")
                .content(clientToUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        1 * mockClientService.update(_) >> ClientDto.builder().id("1").build()

        expect:
        response.status == OK.value()
        content.id == "1"
    }

    def "should not update a client when id isn't given in the request"() {
        given:
        def clientToUpdate = '{"id":""}'

        when:
        def response = mockMvc.perform(put("$baseApiUrl/")
                .content(clientToUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        0 * mockClientService.update(_)

        expect:
        response.status == BAD_REQUEST.value()
    }

    def "should not update a client when id does not exist"() {
        given:
        def clientToUpdate = '{"id":"ID"}'

        when:
        def response = mockMvc.perform(put("$baseApiUrl/")
                .content(clientToUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockClientService.update(_) >> { throw new ResourceNotFoundException() }

        expect:
        response.status == NOT_FOUND.value()
    }

    def "should return a client given its id"() {
        given:
        def clientId = "1"

        when:
        def response = mockMvc.perform(get("$baseApiUrl/$clientId")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        1 * mockClientService.get("1") >> ClientDto.builder().id("1").build()

        expect:
        response.status == OK.value()
        content.id == "1"
    }

    def "should return not found 404 if client id does not exist"() {
        given:
        def clientId = "1"

        when:
        def response = mockMvc.perform(get("$baseApiUrl/$clientId")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockClientService.get("1") >> { throw new ResourceNotFoundException() }

        expect:
        response.status == NOT_FOUND.value()
    }

    def "should return a clients page"() {
        when:
        def response = mockMvc.perform(get("$baseApiUrl")
                .queryParam("page", "1")
                .queryParam("size", "10")
                .queryParam("sort", "id,desc")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockClientService.getAll(_ as Pageable, "") >> { new PageImpl<>(List.of(new ClientDto())) }

        expect:
        response.status == OK.value()
    }

    def "should return a clients page filter by search query"() {
        when:
        def response = mockMvc.perform(get("$baseApiUrl")
                .queryParam("q", "test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockClientService.getAll(_ as Pageable, "test") >> { new PageImpl<>(List.of(new ClientEntity())) }

        expect:
        response.status == OK.value()
    }

    def "should delete a client given its id"() {
        given:
        def clientToDelete = '1'

        when:
        def response = mockMvc.perform(delete("$baseApiUrl/$clientToDelete")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockClientService.get("1")
        1 * mockClientService.delete(_)

        expect:
        response.status == OK.value()
    }

    def "should not delete a client when id does not exist"() {
        given:
        def clientToDelete = 'ID'

        when:
        def response = mockMvc.perform(delete("$baseApiUrl/$clientToDelete")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn().getResponse()

        then:
        1 * mockClientService.get("ID") >> { throw new ResourceNotFoundException() }
        0 * mockClientService.delete(_)

        expect:
        response.status == NOT_FOUND.value()
    }
}
