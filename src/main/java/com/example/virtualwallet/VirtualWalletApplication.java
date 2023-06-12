package com.example.virtualwallet;

import com.example.virtualwallet.configuration.HibernateConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableSwagger2
//@Configuration
//@EnableAutoConfiguration(exclude = {VirtualWalletApplication.class})
//@ComponentScan
//@EnableTransactionManagement
//@EnableJpaRepositories
@ComponentScan(excludeFilters  = {@ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = {HibernateConfig.class})})
public class VirtualWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualWalletApplication.class, args);
    }

}
