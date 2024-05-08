package ru.otus.hw.dao.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Answer;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс AnswerCsvConverter")
class AnswerCsvConverterTest {
    private static AnswerCsvConverter answerCsvConverter;

    @BeforeAll
    static void setUp() {
        answerCsvConverter = new AnswerCsvConverter();
    }

    @Test
    @DisplayName("должен корректно конвертировать строку в")
    void shouldCorrectConvertToRead() {
        Object answer = answerCsvConverter.convertToRead("Science doesn't know this yet%true");
        assertInstanceOf(Answer.class, answer);
        assertEquals("Science doesn't know this yet", ((Answer) answer).text());
        assertTrue(((Answer) answer).isCorrect());
    }
}