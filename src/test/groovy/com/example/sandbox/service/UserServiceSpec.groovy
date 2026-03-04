package com.example.sandbox.service

import com.example.sandbox.entity.User
import com.example.sandbox.repository.UserRepository
import spock.lang.Specification
import spock.lang.Subject

class UserServiceSpec extends Specification {

    UserRepository userRepository = Mock()

    @Subject
    UserService userService = new UserService(userRepository)

    def "findAll returns list of users"() {
        given:
        def users = [User.builder().id(1L).name("Alice").email("alice@example.com").build()]
        userRepository.findAll() >> users

        when:
        def result = userService.findAll()

        then:
        result == users
        result.size() == 1
        result[0].name == "Alice"
    }

    def "findById returns user when found"() {
        given:
        def user = User.builder().id(1L).name("Bob").email("bob@example.com").build()
        userRepository.findById(1L) >> Optional.of(user)

        when:
        def result = userService.findById(1L)

        then:
        result.isPresent()
        result.get().name == "Bob"
    }

    def "findById returns empty optional when user does not exist"() {
        given:
        userRepository.findById(99L) >> Optional.empty()

        when:
        def result = userService.findById(99L)

        then:
        result.isEmpty()
    }

    def "save persists and returns the user"() {
        given:
        def user = User.builder().name("Charlie").email("charlie@example.com").build()
        def saved = User.builder().id(3L).name("Charlie").email("charlie@example.com").build()
        userRepository.save(user) >> saved

        when:
        def result = userService.save(user)

        then:
        result.id == 3L
        result.name == "Charlie"
    }

    def "deleteById delegates to repository"() {
        when:
        userService.deleteById(1L)

        then:
        1 * userRepository.deleteById(1L)
    }

    def "findByEmail returns user when email matches"() {
        given:
        def user = User.builder().id(2L).name("Diana").email("diana@example.com").build()
        userRepository.findByEmail("diana@example.com") >> Optional.of(user)

        when:
        def result = userService.findByEmail("diana@example.com")

        then:
        result.isPresent()
        result.get().email == "diana@example.com"
    }
}
