package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import ru.otus.hw.domain.Student;

@ShellComponent
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    @Override
    public void run(Student student) {
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
        System.exit(0);
    }
}
