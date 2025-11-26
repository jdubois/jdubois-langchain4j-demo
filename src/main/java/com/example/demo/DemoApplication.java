package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class DemoApplication {

	private static final Logger log = Logger.getLogger(DemoApplication.class.getName());

    static void main(String[] args) {
		// Load .env file if OPENAI_BASE_URL, OPENAI_API_KEY, AZURE_SEARCH_ENDPOINT, or AZURE_SEARCH_KEY are not set
		loadEnvironmentVariables();

		SpringApplication app = new SpringApplication(DemoApplication.class);
		// Startup performance optimizations
		app.setLazyInitialization(true);
		app.setRegisterShutdownHook(false);
		app.run(args);
	}

	private static void loadEnvironmentVariables() {
		// Check if the required environment variables are already set
		String openaiBaseUrl = System.getenv("OPENAI_BASE_URL");
		String openaiApiKey = System.getenv("OPENAI_API_KEY");
		String azureSearchEndpoint = System.getenv("AZURE_SEARCH_ENDPOINT");
		String azureSearchKey = System.getenv("AZURE_SEARCH_KEY");

		// If any is not set, try to load from .env file
		if (openaiBaseUrl == null || openaiApiKey == null || azureSearchEndpoint == null || azureSearchKey == null) {
			try {
				Dotenv dotenv = Dotenv.configure()
					.directory(".")
					.ignoreIfMissing()
					.load();

				// Set OPENAI_BASE_URL if not already set
				if (openaiBaseUrl == null && dotenv.get("OPENAI_BASE_URL") != null) {
					System.setProperty("OPENAI_BASE_URL", dotenv.get("OPENAI_BASE_URL"));
				}

				// Set OPENAI_API_KEY if not already set
				if (openaiApiKey == null && dotenv.get("OPENAI_API_KEY") != null) {
					System.setProperty("OPENAI_API_KEY", dotenv.get("OPENAI_API_KEY"));
				}

				// Set AZURE_SEARCH_ENDPOINT if not already set
				if (azureSearchEndpoint == null && dotenv.get("AZURE_SEARCH_ENDPOINT") != null) {
					System.setProperty("AZURE_SEARCH_ENDPOINT", dotenv.get("AZURE_SEARCH_ENDPOINT"));
				}

				// Set AZURE_SEARCH_KEY if not already set
				if (azureSearchKey == null && dotenv.get("AZURE_SEARCH_KEY") != null) {
					System.setProperty("AZURE_SEARCH_KEY", dotenv.get("AZURE_SEARCH_KEY"));
				}
			} catch (Exception e) {
				log.warning("Could not load .env file: " + e.getMessage());
			}
		}
	}
}
