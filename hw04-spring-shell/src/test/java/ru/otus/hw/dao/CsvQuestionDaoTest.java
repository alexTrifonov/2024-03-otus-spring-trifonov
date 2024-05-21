package ru.otus.hw.dao;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Answer;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;



@SpringBootTest
@DisplayName("Интеграционный тест класса, читающего вопросы")
public class CsvQuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private AppProperties appProperties;

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
        if (Locale.forLanguageTag("en-US").equals(appProperties.getLocale())) {
            assertEquals("What is the name of the annotation for specifying a file with settings for tests?",
                    question.text());
        }
        if (Locale.forLanguageTag("ru-RU").equals(appProperties.getLocale())) {
            assertEquals("Как называется аннотация для указания файла с настройками для тестов?",
                    question.text());
        }
        assertEquals(2, question.answers().size());
        assertEquals("@TestPropertySource", question.answers().stream()
                .filter(Answer::isCorrect)
                .findFirst().get().text());
    }
}
