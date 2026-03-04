package com.example.sandbox.service;

import com.example.sandbox.entity.User;
import com.example.sandbox.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User alice;
    private User bob;

    @BeforeEach
    void setUp() {
        alice = User.builder().id(1L).name("Alice").email("alice@example.com").build();
        bob   = User.builder().id(2L).name("Bob").email("bob@example.com").build();
    }

    // -------------------------------------------------------------------------
    // findAll
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("returns all users from repository")
        void returnsAllUsers() {
            when(userRepository.findAll()).thenReturn(List.of(alice, bob));

            List<User> result = userService.findAll();

            assertThat(result).containsExactly(alice, bob);
        }

        @Test
        @DisplayName("returns empty list when no users exist")
        void returnsEmptyList() {
            when(userRepository.findAll()).thenReturn(Collections.emptyList());

            List<User> result = userService.findAll();

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("delegates to repository exactly once")
        void delegatesToRepositoryOnce() {
            when(userRepository.findAll()).thenReturn(List.of(alice));

            userService.findAll();

            verify(userRepository, times(1)).findAll();
            verifyNoMoreInteractions(userRepository);
        }
    }

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("returns user when id exists")
        void returnsUserWhenFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(alice));

            Optional<User> result = userService.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(alice);
        }

        @Test
        @DisplayName("returns empty Optional when id does not exist")
        void returnsEmptyWhenNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<User> result = userService.findById(99L);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("passes the correct id to repository")
        void passesCorrectIdToRepository() {
            when(userRepository.findById(2L)).thenReturn(Optional.of(bob));

            userService.findById(2L);

            verify(userRepository).findById(2L);
        }

        @Test
        @DisplayName("delegates to repository exactly once")
        void delegatesToRepositoryOnce() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(alice));

            userService.findById(1L);

            verify(userRepository, times(1)).findById(1L);
            verifyNoMoreInteractions(userRepository);
        }
    }

    // -------------------------------------------------------------------------
    // findByEmail
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("findByEmail")
    class FindByEmail {

        @Test
        @DisplayName("returns user when email exists")
        void returnsUserWhenFound() {
            when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(alice));

            Optional<User> result = userService.findByEmail("alice@example.com");

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Alice");
            assertThat(result.get().getEmail()).isEqualTo("alice@example.com");
        }

        @Test
        @DisplayName("returns empty Optional when email does not exist")
        void returnsEmptyWhenNotFound() {
            when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

            Optional<User> result = userService.findByEmail("unknown@example.com");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("passes the exact email string to repository")
        void passesExactEmailToRepository() {
            when(userRepository.findByEmail("bob@example.com")).thenReturn(Optional.of(bob));

            userService.findByEmail("bob@example.com");

            verify(userRepository).findByEmail("bob@example.com");
        }

        @Test
        @DisplayName("delegates to repository exactly once")
        void delegatesToRepositoryOnce() {
            when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(alice));

            userService.findByEmail("alice@example.com");

            verify(userRepository, times(1)).findByEmail("alice@example.com");
            verifyNoMoreInteractions(userRepository);
        }
    }

    // -------------------------------------------------------------------------
    // save
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("save")
    class Save {

        @Test
        @DisplayName("returns the saved user with generated id")
        void returnsSavedUserWithId() {
            User newUser   = User.builder().name("Charlie").email("charlie@example.com").build();
            User savedUser = User.builder().id(3L).name("Charlie").email("charlie@example.com").build();
            when(userRepository.save(newUser)).thenReturn(savedUser);

            User result = userService.save(newUser);

            assertThat(result.getId()).isEqualTo(3L);
            assertThat(result.getName()).isEqualTo("Charlie");
            assertThat(result.getEmail()).isEqualTo("charlie@example.com");
        }

        @Test
        @DisplayName("returns updated user when saving existing entity")
        void returnsUpdatedUser() {
            User updated = User.builder().id(1L).name("Alice Updated").email("alice@example.com").build();
            when(userRepository.save(updated)).thenReturn(updated);

            User result = userService.save(updated);

            assertThat(result.getName()).isEqualTo("Alice Updated");
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("passes the user object to repository unchanged")
        void passesUserToRepository() {
            User newUser = User.builder().name("Dana").email("dana@example.com").build();
            when(userRepository.save(newUser)).thenReturn(newUser);

            userService.save(newUser);

            verify(userRepository).save(newUser);
        }

        @Test
        @DisplayName("delegates to repository exactly once")
        void delegatesToRepositoryOnce() {
            User newUser = User.builder().name("Eve").email("eve@example.com").build();
            when(userRepository.save(newUser)).thenReturn(newUser);

            userService.save(newUser);

            verify(userRepository, times(1)).save(newUser);
            verifyNoMoreInteractions(userRepository);
        }
    }

    // -------------------------------------------------------------------------
    // deleteById
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("deleteById")
    class DeleteById {

        @Test
        @DisplayName("delegates delete to repository with correct id")
        void delegatesDeleteToRepository() {
            userService.deleteById(1L);

            verify(userRepository).deleteById(1L);
        }

        @Test
        @DisplayName("delegates to repository exactly once")
        void delegatesToRepositoryOnce() {
            userService.deleteById(1L);

            verify(userRepository, times(1)).deleteById(1L);
            verifyNoMoreInteractions(userRepository);
        }

        @Test
        @DisplayName("does not throw when id does not exist (no-op)")
        void doesNotThrowForMissingId() {
            doNothing().when(userRepository).deleteById(999L);

            org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> userService.deleteById(999L));

            verify(userRepository).deleteById(999L);
        }
    }
}
