package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.LocalizedMessagesService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class TestCommand {

    private final TestRunnerService testRunnerService;

    private final LocalizedMessagesService localizedMessagesService;

    private final LoginContext loginContext;

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
