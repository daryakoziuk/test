package com.dmdev.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.UserDto;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.validator.CreateUserValidator;
import com.dmdev.validator.ValidationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.dmdev.FakeObject.CREATE_USER_DTO;
import static com.dmdev.FakeObject.EXPECTED_USER;
import static com.dmdev.FakeObject.IRA;
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
            //not return because email is wrong
            "dummy,123",
            //not return because password is wrong
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
