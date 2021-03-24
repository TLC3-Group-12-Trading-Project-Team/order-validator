package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(basePackages = "com.example.ordervalidation")
public class OrderValidatorApplication {

	@Autowired
	private Environment env;

	private static final String MARKETDATA = "/webhooks/market-data";

	@Bean
	private static RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	public static void main(String[] args) {
		SpringApplication.run(OrderValidatorApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws RestClientException {
		return args -> {
			// * Use a webhook
			// subscribe to exchange
			restTemplate.postForEntity(
					"https://exchange.matraining.com/md/subscription",
					env.getProperty("app.host").concat(MARKETDATA),
					null
			);
		};
	}
}
