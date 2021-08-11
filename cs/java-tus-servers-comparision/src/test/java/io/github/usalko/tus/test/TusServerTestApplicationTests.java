package io.github.usalko.tus.test;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
class TusServerTestApplicationTests {

	@Autowired
	ServletWebServerApplicationContext server;

	@Test
	void contextLoads() {
		Integer localHttpPort = server.getWebServer().getPort();
		// Given
		String name = RandomStringUtils.randomAlphabetic( 8 );
		Map<String, Object> value = Map.ofEntries(Map.entry("", 1));
		given()
				.contentType(ContentType.JSON)
				.when()
				.body(value)
				.post(String.format("http://localhost:%s/videos", localHttpPort));
	}

}
