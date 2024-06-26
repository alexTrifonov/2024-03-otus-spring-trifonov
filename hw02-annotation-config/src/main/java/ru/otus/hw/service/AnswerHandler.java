package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

public interface AnswerHandler {

    String getUserAnswer();

    boolean checkAnswer(Question question, String selectedAnswerCode);
}
