package com.app.lms.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.app.lms.controller.LibraryAdminController;
import com.app.lms.controller.LibraryManagementController;
import com.app.lms.model.LibrarySection;
import com.app.lms.service.LibraryManagementService;

/**
 * Configuration class for the application.
 * 
 * @author karve
 *
 */

@Configuration
@EnableScheduling
public class AppConfig {

	@Autowired
	private Environment env;

	@Autowired
	private LibraryAdminController lac;

	@Autowired
	private LibraryManagementController lmc;

	@Autowired
	private LibraryManagementService lms;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Cron job to update the expired subscriptions. Runs every 5 mins. Note : It is
	 * updating all active subscriptions.
	 */
	@Scheduled(fixedDelay = 300000)
	public void updateExpiredSubscriptions() {
		logger.info("Cron Job Started");
		lms.expireSubscription();
		logger.info("Cron Job Finished");
	}

	/**
	 * CORS Configuration.
	 * 
	 * @return {@link WebMvcConfigurer}
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:4200").allowedMethods("*");
			}
		};
	}

	/**
	 * Adds dummy data to the database if the spring.jpa.hibernate.ddl-auto property
	 * is 'create'.
	 */
	@Bean
	public void addDummyData() {
		if (!env.getRequiredProperty("spring.jpa.hibernate.ddl-auto").equals("create"))
			return;
		try {
			addLibrarySections();
			addSubscriptionPackages();
			addBooks();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void addLibrarySections() throws IOException {
		try (Stream<String> stream = Files.lines(Path.of("src/main/resources/LMS_Sections.csv")).skip(1)) {
			stream.map(line -> {
				String[] sarr = line.split(",");
				return new LibrarySection(sarr[0], sarr[1]);
			}).forEach(lac::addLibrarySection);
		}
	}

	private void addSubscriptionPackages() throws IOException {
		try (Stream<String> stream = Files.lines(Path.of("src/main/resources/LMS_Packages.txt"))) {
			stream.forEach(lac::addSubscriptionPackage);
		}
	}

	private void addBooks() throws IOException {
		final String json = "{ \"sectionId\": \"%s\", \"BookTitle\": { \"title\": \"%s\", \"author\": \"%s\"}, \"BookCopy\": { \"price\": \"%s\"} }";
		try (Stream<String> stream = Files
				.lines(Path.of("src/main/resources/LMS_Books.csv"), StandardCharsets.ISO_8859_1).skip(1)) {
			stream.parallel().map(line -> String.format(json, (Object[]) line.split(","))).forEach(lmc::addBook);
		}
	}

}
