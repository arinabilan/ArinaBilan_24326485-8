package bank.app.appbank;

import config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties.class)
public class AppBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppBankApplication.class, args);
    }

}
