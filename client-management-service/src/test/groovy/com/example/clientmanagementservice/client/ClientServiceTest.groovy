package com.example.clientmanagementservice.client

import com.example.clientmanagementservice.client.exception.ResourceNotFoundException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject

class ClientServiceTest extends Specification {

    def mockClientRepository = Mock(ClientRepository.class)
    def mockClientMapper = Mock(ClientMapper.class)
    @Subject
    ClientService clientService = new ClientService(mockClientRepository, mockClientMapper)

    def "should save a client to the database"() {
        given:
        ClientDto clientDto = ClientDto.builder().build()
        ClientEntity clientEntity = new ClientEntity()

        when:
        def savedClient = clientService.save(clientDto)

        then:
        1 * mockClientMapper.toEntity(clientDto) >> clientEntity
        1 * mockClientRepository.save(clientEntity) >> {
            def mockClient = new ClientEntity()
            mockClient.setId("1")
            mockClient
        }
        1 * mockClientMapper.toDto(_ as ClientEntity) >> { args -> ClientDto.builder().id(args.id as String).build() }

        expect:
        savedClient.id != null
    }

    def "should update an existing client"() {
        given:
        ClientDto clientDto = ClientDto.builder().id("1").name("name").nameAr("nameAr").build()
        ClientEntity clientEntity = new ClientEntity()
        clientEntity.setId(clientDto.id)
        clientEntity.setName("NAME")
        clientEntity.setNameAr("NAME_AR")

        when:
        def savedClient = clientService.update(clientDto)

        then:
        1 * mockClientRepository.findById(clientDto.id) >> Optional.of(clientEntity)
        1 * mockClientRepository.save(clientEntity) >> { args ->
            clientEntity.name == 'name'
            clientEntity.nameAr == 'nameAr'
            clientEntity
        }
        1 * mockClientMapper.toDto(_ as ClientEntity) >> { args ->
            def u = args[0] as ClientEntity
            ClientDto.builder().id(u.id).name(u.name).nameAr(u.nameAr).build()
        }

        expect:
        savedClient.name == 'name'
        savedClient.nameAr == 'nameAr'
    }

    def "should get a client from the database given its id"() {
        given:
        def clientId = '1'

        when:
        def client = clientService.get(clientId)

        then:
        1 * mockClientRepository.findById("1") >> {
            def mockClient = new ClientEntity()
            mockClient.setId("1")
            mockClient.setName("NAME")
            mockClient.setNameAr("NAME_AR")
            Optional.of(mockClient)
        }
        1 * mockClientMapper.toDto(_ as ClientEntity) >> { args ->
            def c = args[0] as ClientEntity
            ClientDto.builder().id(c.id).name(c.name).nameAr(c.nameAr).build()
        }

        expect:
        client.id == "1"
    }

    def "should throw ResourceNotFoundException if client id does not exist"() {
        given:
        def clientId = '1'

        when:
        clientService.get(clientId)

        then:
        1 * mockClientRepository.findById("1") >> Optional.empty()
        thrown(ResourceNotFoundException)
    }

    def "should get a clients page"() {
        given:
        def pageable = PageRequest.of(0, 2)
        def client1 = new ClientEntity()
        client1.setId("1")
        client1.setName("NAME")
        client1.setNameAr("NAME_AR")
        def client2 = new ClientEntity()
        client2.setId("2")
        client2.setName("NAME2")
        client2.setNameAr("NAME_AR2")

        when:
        def clientsPage = clientService.getAll(pageable, "")

        then:
        1 * mockClientRepository.findAll(pageable) >> {
            new PageImpl<>(List.of(client1, client2))
        }
        2 * mockClientMapper.toDto(_ as ClientEntity) >> { args ->
            def c = args[0] as ClientEntity
            ClientDto.builder().id(c.id).name(c.name).nameAr(c.nameAr).build()
        }

        expect:
        clientsPage.size == 2
        clientsPage.totalElements == 2
    }

    def "should get a clients page filtered by search query"() {
        given:
        def pageable = PageRequest.of(0, 2)
        def client1 = new ClientEntity()
        client1.setId("1")
        client1.setName("NAME")
        client1.setNameAr("NAME_AR")
        def client2 = new ClientEntity()
        client2.setId("2")
        client2.setName("NAME2")
        client2.setNameAr("NAME_AR2")

        when:
        def clientsPage = clientService.getAll(pageable, "NAME")

        then:
        1 * mockClientRepository.findBySearchQuery(pageable, "NAME") >> {
            new PageImpl<>(List.of(client1))
        }
        1 * mockClientMapper.toDto(_ as ClientEntity) >> { args ->
            def c = args[0] as ClientEntity
            ClientDto.builder().id(c.id).name(c.name).nameAr(c.nameAr).build()
        }

        expect:
        clientsPage.size == 1
        clientsPage.totalElements == 1
    }

    def "should delete an existing client"() {
        given:
        def client = ClientDto.builder().id("1").build()
        def clientEntity = new ClientEntity()

        when:
        clientService.delete(client)

        then:
        1 * mockClientRepository.findById(client.id) >> Optional.of(clientEntity)
        1 * mockClientRepository.delete(clientEntity)
    }
}