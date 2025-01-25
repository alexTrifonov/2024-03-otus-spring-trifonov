package ru.otus.hw.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "genres")
public class Genre {
    @Id
    private String id;

    @Field(name = "name", write = Field.Write.NON_NULL, targetType = FieldType.STRING)
    private String name;
}
