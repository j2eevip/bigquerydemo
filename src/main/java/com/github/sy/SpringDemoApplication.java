package com.github.sy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Sherlock
 */
@SpringBootApplication
public class SpringDemoApplication {

    public static void main(String[] args) {
        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "7890");
        SpringApplication.run(SpringDemoApplication.class, args);
    }

}
