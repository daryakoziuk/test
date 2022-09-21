package com.dmdev.mapper;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateUserMapperTest {

    private final CreateUserMapper createUserMapper = CreateUserMapper.getInstance();

    @Test
    @DisplayName("user will be returned if everything is correct")
    void shouldReturnUserIfAllCorrect() {
        CreateUserDto userDto = CreateUserDto.builder()
                .gender(Gender.FEMALE.name())
                .role(Role.USER.name())
                .birthday("2012-12-12")
                .password("123")
                .name("Ira")
                .email("ira@gmail.com")
                .build();

        User correctUser = createUserMapper.map(userDto);

        assertThat(correctUser.getGender()).isEqualTo(Gender.valueOf(userDto.getGender()));
        assertThat(correctUser.getRole()).isEqualTo(Role.valueOf(userDto.getRole()));
        assertThat(correctUser.getBirthday()).isEqualTo(LocalDate.parse(userDto.getBirthday()));
        assertThat(correctUser.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(correctUser.getName()).isEqualTo(userDto.getName());
        assertThat(correctUser.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    @DisplayName("exception will be thrown if birthday isn't correct")
    void shouldNotReturnUserIfBirthdayWrong() {
        CreateUserDto userDto = CreateUserDto.builder()
                .gender(Gender.FEMALE.name())
                .role(Role.USER.name())
                .birthday("112-12-12")
                .password("123")
                .name("Ira")
                .email("ira@gmail.com")
                .build();

        assertThrows(DateTimeParseException.class,
                () -> createUserMapper.map(userDto));
    }
}
