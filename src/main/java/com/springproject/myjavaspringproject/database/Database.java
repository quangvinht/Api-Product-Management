package com.springproject.myjavaspringproject.database;


import com.springproject.myjavaspringproject.model.Product;
import com.springproject.myjavaspringproject.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    //connect to docker
//>docker run -d --rm --name mysql-spring-boot -e MYSQL_ROOT_PASSWORD=0104 -e MYSQl_USER=vinh -e MYSQL_PASSWORD=0104 -e MYSQL_DATABASE=tesdb  mysql:
//latest

    //logger
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    // create some data for database because we dont have any data at first (initial data in database)
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {

        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                Product product_1 = new Product( "Iphone", 2023, 2300.000, "");
//                Product product_2 = new Product( "Xiaomi", 2022, 800.000, "");
//                Product product_3 = new Product( "Samsung", 2020, 3000.000, "");
//
//                logger.info("Insert data :" + productRepository.save(product_1));
//                logger.info("Insert data :" + productRepository.save(product_2));
//                logger.info("Insert data :" + productRepository.save(product_3));


            }
        };
    }
}
