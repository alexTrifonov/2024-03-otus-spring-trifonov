package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Question;

@Service
@RequiredArgsConstructor
public class AskingServiceImpl implements AskingService {

    private final IOService ioService;

    @Override
    public void askQuestion(Question question) {
        ioService.printLine(question.text());
        for (int i = 0; i < question.answers().size(); i++) {
            ioService.printFormattedLine("%d. %s", i + 1, question.answers().get(i).text());
        }
        ioService.printLine("");
    }
}
