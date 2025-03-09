package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Адрес http://localhost:8080
 * Регистрация http://localhost:8080/register
 * Выход http://localhost:8080/logout
 */

@SpringBootApplication
public class Hw12SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(Hw12SpringSecurityApplication.class, args);
    }
}
