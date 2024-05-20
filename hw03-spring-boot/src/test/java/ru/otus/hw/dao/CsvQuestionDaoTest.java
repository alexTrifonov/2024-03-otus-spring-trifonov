package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Answer;

import java.util.HashMap;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("Интеграционный тест класса, читающего вопросы")
public class CsvQuestionDaoTest {


    private QuestionDao questionDao;

    private AppProperties appProperties;

    @BeforeEach
    public void setUp() {
        appProperties = new AppProperties();
        appProperties.setLocale("ru-RU");
        appProperties.setFileNameByLocaleTag(new HashMap<>(){{
            put("ru-RU", "testQuestions_ru.csv");
            put("en-US", "testQuestion.csv");
        }});
        appProperties.setRightAnswersCountToPass(3);
        questionDao = new CsvQuestionDao(appProperties);
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
