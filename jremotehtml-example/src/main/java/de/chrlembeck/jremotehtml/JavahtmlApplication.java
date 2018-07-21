package de.chrlembeck.jremotehtml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class JavahtmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavahtmlApplication.class, args);
	}
}
