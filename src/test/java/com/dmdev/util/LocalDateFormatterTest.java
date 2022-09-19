package com.dmdev.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LocalDateFormatterTest {

    @Test
    @DisplayName("check is successful if date format is valid")
    void checkDateFormatWhenItIsTrue() {
        String date = "2012-12-12";

        LocalDate actualDate = LocalDateFormatter.format(date);

        assertThat(actualDate).isEqualTo(LocalDate.of(2012, 12, 12));
    }

    @Test
    @DisplayName("exception will be thrown if date is invalid")
    void shouldThrowsExceptionWhenInvalidFormat() {
        String date = "12-12-2012";

        assertThrows(DateTimeParseException.class, () ->
                LocalDateFormatter.format(date));
    }

    @ParameterizedTest
    @CsvSource({
            "12-12-12",
            "dummy",
            "20123"
    })
    @DisplayName("date formatter is checked if date is invalid")
    void checkFormatterDateFunctionality(String date) {
        boolean invalid = LocalDateFormatter.isValid(date);

        assertFalse(invalid);
    }
}
