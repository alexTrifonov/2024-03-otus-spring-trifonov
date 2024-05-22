package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.LocalizedMessagesService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class Command {

    private final StudentService studentService;

    private final LoginContext loginContext;

    private final TestRunnerService testRunnerService;

    private final LocalizedMessagesService localizedMessagesService;

    @ShellMethod(value = "Login command", key = {"login", "l"})
    public void login() {
        var student = studentService.determineCurrentStudent();
        loginContext.login(student);
    }

    @ShellMethod(value = "Test executing command", key = {"test-run", "test", "t"})
    @ShellMethodAvailability(value = "isTestRunAvailable")
    public void testRun() {
        testRunnerService.run(loginContext.getStudent());
    }

    private Availability isTestRunAvailable() {
        return loginContext.isUserLoggedIn()
                ? Availability.available()
                : Availability.unavailable(localizedMessagesService.getMessage("Login.prompt"));
    }

}
