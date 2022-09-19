package com.dmdev.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.validator.CreateUserValidator;
import com.dmdev.validator.ValidationResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private CreateUserValidator createUserValidator;
    @Mock
    private UserDao userDao;
    @Mock
    private CreateUserMapper createUserMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;
    private static final User IRA = User.builder()
            .id(1)
            .birthday(LocalDate.of(2012, 12, 12))
            .gender(Gender.FEMALE)
            .role(Role.USER)
            .name("Ira")
            .email("ira@gmail.com")
            .password("123")
            .build();
    private static final CreateUserDto CREATE_USER_DTO = CreateUserDto.builder()
            .password("123")
            .email("ira@gmail.com")
            .name("Ira")
            .birthday("2012-12-12")
            .role("USER")
            .gender("FEMALE")
            .build();
    private static final UserDto EXPECTED_USER = UserDto.builder()
            .birthday(LocalDate.of(2012, 12, 12))
            .email("ira@gmail.com")
            .id(1)
            .gender(Gender.FEMALE)
            .role(Role.USER)
            .name("Ira")
            .build();

    @Test
    @DisplayName("find user if email and login are correct")
    void shouldReturnUserIfEmailAndPasswordCorrect() {
        doReturn(Optional.of(IRA)).when(userDao).findByEmailAndPassword(IRA.getEmail(), IRA.getPassword());
        doReturn(EXPECTED_USER).when(userMapper).map(any());

        Optional<UserDto> optionalUser = userService.login(IRA.getEmail(), IRA.getPassword());

        assertThat(optionalUser.get()).isEqualTo(EXPECTED_USER);
        verify(userDao).findByEmailAndPassword(IRA.getEmail(), IRA.getPassword());
    }

    @ParameterizedTest
    @CsvSource({
            "dummy,123",
            "ira@gmail.com,dummy"
    })
    @DisplayName("user will be empty if email or passoword is wrong")
    void shouldNotReturnUserIfEmailOrPasswordWrong(String email, String password) {
        doReturn(Optional.empty()).when(userDao).findByEmailAndPassword(email, password);

        Optional<UserDto> actualUser = userService.login(email, password);

        assertThat(actualUser).isEmpty();
    }

    @Test
    @DisplayName("create user if everything is correct")
    void shouldCreateUserIfAllCorrect() {
        doReturn(new ValidationResult()).when(createUserValidator).validate(CREATE_USER_DTO);
        doReturn(EXPECTED_USER).when(userMapper).map(any());
        doReturn(IRA).when(userDao).save(any());

        UserDto actual = userService.create(CREATE_USER_DTO);

        assertThat(actual).isEqualTo(EXPECTED_USER);
        verify(createUserValidator).validate(CREATE_USER_DTO);
        verify(createUserMapper).map(CREATE_USER_DTO);
        verify(userDao).save(any());
    }
}
