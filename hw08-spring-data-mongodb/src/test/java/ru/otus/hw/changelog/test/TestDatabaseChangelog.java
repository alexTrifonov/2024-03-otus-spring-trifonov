package ru.otus.hw.changelog.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.config.FileNameProvider;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.AuthorFillException;
import ru.otus.hw.exceptions.BookFillException;
import ru.otus.hw.exceptions.CommentFillException;
import ru.otus.hw.exceptions.GenreFillException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@ChangeLog
public class TestDatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDb", author = "alexTrifonov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }


    @ChangeSet(order = "002", id = "insertAuthors", author = "alexTrifonov")
    public void insertAuthors(AuthorService authorService, FileNameProvider fileNameProvider) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getAuthorFile());
             InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            CsvToBean<AuthorDto> csvToBean = new CsvToBeanBuilder<AuthorDto>(inputStreamReader)
                    .withType(AuthorDto.class)
                    .withSeparator(';')
                    .build();
            List<Author> authors = csvToBean.parse().stream()
                    .map(AuthorDto::toDomainObject).collect(Collectors.toCollection(ArrayList::new));
            authorService.saveAll(authors);
        } catch (Exception e) {
            throw new AuthorFillException(e.getMessage(), e);
        }
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "alexTrifonov")
    public void insertGenres(MongockTemplate mongockTemplate, FileNameProvider fileNameProvider) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getGenreFile());
             InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            CsvToBean<GenreDto> csvToBean = new CsvToBeanBuilder<GenreDto>(inputStreamReader)
                    .withType(GenreDto.class)
                    .withSeparator(';')
                    .build();
            List<Genre> genres = csvToBean.parse().stream()
                    .map(GenreDto::toDomainObject).collect(Collectors.toCollection(ArrayList::new));
            mongockTemplate.insertAll(genres);
        } catch (Exception e) {
            throw new GenreFillException(e.getMessage(), e);
        }
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "alexTrifonov")
    public void insertBooks(AuthorService aService, GenreService gServ, BookService bServ, FileNameProvider provider) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(provider.getBookFile());
             InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            CsvToBean<BookDto> csvToBean = new CsvToBeanBuilder<BookDto>(inputStreamReader)
                    .withType(BookDto.class)
                    .withSeparator(';')
                    .build();
            List<Book> books = csvToBean.parse().stream()
                    .map(BookDto::toDomainObject).collect(Collectors.toCollection(ArrayList::new));

           List<Author> authors = aService.findAll();
           List<Genre> genres = gServ.findAll();
           List<Book> booksWithAuthorAndGenres = createBooks(books, authors, genres);
           booksWithAuthorAndGenres.forEach(bServ::save);
        } catch (Exception e) {
            throw new BookFillException(e.getMessage(), e);
        }
    }

    @ChangeSet(order = "005", id = "insertComments", author = "alexTrifonov")
    public void insertComments(FileNameProvider provider, BookService bookService, CommentService commentService) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(provider.getCommentFile());
             InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            CsvToBean<CommentDto> csvToBean = new CsvToBeanBuilder<CommentDto>(inputStreamReader)
                    .withType(CommentDto.class)
                    .withSeparator(';')
                    .build();
            List<Comment> comments = csvToBean.parse().stream()
                    .map(CommentDto::toDomainObject).collect(Collectors.toCollection(ArrayList::new));
            List<Book> books = bookService.findAll();
            for (int i = 0; i < comments.size(); i++) {
                Comment comment = comments.get(i);
                if (i < books.size()) {
                    comment.setBook(books.get(i));
                    commentService.save(comment);
                } else {
                    comment.setBook(books.get(0));
                    commentService.save(comment);
                }
            }
        } catch (Exception e) {
            throw new CommentFillException(e.getMessage(), e);
        }
    }

    private List<Book> createBooks(List<Book> books, List<Author> authors, List<Genre> genres)  {
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (i < authors.size() && i < genres.size()) {
                book.setAuthor(authors.get(i));
                book.setGenre(genres.get(i));
            } else {
                book.setAuthor(authors.get(0));
                book.setGenre(genres.get(0));
            }
        }
        return books;
    }
}
