package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.Application;
import ru.otus.hw.domain.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Application.class)
@TestPropertySource("classpath:test.properties")
@DisplayName("Интеграционный тест класса, читающего вопросы")
public class CsvQuestionDaoTest {

    @Autowired
    private QuestionDao questionDao;

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
        assertEquals("What is the name of the annotation for specifying a file with settings for tests?",
                question.text());
        assertEquals(2, question.answers().size());
        assertEquals("@TestPropertySource", question.answers().stream()
                        .filter(Answer::isCorrect)
                        .findFirst().get().text());
    }
}
