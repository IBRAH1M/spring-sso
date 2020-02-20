package com.example.clientmanagementservice.client

import com.example.clientmanagementservice.client.exception.ResourceNotFoundException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.http.HttpStatus.*

class ClientControllerTest extends Specification {

    private ClientService mockClientService
    @Subject
    private ClientController clientController

    void setup() {
        mockClientService = Mock(ClientService.class)
        clientController = new ClientController(mockClientService)
    }

    def "should save a client"() {
        given:
        def client = ClientDto.builder().build()

        when:
        def response = clientController.createClient(client)

        then:
        1 * mockClientService.save(_) >> ClientDto.builder().id("1").build()

        expect:
        response.getStatusCodeValue() == CREATED.value()
        response.getBody().id == "1"
    }

    def "should not save a client when id is given in the request"() {
        given:
        def client = ClientDto.builder().id("ID").build()

        when:
        clientController.createClient(client)

        then:
        0 * mockClientService.save(_)

        and:
        def exception = thrown(ResponseStatusException)
        exception.getStatus().value() == BAD_REQUEST.value()
    }

    def "should update a client"() {
        given:
        def clientToUpdate = ClientDto.builder().id("1").name("NAME").nameAr("NAME_AR").build()

        when:
        def response = clientController.updateClient(clientToUpdate)

        then:
        1 * mockClientService.update(_) >> ClientDto.builder().id("1").name("NAME").nameAr("NAME_AR").build()

        expect:
        response.getStatusCodeValue() == OK.value()
        response.getBody().id == "1"
        response.getBody().name == "NAME"
        response.getBody().nameAr == "NAME_AR"
    }

    def "should not update a client when id isn't given in the request"() {
        given:
        def clientToUpdate = ClientDto.builder().id("").build()

        when:
        clientController.updateClient(clientToUpdate)

        then:
        0 * mockClientService.update(_)

        and:
        def exception = thrown(ResponseStatusException)
        exception.getStatus().value() == BAD_REQUEST.value()
    }

    def "should not update a client when id does not exist"() {
        given:
        def clientToUpdate = ClientDto.builder().id("ID").build()

        when:
        clientController.updateClient(clientToUpdate)

        then:
        1 * mockClientService.update(_) >> { throw new ResourceNotFoundException() }

        and:
        def exception = thrown(ResponseStatusException)
        exception.getStatus().value() == NOT_FOUND.value()
    }

    def "should return a client given its id"() {
        when:
        def response = clientController.getClient("1")

        then:
        1 * mockClientService.get("1") >> ClientDto.builder().id("1").build()

        expect:
        response.getStatusCodeValue() == OK.value()
        response.getBody().id == "1"
    }

    def "should return not found 404 if client id does not exist"() {
        when:
        clientController.getClient("1")

        then:
        1 * mockClientService.get("1") >> { throw new ResourceNotFoundException() }

        and:
        def exception = thrown(ResponseStatusException)
        exception.getStatus().value() == NOT_FOUND.value()
    }

    def "should return a clients page"() {
        given:
        def pageable = PageRequest.of(0, 2, Sort.by("id"))
        def query = ""

        when:
        def response = clientController.getAllClients(pageable, query)

        then:
        1 * mockClientService.getAll(_ as Pageable, "") >> { args ->
            Pageable page = args.get(0) as Pageable
            String q = args.get(1)

            assert page.pageNumber == 0
            assert page.pageSize == 2
            assert page.getSort().sorted
            assert q == ""

            def clients = new PageImpl<>(List.of(ClientDto.builder().build()))
            clients
        }

        expect:
        response.getStatusCodeValue() == OK.value()
    }

    def "should delete a client given its id"() {
        when:
        def response = clientController.deleteClient("1")

        then:
        1 * mockClientService.get("1")
        1 * mockClientService.delete(_)

        expect:
        response.getStatusCodeValue() == OK.value()
    }

    def "should not delete a client when id does not exist"() {
        when:
        clientController.deleteClient("ID")

        then:
        1 * mockClientService.get("ID") >> { throw new ResourceNotFoundException() }
        0 * mockClientService.delete(_)

        and:
        def exception = thrown(ResponseStatusException)
        exception.getStatus().value() == NOT_FOUND.value()
    }
}