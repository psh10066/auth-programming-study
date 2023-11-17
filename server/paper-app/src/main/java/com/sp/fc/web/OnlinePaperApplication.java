package com.sp.fc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {
    "com.sp.fc.config",
    "com.sp.fc.web",
    "com.sp.fc.site"
})
@EnableJpaAuditing
public class OnlinePaperApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinePaperApplication.class, args);
    }
}
