package com.dmdev.integration.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.exception.ValidationException;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.service.UserService;
import com.dmdev.validator.CreateUserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static com.dmdev.ObjectUtility.IVAN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceIT extends IntegrationTestBase {

    private UserService userService;

    @BeforeEach
    void init(){
        userService= new UserService(CreateUserValidator.getInstance(),
                UserDao.getInstance(), CreateUserMapper.getInstance(),
                UserMapper.getInstance());
    }

    @Test
    @DisplayName("user will be shown")
    void shouldReturnUser() {
        Optional<UserDto> actualUser = userService.login(IVAN.getEmail(), IVAN.getPassword());

        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getEmail()).isEqualTo(IVAN.getEmail());
    }

    @ParameterizedTest
    @CsvSource({
            //not login because email is wrong
            "dummy,123",
            //not login because password is wrong
            "ivan@gmail.com, dummy"
    })
    @DisplayName("user won't login if email or password is wrong")
    void shouldNotLoginIfEmailOrPasswordIsWrong(String email, String password) {
        Optional<UserDto> optionalUser = userService.login(email, password);

        assertThat(optionalUser).isEmpty();
    }

    @Test
    @Tag("createUser")
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
    @Tag("createUser")
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
    @Tag("createUser")
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
    @Tag("createUser")
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
