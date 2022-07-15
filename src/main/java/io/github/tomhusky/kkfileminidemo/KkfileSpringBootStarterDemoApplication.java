package io.github.tomhusky.kkfileminidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KkfileSpringBootStarterDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(KkfileSpringBootStarterDemoApplication.class, args);
    }

}
