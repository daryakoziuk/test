package com.dmdev.validator;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateUserValidatorTest {

    private CreateUserValidator createUserValidator = CreateUserValidator.getInstance();

    @Test
    @DisplayName("check is successful if all is validate")
    void checkUserWhenIsValidated() {
        CreateUserDto userDto = CreateUserDto.builder()
                .birthday("2001-12-12")
                .email("svewta@gmail.com")
                .gender(Gender.FEMALE.name())
                .name("Sveta")
                .role(Role.USER.name())
                .password("123")
                .build();
        ValidationResult validationResult = createUserValidator.validate(userDto);

        assertTrue(validationResult.isValid());
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForValidTest")
    @DisplayName("check is successful if arguments is invalid")
    void shouldShowIfInvalidUser(String birthday, String role, String gender, String codeError) {
        CreateUserDto userDto = CreateUserDto.builder()
                .birthday(birthday)
                .role(role)
                .gender(gender)
                .build();

        ValidationResult validationResult = createUserValidator.validate(userDto);

        assertFalse(validationResult.isValid());
        assertThat(validationResult.getErrors().get(0).getCode()).isEqualTo(codeError);
    }

    static Stream<Arguments> getArgumentsForValidTest() {
        return Stream.of(
                Arguments.of("12-11-2005", "USER", "FEMALE", "invalid.birthday"),
                Arguments.of("2004-12-23", "dummy", "FEMALE", "invalid.role"),
                Arguments.of("2005-11-12", "USER", "dummy", "invalid.gender")
        );
    }
}
