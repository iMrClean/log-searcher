package ru.bmstu.log.searcher.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application")
@EnableConfigurationProperties(value = ApplicationProperties.class)
public class ApplicationProperties {

    private static Path domainPath;

    static {
	domainPath = Paths.get(System.getProperty("user.dir"));
	while (!domainPath.endsWith("domains")) {
	    if (domainPath.getParent() == null) {
		domainPath = Paths.get("");
		break;
	    }
	    domainPath = domainPath.getParent();
	}
    }

    public static Path getDomainPath() {
	return domainPath;
    }

    public static Path getAbsolutePath() {
        return Paths.get("/home/anomaly/projects/log-searcher/files");
    }
    //    @Value("${logs.path}")
//    private String path;
//
//    @Value("${logs.fileLink}")
//    private String fileLink;
//
//    @Value("${logs.deletionInterval}")
//    private int deletionInterval;
//
//    @Value("${logs.fileExistTime}")
//    private int fileExistTime;
//
//    @Value("${logs.threadsNumber}")
//    private int threadsNumber;
}
