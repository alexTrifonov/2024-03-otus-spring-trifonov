package ru.otus.hw.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Интеграционный тест класса настроек")
class AppPropertiesTest {

    @Autowired
    private AppProperties appProperties;

    @Test
    @DisplayName("Содержит правильное название файла с вопросами в зависимости от локали")
    void mustContainTheCorrectFileName() {
        if (Locale.forLanguageTag("en-US").equals(appProperties.getLocale())) {
            assertEquals("testQuestions.csv",
                    appProperties.getTestFileName());
        }
        if (Locale.forLanguageTag("ru-RU").equals(appProperties.getLocale())) {
            assertEquals("testQuestions_ru.csv",
                    appProperties.getTestFileName());
        }
    }
}