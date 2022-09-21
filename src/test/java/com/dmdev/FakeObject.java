package com.dmdev;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class FakeObject {

    public static final User IRA = User.builder()
            .id(1)
            .birthday(LocalDate.of(2012, 12, 12))
            .gender(Gender.FEMALE)
            .role(Role.USER)
            .name("Ira")
            .email("ira@gmail.com")
            .password("123")
            .build();
    public static final CreateUserDto CREATE_USER_DTO = CreateUserDto.builder()
            .password("123")
            .email("ira@gmail.com")
            .name("Ira")
            .birthday("2012-12-12")
            .role("USER")
            .gender("FEMALE")
            .build();
    public static final UserDto EXPECTED_USER = UserDto.builder()
            .birthday(LocalDate.of(2012, 12, 12))
            .email("ira@gmail.com")
            .id(1)
            .gender(Gender.FEMALE)
            .role(Role.USER)
            .name("Ira")
            .build();
}
