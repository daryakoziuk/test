package com.dmdev.integration.dao;

import com.dmdev.dao.UserDao;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.integration.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static com.dmdev.ObjectUtility.IVAN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UserDaoIT extends IntegrationTestBase {

    private final UserDao userDao = UserDao.getInstance();

    @Test
    @DisplayName("all users will be shown")
    void shouldReturnAllUsers() {
        assertThat(userDao.findAll()).hasSize(5);
    }

    @Test
    @DisplayName("user will be shown")
    void shouldReturnUser() {
        Optional<User> optionalUser = userDao.findById(1);

        assertThat(optionalUser).isPresent();
    }

    @Test
    @DisplayName("user won't be returned if user doesnt exist")
    void shouldReturnEmptyIfUserDoesntExist() {
        Optional<User> optionalUser = userDao.findById(10000);

        assertThat(optionalUser).isEmpty();
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

        assertNotNull(save.getId());
    }

    @Test
    @DisplayName("user will be found")
    void shouldFindExistingUser() {
        Optional<User> optionalUser = userDao.findByEmailAndPassword(IVAN.getEmail(), IVAN.getPassword());

        assertThat(optionalUser).isPresent();
        assertThat(optionalUser.get()).isEqualTo(IVAN);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForFindUser")
    @DisplayName("user will be empty if email or password is wrong")
    void shouldReturnEmptyIfWrongEmailOrPassword(String email, String password) {
        Optional<User> optionalUser = userDao.findByEmailAndPassword(email, password);

        assertThat(optionalUser).isEmpty();
    }

    static Stream<Arguments> getArgumentsForFindUser() {
        return Stream.of(
                //return empty user because invalid email
                Arguments.of("dummy", "111"),
                //return emptu user because invalid password
                Arguments.of("ivan@gmail.com", "dummy")
        );
    }

    @Test
    @DisplayName("user will be deleted")
    void shouldDeleteUser() {
        assertThat(userDao.delete(IVAN.getId())).isEqualTo(true);
    }

    @Test
    @DisplayName("user won't be deleted if user doesn't exist")
    void shouldNotDeleteUserIfDoesntExist() {
        assertThat(userDao.delete(1000)).isEqualTo(false);
    }

    @Test
    @DisplayName("user will be updated")
    void shouldUpdateUser() {
        User user = userDao.findById(1).get();
        user.setName("Vanechka");

        userDao.update(user);
        User actualUser = userDao.findById(1).get();

        assertThat(actualUser.getName()).isEqualTo(user.getName());
    }
}
