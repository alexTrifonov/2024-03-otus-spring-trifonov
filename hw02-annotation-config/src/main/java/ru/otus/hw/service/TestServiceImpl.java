package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final AskingService askingService;

    private final AnswerHandler answerHandler;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        for (var question: questions) {
            askingService.askQuestion(question);
            var selectedAnswerCode = answerHandler.getUserAnswer();
            var isAnswerValid = answerHandler.checkAnswer(question, selectedAnswerCode);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
