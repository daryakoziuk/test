package com.dmdev.integration.dao;

import com.dmdev.dao.UserDao;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.integration.IntegrationTestBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

public class UserDaoIT extends IntegrationTestBase {

    private UserDao userDao = UserDao.getInstance();
    private static final User IVAN = User.builder()
            .id(1)
            .birthday(LocalDate.of(1990, 01, 10))
            .gender(Gender.MALE)
            .role(Role.ADMIN)
            .name("Ivan")
            .email("ivan@gmail.com")
            .password("111")
            .build();

    @Test
    @DisplayName("all users will be shown")
    void shouldReturnAllUsers() {
        Assertions.assertThat(userDao.findAll()).hasSize(5);
    }

    @Test
    @DisplayName("user will be shown")
    void shouldReturnUser() {
        Optional<User> optionalUser = userDao.findById(1);

        Assertions.assertThat(optionalUser).isPresent();
    }

    @Test
    @DisplayName("user won't be returned if user doesnt exist")
    void shouldReturnEmptyIfUserDoesntExist() {
        Optional<User> optionalUser = userDao.findById(10000);

        Assertions.assertThat(optionalUser).isEmpty();
    }

    @Test
    @DisplayName("user will be saved if everything is correct")
    void shouldSaveUserIfAllCorrect() {
        User user = User.builder()
                .password("123")
                .email("marta@gmail.com")
                .name("Marta")
                .role(Role.USER)
                .gender(Gender.FEMALE)
                .birthday(LocalDate.of(2012, 12, 12))
                .build();

        User save = userDao.save(user);

        Assertions.assertThat(save.getId()).isEqualTo(6);
    }

    @Test
    @DisplayName("user will be found")
    void shouldFindExistingUser() {
        Optional<User> optionalUser = userDao.findByEmailAndPassword(IVAN.getEmail(), IVAN.getPassword());

        Assertions.assertThat(optionalUser.get()).isEqualTo(IVAN);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForFindUser")
    @DisplayName("user will be empty if email or password is wrong")
    void shouldReturnEmptyIfWrongEmailOrPassword(String email, String password) {
        Optional<User> optionalUser = userDao.findByEmailAndPassword(email, password);

        Assertions.assertThat(optionalUser).isEmpty();
    }

    static Stream<Arguments> getArgumentsForFindUser() {
        return Stream.of(
                Arguments.of("dummy", "111"),
                Arguments.of("ivan@gmail.com", "dummy")
        );
    }

    @Test
    @DisplayName("user will be deleted")
    void shouldDeleteUser() {
        Assertions.assertThat(userDao.delete(1)).isEqualTo(true);
    }

    @Test
    @DisplayName("user won't be deleted if user doesn't exist")
    void shouldNotDeleteUserIfDoesntExist() {
        Assertions.assertThat(userDao.delete(1000)).isEqualTo(false);
    }

    @Test
    @DisplayName("user will be updated")
    void shouldUpdateUser() {
        User user = userDao.findById(1).get();
        user.setName("Vanechka");

        userDao.update(user);
        User actualUser = userDao.findById(1).get();

        Assertions.assertThat(actualUser.getName()).isEqualTo(user.getName());
    }
}
