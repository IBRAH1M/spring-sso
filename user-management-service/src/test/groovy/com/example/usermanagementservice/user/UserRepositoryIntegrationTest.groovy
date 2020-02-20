package com.example.usermanagementservice.user

import com.example.usermanagementservice.UserManagementServiceConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject

import javax.persistence.EntityManager

@DataJpaTest
@Import(UserManagementServiceConfiguration.class)
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

    def "should save a user to the database with auditing info"() {
        given:
        def user = new UserEntity()
        user.setClientId("CLIENT_ID")

        when:
        def persistedUser = userRepository.save(user)

        then:
        def retrievedUser = entityManager.find(UserEntity, persistedUser.id)
        retrievedUser.id == persistedUser.id
        retrievedUser.createdBy != null
        retrievedUser.createdDate != null
        retrievedUser.lastModifiedBy != null
        retrievedUser.lastModifiedDate != null
    }

    def "should retrieve users page filtered by search query"() {
        given:
        def user1 = new UserEntity()
        user1.setName("NAME")
        user1.setNameAr("NAME_AR")
        user1.setClientId("CLIENT_ID")
        def user2 = new UserEntity()
        user2.setName("TEST")
        user2.setNameAr("TEST_AR")
        user2.setClientId("CLIENT_ID")
        entityManager.persist(user1)
        entityManager.persist(user2)

        when:
        def usersPage = userRepository.findBySearchQuery(PageRequest.of(0, 2), "NAME")

        then:
        usersPage.totalElements == 1
        usersPage.content[0].name == 'NAME'
    }
}