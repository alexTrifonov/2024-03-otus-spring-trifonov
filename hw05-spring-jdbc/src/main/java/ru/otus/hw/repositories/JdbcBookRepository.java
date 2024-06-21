package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Book> findById(long id) {
        try {
            return Optional.ofNullable(namedParameterJdbcOperations.queryForObject("""
                select books.id as id, title, author_id, full_name, genre_id, name as genre_name
                from books
                left join authors
                on books.author_id = authors.id
                left join genres
                on books.genre_id = genres.id
                where books.id = :id""",
                    Map.of("id", id), new BookRowMapper()));
        } catch (DataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public List<Book> findAll() {
        return namedParameterJdbcOperations.query("""
                select books.id as id, title, author_id, full_name, genre_id, name as genre_name
                from books
                left join authors
                on books.author_id = authors.id
                left join genres
                on books.genre_id = genres.id
                """,
                new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from books where id = :id", Map.of("id", id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("title", book.getTitle())
                        .addValue("author_id", book.getAuthor().getId())
                        .addValue("genre_id", book.getGenre().getId());

        namedParameterJdbcOperations.update("""
                insert into books(title, author_id, genre_id)
                values (:title, :author_id, :genre_id)""",
                sqlParameterSource, keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        int countUpdatedRow = namedParameterJdbcOperations.update("""
                update books
                set title = :title, author_id = :author_id, genre_id = :genre_id
                where id = :id""",
                Map.of("title", book.getTitle(),
                        "author_id", book.getAuthor().getId(),
                        "genre_id", book.getGenre().getId(),
                        "id", book.getId()));
        if (countUpdatedRow == 0) {
            throw new EntityNotFoundException("count updated row = 0, entity not found");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            String fullName = rs.getString("full_name");
            Author author = new Author(authorId, fullName);
            long genreId = rs.getLong("genre_id");
            String genreName = rs.getString("genre_name");
            Genre genre = new Genre(genreId, genreName);
            Book book = new Book(id, title, author, genre);
            return book;
        }
    }
}
