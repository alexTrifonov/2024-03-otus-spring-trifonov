package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class AuthorCommands {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @ShellMethod(value = "Find all authors", key = "aa")
    public String findAllAuthors() {
        return authorService.findAll().stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // ains newAuthor
    @ShellMethod(value = "Insert author", key = "ains")
    public String insertBook(String fullName) {
        Author author = new Author();
        author.setFullName(fullName);
        var savedAuthor = authorService.save(author);
        return authorConverter.authorToString(savedAuthor);
    }
}
