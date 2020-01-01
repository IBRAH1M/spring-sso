package com.example.clientmanagementservice.client


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
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
        def client = new Client()

        when:
        def persistedClient = clientRepository.save(client)

        then:
        def retrievedClient = entityManager.find(Client, "1")
        retrievedClient.id == persistedClient.id
    }

    def "should retrieve a client from the database given it id"() {
        given:
        entityManager.persist(new Client())

        when:
        def persistedClient = clientRepository.findById("2").get()

        then:
        persistedClient.id == '2'
    }
}
