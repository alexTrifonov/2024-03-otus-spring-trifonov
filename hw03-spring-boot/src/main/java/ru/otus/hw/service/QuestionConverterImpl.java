package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Question;

@Service
public class QuestionConverterImpl implements QuestionConverter {
    @Override
    public String convertQuestionToString(Question question) {
        var sb = new StringBuilder();
        sb.append(question.text())
                .append(System.lineSeparator());
        for (int i = 0; i < question.answers().size(); i++) {
            sb.append(String.format("%d. %s", i + 1, question.answers().get(i).text()))
                    .append(System.lineSeparator());

        }
        return sb.toString();
    }
}
