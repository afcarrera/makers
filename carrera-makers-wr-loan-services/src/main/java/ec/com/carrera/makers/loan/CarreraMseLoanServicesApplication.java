package ec.com.carrera.makers.loan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "ec.com.carrera.makers.loan", "ec.com.carrera.makers.security",})
public class CarreraMseLoanServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarreraMseLoanServicesApplication.class, args);
	}

}
