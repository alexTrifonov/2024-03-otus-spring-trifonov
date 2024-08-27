package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DisplayName("Интеграционный тест сервиса книг")
@Import({BookServiceImpl.class})
public class BookServiceTest {
    @Autowired
    private BookServiceImpl bookService;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
    }

    @Test
    @DisplayName("должен находить книгу по id")
    public void shouldFindBookById() {
        Optional<Book> book = bookService.findById(1L);
        Author author = dbAuthors.get(0);
        Genre genre = dbGenres.get(0);
        Book expectedBook = new Book(1L, "BookTitle_1", author, genre);
        assertThat(book).isPresent()
                .get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("должен находить все книги")
    public void shouldFindAllBooks() {
        List<Book> books = bookService.findAll();
        assertThat(books.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("должен создавать книгу")
    public void shouldSaveBook() {
        Author author = dbAuthors.get(0);
        Genre genre = dbGenres.get(0);
        Book persistBook = bookService.insert("BookTitle", author.getId(), genre.getId());
        Book expectedBook = bookService.findById(persistBook.getId()).orElse(new Book());
        assertThat(expectedBook).usingRecursiveComparison().isEqualTo(persistBook);

        //восстановление исходного состояния БД для других тестов. я не нашел как это сделать лучше
        //если знаете решение лучше - подскажите конкретно как сделать
        bookService.deleteById(persistBook.getId());
    }

    @Test
    @DisplayName("должен обновлять книгу")
    public void shouldUpdateBook() {
        Author author = dbAuthors.get(0);
        Genre genre = dbGenres.get(0);

        Book expectedBook = new Book(1L, "newBookTitle", author, genre);
        Book persistBook = bookService.findById(expectedBook.getId()).orElse(new Book());
        assertThat(expectedBook).isNotEqualTo(persistBook);

        Book updatedBook = bookService.update(expectedBook.getId(), expectedBook.getTitle(),
                expectedBook.getAuthor().getId(), expectedBook.getGenre().getId());
        assertThat(updatedBook.getTitle()).isEqualTo(expectedBook.getTitle());

        Book foundBook = bookService.findById(updatedBook.getId()).orElse(new Book());
        assertThat(foundBook).usingRecursiveComparison().isEqualTo(expectedBook);


        //восстановление исходного состояния БД для других тестов. я не нашел как это сделать лучше
        //если знаете решение лучше - подскажите конкретно как сделать
        bookService.update(persistBook.getId(), persistBook.getTitle(), persistBook.getAuthor().getId(),
                persistBook.getGenre().getId());
    }

    @Test
    @DisplayName("должен удалять книгу по id")
    public void shouldDeleteBook() {
        Book book = bookService.findAll().get(0);
        bookService.deleteById(book.getId());

        Optional<Book> optionalEmptyBook = bookService.findById(book.getId());
        assertThat(optionalEmptyBook.isEmpty()).isTrue();


        //восстановление исходного состояния БД для других тестов. я не нашел как это сделать лучше
        //если знаете решение лучше - подскажите конкретно как сделать
        bookService.update(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}
