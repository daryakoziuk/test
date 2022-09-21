package com.dmdev.mapper;

import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserMapperTest {

    private final UserMapper userMapper = UserMapper.getInstance();

    @Test
    @DisplayName("user will be mapped if all is correct")
    void shouldReturnUserDtoIfAllCorrect() {
        User user = User.builder()
                .role(Role.USER)
                .name("Ira")
                .email("ira@gmail.com")
                .id(1)
                .gender(Gender.FEMALE)
                .birthday(LocalDate.of(2012, 12, 12))
                .build();

        UserDto userDto = userMapper.map(user);

        assertThat(userDto.getGender()).isEqualTo(user.getGender());
        assertThat(userDto.getBirthday()).isEqualTo(user.getBirthday());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getName()).isEqualTo(user.getName());
        assertThat(userDto.getRole()).isEqualTo(user.getRole());
    }
}
