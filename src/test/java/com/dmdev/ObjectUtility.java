package com.dmdev;

import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class ObjectUtility {

    public static final User IVAN = User.builder()
            .id(1)
            .birthday(LocalDate.of(1990, 01, 10))
            .gender(Gender.MALE)
            .role(Role.ADMIN)
            .name("Ivan")
            .email("ivan@gmail.com")
            .password("111")
            .build();
}
