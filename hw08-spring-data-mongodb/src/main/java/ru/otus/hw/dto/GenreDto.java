package ru.otus.hw.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import ru.otus.hw.models.Genre;

@Data
public class GenreDto {

    @CsvBindByName(column = "id")
    private String id;

    @CsvBindByName(column = "name")
    private String name;

    public Genre toDomainObject() {
        return new Genre(id, name);
    }
}
