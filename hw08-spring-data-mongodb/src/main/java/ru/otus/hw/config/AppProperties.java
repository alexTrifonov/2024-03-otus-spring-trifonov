package ru.otus.hw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;



@ConfigurationProperties(prefix = "changelog")
@Data
public class AppProperties implements FileNameProvider {

    private String authorFile;

    private String genreFile;

    private String bookFile;

    private String commentFile;
}
