package com.vit.aft;

import io.vavr.control.Try;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Builder
public class DockerComposeExtension implements BeforeAllCallback {

    private static final List<String> removalArtifacts = List.of(
            "src/test/resources/test_model/allure-report",
            "src/test/resources/test_model/allure-result"
    );
    private static DockerComposeContainer<?> yafRunner;

    private String location;
    private String healthCheck;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        removalArtifacts.stream()
                .map(Path::of)
                .filter(Files::exists)
                .forEach(path -> Try.run(() -> Files.walk(path)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete)));

        yafRunner = new DockerComposeContainer<>(new File(this.location))
                .waitingFor("yaf_runner", Wait.forLogMessage(this.healthCheck, 1))
                .withLocalCompose(true);
    }

    public void launch_YAF() {
        log.warn("\n\n//////////  RUN TESTS !  ////////////\n");
        yafRunner.start();
        log.warn("\n\n////////  TEST COMPLETE !  /////////\n");
    }
}
