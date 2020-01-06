package com.example.usermanagementservice.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification
import spock.lang.Subject

import javax.persistence.EntityManager

@DataJpaTest
class UserRepositoryIntegrationTest extends Specification {

    @Autowired
    private EntityManager entityManager

    @Subject
    @Autowired
    UserRepository userRepository

    def cleanup() {
        println('Cleaning up after a test!')
        userRepository.deleteAll()
    }

    def "should save a user to the database"() {
        given:
        def user = new UserEntity()

        when:
        def persistedUser = userRepository.save(user)

        then:
        def retrievedUser = entityManager.find(UserEntity, "1")
        retrievedUser.id == persistedUser.id
    }

    def "should retrieve a user from the database given it id"() {
        given:
        entityManager.persist(new UserEntity())

        when:
        def persistedUser = userRepository.findById("2").get()

        then:
        persistedUser.id == '2'
    }

    def "should retrieve users page filtered by search query"() {
        given:
        def user1 = new UserEntity()
        user1.setName("NAME")
        user1.setNameAr("NAME_AR")
        def user2 = new UserEntity()
        user2.setName("TEST")
        user2.setNameAr("TEST_AR")
        entityManager.persist(user1)
        entityManager.persist(user2)

        when:
        def usersPage = userRepository.findBySearchQuery(PageRequest.of(0, 2), "NAME")

        then:
        usersPage.totalElements == 1
        usersPage.content[0].name == 'NAME'
    }
}