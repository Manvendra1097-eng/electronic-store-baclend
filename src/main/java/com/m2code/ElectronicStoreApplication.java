package com.m2code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElectronicStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectronicStoreApplication.class, args);
    }
/*

    @Bean
    public CommandLineRunner runner(
            RoleRepository repository
    ) {
        return args -> {
            Role user = new Role();
            user.setRole(_Role.ROLE_USER.name());
            Role admin = new Role();
            admin.setRole(_Role.ROLE_ADMIN.name());
            repository.saveAll(List.of(user, admin));
        };
    }
*/

}
