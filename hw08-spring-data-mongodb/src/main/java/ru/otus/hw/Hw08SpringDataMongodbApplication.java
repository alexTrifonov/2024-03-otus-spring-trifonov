package ru.otus.hw;


import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

@EnableMongock
@EnableMongoRepositories
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class Hw08SpringDataMongodbApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Hw08SpringDataMongodbApplication.class, args);

        AuthorRepository repository = context.getBean(AuthorRepository.class);

        repository.save(new Author("author_id","Dostoevsky"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("\n\n\n----------------------------------------------\n\n");
        System.out.println("Авторы в БД:");
        repository.findAll().forEach(p -> System.out.println(p.getFullName()));
        System.out.println("\n\n----------------------------------------------\n\n\n");
    }

}
