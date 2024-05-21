package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Тест класса, выполняющего тестирование")
@SpringBootTest
class TestServiceImplTest {
    @MockBean
    private LocalizedIOService ioService;

    @Autowired
    private TestServiceImpl testService;

    @Test
    @DisplayName("должен получить объект TestResult ")
    void shouldGetTestResult() {
        when(ioService.readIntForRangeWithPromptLocalized(
                any(Integer.class), any(Integer.class), any(String.class), any(String.class)))
                .thenReturn(1);
        Student student = new Student("Igor", "Ivanov");
        TestResult testResult = testService.executeTestFor(student);
        assertEquals(3, testResult.getRightAnswersCount());
        assertEquals(5, testResult.getAnsweredQuestions().size());
    }
}