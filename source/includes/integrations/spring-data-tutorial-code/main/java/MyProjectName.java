package com.mongodb.examples.springdatabulkinsert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// start-application
@SpringBootApplication
public class MyProjectName implements CommandLineRunner {

    @Value("${documentCount}")
    private int count;
    private static final Logger LOG = LoggerFactory
            .getLogger(MyProjectName.class);

    @Autowired
    private ProductRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(MyProjectName.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        repository.bulkInsertProducts(count);
        LOG.info("End run");
        System.exit(1);
    }
}
// end-application
