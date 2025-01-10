package ru.otus.hw.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import ru.otus.hw.models.Comment;

@Data
public class CommentDto {

    @CsvBindByName(column = "text")
    private String text;

    public Comment toDomainObject() {
        Comment comment = new Comment();
        comment.setText(text);
        return comment;
    }
}
