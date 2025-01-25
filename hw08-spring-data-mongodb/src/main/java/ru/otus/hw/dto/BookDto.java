package ru.otus.hw.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import ru.otus.hw.models.Book;

@Data
public class BookDto {

    @CsvBindByName(column = "id")
    private String id;

    @CsvBindByName(column = "title")
    private String title;

    public Book toDomainObject() {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        return book;
    }
}
