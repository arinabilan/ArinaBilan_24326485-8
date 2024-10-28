package config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
//@EnableConfigurationProperties(FileStorageProperties.class)
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    //@Value("${file.upload-dir}")
    private String uploadDir;

}
