package com.example.clientmanagementservice.client


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject

import javax.persistence.EntityManager

@DataJpaTest
class ClientRepositoryIntegrationTest extends Specification {

    @Autowired
    private EntityManager entityManager

    @Subject
    @Autowired
    ClientRepository clientRepository

    def cleanup() {
        println('Cleaning up after a test!')
        clientRepository.deleteAll()
    }

    def "should save a client to the database"() {
        given:
        def client = new ClientEntity()

        when:
        def persistedClient = clientRepository.save(client)

        then:
        def retrievedClient = entityManager.find(ClientEntity, "1")
        retrievedClient.id == persistedClient.id
    }

    def "should retrieve a client from the database given it id"() {
        given:
        entityManager.persist(new ClientEntity())

        when:
        def persistedClient = clientRepository.findById("2").get()

        then:
        persistedClient.id == '2'
    }

    def "should retrieve clients page filtered by search query"() {
        given:
        def client1 = new ClientEntity()
        client1.setName("NAME")
        client1.setNameAr("NAME_AR")
        def client2 = new ClientEntity()
        client2.setName("TEST")
        client2.setNameAr("TEST_AR")
        entityManager.persist(client1)
        entityManager.persist(client2)

        when:
        def clientsPage = clientRepository.findBySearchQuery(PageRequest.of(0, 2), "NAME")

        then:
        clientsPage.totalElements == 1
        clientsPage.content[0].name == 'NAME'
    }
}
