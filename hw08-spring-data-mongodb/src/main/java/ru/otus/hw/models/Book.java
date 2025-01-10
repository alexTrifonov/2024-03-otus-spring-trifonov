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

@Data
@ToString(exclude = {"author", "genre"})
@EqualsAndHashCode(exclude = {"author", "genre"})
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    @Field(name = "title", write = Field.Write.NON_NULL, targetType = FieldType.STRING)
    private String title;

    @DBRef
    private Author author;

    @DBRef
    private Genre genre;
}
