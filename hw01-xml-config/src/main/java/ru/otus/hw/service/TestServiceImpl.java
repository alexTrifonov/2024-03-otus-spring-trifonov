package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = questionDao.findAll();
        questions.forEach(question -> {
                    ioService.printFormattedLine("%s (%s)", question.text(), "Choose one of the answers");
                    IntStream.range(0, question.answers().size())
                                    .forEach(index -> ioService.printFormattedLine("%d. %s", index + 1,
                                            question.answers().get(index).text()));
                    ioService.printLine("");
                });
    }
}
