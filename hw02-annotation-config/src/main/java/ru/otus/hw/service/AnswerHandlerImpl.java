package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Question;


@Service
@RequiredArgsConstructor
public class AnswerHandlerImpl implements AnswerHandler {

    private final IOService ioService;

    private final AnswerService answerService;

    @Override
    public String getUserAnswer() {
        ioService.printLine("Enter only the number of the correct answer:");
        var selectedAnswerCode = ioService.readString();
        ioService.printLine("");
        return selectedAnswerCode;
    }

    @Override
    public boolean checkAnswer(Question question, String selectedAnswerCode) {
        return selectedAnswerCode.equals(answerService.getCorrectAnswerCode(question));
    }
}
