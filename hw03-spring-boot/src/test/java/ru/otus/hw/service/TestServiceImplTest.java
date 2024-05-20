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
    private LocalizedIOService ioService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    private QuestionConverter questionConverter;

    @InjectMocks
    private TestServiceImpl testService;

    private final List<Question> questionList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Question question = new Question("questionOne", List.of(new Answer("one", true), new Answer("two", false)));
        Question question1 = new Question("questionTwo", List.of(new Answer("A", true), new Answer("B", false)));
        questionList.add(question);
        questionList.add(question1);
    }

    @Test
    @DisplayName("должен получить объект TestResult ")
    void shouldGetTestResult() {
        when(questionDao.findAll()).thenReturn(questionList);
        when(questionConverter.convertQuestionToString(any(Question.class))).thenReturn("question");
        when(ioService.readIntForRangeWithPromptLocalized(any(Integer.class), any(Integer.class), any(String.class), any(String.class)))
                .thenReturn(1);
        Student student = new Student("Igor", "Ivanov");
        TestResult testResult = testService.executeTestFor(student);
        assertEquals(2, testResult.getRightAnswersCount());
        assertEquals(2, testResult.getAnsweredQuestions().size());
    }
}