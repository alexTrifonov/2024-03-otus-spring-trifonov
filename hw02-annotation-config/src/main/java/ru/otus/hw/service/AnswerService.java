package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

public interface AnswerService {
    String getCorrectAnswerCode(Question question);
}
