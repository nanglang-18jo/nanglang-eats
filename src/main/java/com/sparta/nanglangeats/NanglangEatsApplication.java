package com.sparta.nanglangeats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NanglangEatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NanglangEatsApplication.class, args);
        System.out.println("Environment Variable: " + System.getenv("AWS_ACCESS_KEY"));
    }


}
