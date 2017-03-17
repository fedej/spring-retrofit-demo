package com.example;

import com.example.autoconfigure.EnableRetrofitService;
import com.example.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
@EnableRetrofitService(basePackage = "com.example.service.retrofit")
class SpringRetrofitDemoApplication {

    @Autowired
    private DemoService demoService;

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> demoService.getUser();
    }

    public static void main(String[] args) throws IOException {
        SpringApplication.run(SpringRetrofitDemoApplication.class, args);
    }

}
