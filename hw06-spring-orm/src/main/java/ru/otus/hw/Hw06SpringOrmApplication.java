package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class Hw06SpringOrmApplication {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(Hw06SpringOrmApplication.class, args);
    }

}
