package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Тест класса, выполняющего тестирование")
@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {
    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    private AskingService askingService;

    @Mock
    private AnswerHandler answerHandler;

    @InjectMocks
    private TestServiceImpl testService;

    private final List<Question> questionList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Question question = new Question("questionOne", List.of(new Answer("one", true), new Answer("two", false)));
        Question question1 = new Question("questionTwo", List.of(new Answer("A", false), new Answer("B", true)));
        questionList.add(question);
        questionList.add(question1);
    }

    @Test
    @DisplayName("должен получить объект TestResult")
    void shouldGetTestResult() {
        when(questionDao.findAll()).thenReturn(questionList);
        when(answerHandler.getUserAnswer()).thenReturn("1");
        when(answerHandler.checkAnswer(any(Question.class), any(String.class))).thenReturn(true);
        Student student = new Student("Igor", "Ivanov");
        TestResult testResult = testService.executeTestFor(student);
        assertEquals(2, testResult.getRightAnswersCount());
        assertEquals(2, testResult.getAnsweredQuestions().size());
    }
}