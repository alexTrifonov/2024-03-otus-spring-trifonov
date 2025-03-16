package ru.otus.hw.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listPage(Model model) {
        List<BookDto> books = bookService.findAll().stream()
                .map(BookDto::fromBook).toList();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/add-book")
    public String addBook(Model model) {
        model.addAttribute("book", new BookDto(null, null, new AuthorDto(null, null), new GenreDto(null, null)));
        addAuthorsAndGenresAttributes(model);
        return "addBook";
    }

    @PostMapping("/add-book")
    public String addBook(@Valid @ModelAttribute("book") BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> log.error(error.getDefaultMessage()));
            return "addBook";
        }
        return saveBook(bookDto);
    }

    @PostMapping("/delete-book/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "deleteBook";
    }

    @GetMapping("/edit-book/{id}")
    public String editBook(Model model, @PathVariable("id") Long id) {
        BookDto bookDto = bookService.findById(id)
                .map(BookDto::fromBook)
                        .orElseThrow(EntityNotFoundException::new);
        model.addAttribute("book", bookDto);
        addAuthorsAndGenresAttributes(model);
        return "editBook";
    }

    @PostMapping("/edit-book")
    public String saveBook(@Valid @ModelAttribute("book") BookDto bookDto,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> log.error(error.getDefaultMessage()));
            return "editBook";
        }
        return saveBook(bookDto);
    }

    private void addAuthorsAndGenresAttributes(Model model) {
        List<AuthorDto> authors = authorService.findAll().stream()
                .map(AuthorDto::fromAuthor).toList();
        List<GenreDto> genres = genreService.findAll().stream()
                .map(GenreDto::fromGenre).toList();
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
    }

    private String saveBook(BookDto bookDto) {
        log.debug("bookDto: {}", bookDto);
        Book book = bookDto.toBook();
        bookService.update(book);
        return "redirect:/";
    }
}
