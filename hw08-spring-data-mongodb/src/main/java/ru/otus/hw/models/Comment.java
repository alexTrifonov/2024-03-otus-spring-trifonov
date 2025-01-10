package ru.otus.hw.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"book"})
@EqualsAndHashCode(exclude = {"book"})
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    @Field(name = "text", write = Field.Write.NON_NULL, targetType = FieldType.STRING)
    private String text;


    @DBRef
    private Book book;
}
