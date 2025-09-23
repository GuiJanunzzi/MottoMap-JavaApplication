package br.com.fiap.mottomap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MottoMapJavaApplication {

    public static void main(String[] args) {

        SpringApplication.run(MottoMapJavaApplication.class, args);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("local123")); // gerando senha para testes
    }

}