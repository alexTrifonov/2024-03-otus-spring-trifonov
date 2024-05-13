package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionConverter questionConverter;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLineLocalized("TestService.answer.the.questions");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        for (var question: questions) {
            ioService.printLine(questionConverter.convertQuestionToString(question));
            var selectedAnswerCode = getUserAnswer(question);
            var isAnswerValid = checkAnswer(question, selectedAnswerCode);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private int getUserAnswer(Question question) {
        var selectedAnswerCode = ioService.readIntForRangeWithPromptLocalized(1, question.answers().size(),
                "AnswerHandler.prompt", "TestService.answer.error.message");
        ioService.printLine("");
        return selectedAnswerCode;
    }

    private boolean checkAnswer(Question question, int selectedAnswerCode) {
        return selectedAnswerCode == getCorrectAnswerCode(question);
    }

    private int getCorrectAnswerCode(Question question) {
        List<Answer> answerList = question.answers();
        int correctCode = -1;
        for (int i = 0; i < answerList.size(); i++) {
            if (answerList.get(i).isCorrect()) {
                correctCode = i + 1;
                break;
            }
        }
        return correctCode;
    }

}
