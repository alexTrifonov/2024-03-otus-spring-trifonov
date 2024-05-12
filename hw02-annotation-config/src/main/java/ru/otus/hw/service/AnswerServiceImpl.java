package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Override
    public String getCorrectAnswerCode(Question question) {
        List<Answer> answerList = question.answers();
        String correctCode = null;
        for (int i = 0; i < answerList.size(); i++) {
            if (answerList.get(i).isCorrect()) {
                correctCode = String.valueOf(i + 1);
                break;
            }
        }
        return correctCode;
    }
}
