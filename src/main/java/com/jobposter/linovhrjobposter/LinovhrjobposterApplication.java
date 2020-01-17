package com.jobposter.linovhrjobposter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@AutoConfigurationPackage
@ComponentScan(basePackages= {"com.jobposter.dao", "com.jobposter.service", "com.jobposter.controller", "com.jobposter.config"})
@EnableTransactionManagement
@EntityScan(basePackages= {"com.jobposter.entity"})
public class LinovhrjobposterApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(LinovhrjobposterApplication.class, args);
	}

}
