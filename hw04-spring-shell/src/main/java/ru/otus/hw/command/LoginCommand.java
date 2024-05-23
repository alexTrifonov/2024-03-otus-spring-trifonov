package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.StudentService;

@ShellComponent
@RequiredArgsConstructor
public class LoginCommand {

    private final StudentService studentService;

    private final LoginContext loginContext;

    @ShellMethod(value = "Login command", key = {"login", "l"})
    public void login() {
        var student = studentService.determineCurrentStudent();
        loginContext.login(student);
    }

}
