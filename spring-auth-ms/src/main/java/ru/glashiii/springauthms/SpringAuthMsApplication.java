package ru.glashiii.springauthms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.glashiii.springauthms.config.JwtProperties;

@SpringBootApplication
public class SpringAuthMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAuthMsApplication.class, args);
    }

}
