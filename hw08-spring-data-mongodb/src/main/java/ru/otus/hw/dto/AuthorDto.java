package ru.otus.hw.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import ru.otus.hw.models.Author;

@Data
public class AuthorDto {
    @CsvBindByName(column = "id")
    private String id;

    @CsvBindByName(column = "full_name")
    private String fullName;

    public Author toDomainObject() {
        return new Author(id, fullName);
    }

}
