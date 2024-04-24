package com.mslup.lot.lotcrud;

import java.io.File;
import java.time.Duration;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@SpringBootTest
class LotCrudApplicationTests {

	private static final String POSTGRES_NAME = "db-test";
	private static final int POSTGRES_PORT = 5432;
	private static final String POSTGRES_JDBC = "jdbc:postgresql://%s:%s/%s";
	private static final String POSTGRES_DB = "lotcrud";
	private static final String POSTGRES_USERNAME = "compose-postgres";
	private static final String POSTGRES_PASSWORD = "compose-postgres";

	public static DockerComposeContainer environment =
		new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
			.withExposedService(POSTGRES_NAME, POSTGRES_PORT,
				Wait.forListeningPort().withStartupTimeout(
					Duration.ofSeconds(30)));

	@BeforeAll
	public static void start() {
		environment.start();
	}


	@DynamicPropertySource
	public static void overrideProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> String.format(POSTGRES_JDBC,
			environment.getServiceHost(POSTGRES_NAME, POSTGRES_PORT),
			environment.getServicePort(POSTGRES_NAME, POSTGRES_PORT),
			POSTGRES_DB));
		registry.add("spring.datasource.username", () -> POSTGRES_USERNAME);
		registry.add("spring.datasource.password", () -> POSTGRES_PASSWORD);
	}

}
