package com.vit.aft;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = Application.class)
@TestPropertySource(
        locations = "classpath:application-yaf.properties")
@Testcontainers
public class TestApplication {

    public static final String YAF_DOCKER_COMPOSE_LOCATION = "src/test/resources/docker-compose.runner.yml";
    public static final String YAF_COMPLETE_LOG_CHECK = "Report successfully generated .+";
    public static final String SUITES_CSV = "src/test/resources/test_model/allure-report/data/suites.csv";

    @RegisterExtension
    public static DockerComposeExtension dockerExtension = DockerComposeExtension.builder()
            .location(YAF_DOCKER_COMPOSE_LOCATION)
            .healthCheck(YAF_COMPLETE_LOG_CHECK)
            .build();

    @SneakyThrows
    @DisplayName("Тесты выполненные YAF-контейнером")
    @TestFactory
    public Collection<DynamicTest> dynamicTestsFromCollection() {
        dockerExtension.launch_YAF();
        Map<String, Boolean> tests = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(SUITES_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("\"Status\"")) {
                    List<String> values = Arrays.asList(line.split(","));
                    boolean result = "\"passed\"".equals(values.get(0));
                    tests.put(values.get(9).substring(1, values.get(9).length() - 1), result);
                }
            }
        }
        return tests.entrySet().stream()
                .map(row -> dynamicTest(row.getKey(), () -> assertTrue(row.getValue())))
                .collect(Collectors.toList());
    }
}