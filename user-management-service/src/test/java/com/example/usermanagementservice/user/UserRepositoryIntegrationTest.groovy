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

    def "should retrieve a user from the database given it id"() {
        given:
        def user = new User()
        user.id = '1'
        entityManager.persist(user)
        when:
        def persistedUser = userRepository.findById("1").get()
        println(persistedUser)
        then:
        persistedUser.id == '2'
    }
}
//https://stackoverflow.com/questions/42364935/how-to-add-the-mode-mysql-to-embedded-h2-db-in-spring-boot-1-4-1-for-datajpates