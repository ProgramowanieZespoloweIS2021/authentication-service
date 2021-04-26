package com.eszop.authenticationservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({ "com.eszop.authenticationservice.task" })
public class SpringTaskConfig {
}
