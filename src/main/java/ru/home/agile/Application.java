package ru.home.agile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan("ru.home.agile.Service")
@EntityScan(basePackages = "ru/home/agile/entities")
@EnableJpaRepositories (basePackages = "ru.home.agile.repository")
public class Application {

    public static void main(String[] args) {

         SpringApplication.run(Application.class, args);

    }


}
