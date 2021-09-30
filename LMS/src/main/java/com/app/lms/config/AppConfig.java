package com.app.lms.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.app.lms.controller.LibraryAdminController;
import com.app.lms.controller.LibraryManagementController;
import com.app.lms.dao.AuxiliaryDao;
import com.app.lms.model.LibrarySection;

@Configuration
public class AppConfig {

	@Autowired
	private Environment env;

	@Autowired
	LibraryAdminController lac;

	@Autowired
	LibraryManagementController lmc;

	@Autowired
	@Qualifier("AuxiliaryDao")
	private AuxiliaryDao auxiliaryDao;

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
			br.readLine();
			String json = "{ \"sectionId\": \"%s\", \"BookTitle\": { \"title\": \"%s\", \"author\": \"%s\"}, \"BookCopy\": { \"price\": \"%d\"} }";
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
