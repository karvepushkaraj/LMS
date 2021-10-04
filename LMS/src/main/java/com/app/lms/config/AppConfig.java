package com.app.lms.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.app.lms.controller.LibraryAdminController;
import com.app.lms.controller.LibraryManagementController;
import com.app.lms.model.LibrarySection;
import com.app.lms.service.LibraryManagementService;

/**
 * Adds dummy data for Library Sections, Subscription Packages and Books in
 * create mode.
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
	LibraryAdminController lac;

	@Autowired
	LibraryManagementController lmc;

	@Autowired
	LibraryManagementService lms;

	private Logger logger = LoggerFactory.getLogger(AppConfig.class);

	@Scheduled(fixedDelay = 300000)
	public void updateExpiredSubscriptions() {
		logger.info("Cron Job Started");
		lms.expireSubscription();
		logger.info("Cron Job Finished");
	}

	@Bean
	public void addDummyData() {
		if (!env.getRequiredProperty("spring.jpa.hibernate.ddl-auto").equals("create"))
			return;
		addLibrarySections();
		addSubscriptionPackages();
		addBooks();
	}

	private void addLibrarySections() {
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/LMS_Sections.csv"))) {
			String s;
			br.readLine();
			while ((s = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(s, ",");
				LibrarySection ls = new LibrarySection(st.nextToken(), st.nextToken());
				lac.addLibrarySection(ls);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addSubscriptionPackages() {
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/LMS_Packages.txt"))) {
			String s;
			while ((s = br.readLine()) != null)
				lac.addSubscriptionPackage(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addBooks() {
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/LMS_Books.csv"))) {
			String s;
			String json = "{ \"sectionId\": \"%s\", \"BookTitle\": { \"title\": \"%s\", \"author\": \"%s\"}, \"BookCopy\": { \"price\": \"%d\"} }";
			br.readLine();
			while ((s = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(s, ",");
				String input = String.format(json, st.nextToken(), st.nextToken(), st.nextToken(),
						Integer.parseInt(st.nextToken()));
				lmc.addBook(input);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
