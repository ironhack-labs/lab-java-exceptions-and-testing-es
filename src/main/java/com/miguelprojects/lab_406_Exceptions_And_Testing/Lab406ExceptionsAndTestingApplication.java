package com.miguelprojects.lab_406_Exceptions_And_Testing;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lab406ExceptionsAndTestingApplication {

    public static void main(String[] args) {

//        ObjectMapper om = new ObjectMapper();
//
//        // support Java 8 date time apis
//        om.registerModule(new JavaTimeModule());
//
//        // or like this
//        //om.findAndRegisterModules();

        SpringApplication.run(Lab406ExceptionsAndTestingApplication.class, args);
    }

}
