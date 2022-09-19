package com.dmdev.integration.service;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.exception.ValidationException;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceIT extends IntegrationTestBase {

    private UserService userService = new UserService();
    private final static User IVAN = User.builder()
            .password("111")
            .email("ivan@gmail.com")
            .birthday(LocalDate.of(1990, 01, 10))
            .gender(Gender.MALE)
            .role(Role.ADMIN)
            .name("Ivan")
            .id(1)
            .build();

    @Test
    @DisplayName("user will be shown")
    void shouldReturnUser() {
        Optional<UserDto> actualUser = userService.login(IVAN.getEmail(), IVAN.getPassword());

        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getEmail()).isEqualTo(IVAN.getEmail());
    }

    @ParameterizedTest
    @CsvSource({
            "dummy,123",
            "ivan@gmail.com, dummy"
    })
    @DisplayName("user won't login if email or password is wrong")
    void shouldNotLoginIfEmailOrPasswordIsWrong(String email, String password) {
        Optional<UserDto> optionalUser = userService.login(email, password);

        assertThat(optionalUser).isEmpty();
    }

    @Test
    @Tag("create_user")
    @DisplayName("user will be created if everything is correct")
    void shouldCreateUserIfAllCorrect() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .gender("FEMALE")
                .role("USER")
                .birthday("2012-12-12")
                .name("Marta")
                .email("marta@mail.ru")
                .password("123")
                .build();

        UserDto actualUser = userService.create(createUserDto);
        Optional<UserDto> optionalUserDto = userService.login(createUserDto.getEmail(), createUserDto.getPassword());

        assertThat(optionalUserDto).isPresent();
        assertThat(optionalUserDto.get().getId()).isEqualTo(6);
        assertThat(optionalUserDto.get()).isEqualTo(actualUser);
    }

    @Test
    @Tag("create_user")
    @DisplayName("user won't be created if birthday is wrong")
    void shouldNotCreateUserIfWrongBirthday() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .gender("FEMALE")
                .role("USER")
                .birthday("12-12-12")
                .name("Marta")
                .email("marta@mail.ru")
                .password("123")
                .build();

        ValidationException validationException = assertThrows(ValidationException.class,
                () -> userService.create(createUserDto));
        assertThat(validationException.getErrors().get(0).getCode()).isEqualTo("invalid.birthday");
    }

    @Test
    @Tag("create_user")
    @DisplayName("user won't be created if role is wrong")
    void shouldNotCreateUserIfWrongRole() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .gender("FEMALE")
                .role("MANAGER")
                .birthday("2012-12-12")
                .name("Marta")
                .email("marta@mail.ru")
                .password("123")
                .build();

        ValidationException validationException = assertThrows(ValidationException.class,
                () -> userService.create(createUserDto));
        assertThat(validationException.getErrors().get(0).getCode()).isEqualTo("invalid.role");
    }

    @Test
    @Tag("create_user")
    @DisplayName("user won't be created if gender is wrong")
    void shouldNotCreateUserIfWrongGender() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .gender("dummy")
                .role("USER")
                .birthday("2012-12-12")
                .name("Marta")
                .email("marta@mail.ru")
                .password("123")
                .build();

        ValidationException validationException = assertThrows(ValidationException.class,
                () -> userService.create(createUserDto));
        assertThat(validationException.getErrors().get(0).getCode()).isEqualTo("invalid.gender");
    }
}
