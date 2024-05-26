package ru.otus.hw.dao;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
@DisplayName("Интеграционный тест класса, читающего вопросы")
public class CsvQuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

    @MockBean
    private AppProperties appProperties;

    @BeforeEach
    public void setUp() {
        when(appProperties.getTestFileName()).thenReturn("testQuestions_ru.csv");
    }

    @Test
    @DisplayName("загружает все вопросы из csv файла")
    public void shouldGetAllQuestion() {
        assertEquals(5, questionDao.findAll().size());
    }

    @Test
    @DisplayName("корректно парсит строку csv файла")
    public void shouldCorrectParse() {
        var questionList = questionDao.findAll();
        var question = questionList.get(2);
        assertEquals("Как называется аннотация для указания файла с настройками для тестов?",
                question.text());
        assertEquals(2, question.answers().size());
        assertEquals("@TestPropertySource", question.answers().stream()
                .filter(Answer::isCorrect)
                .findFirst().get().text());
    }
}
