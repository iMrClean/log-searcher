package ru.mos.eirc.log.searcher.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Setter
@ToString
@EnableConfigurationProperties(ApplicationProperties.class)
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private static Path domainPath;

    static {
	domainPath = Paths.get(System.getProperty("user.dir"));
	while (!domainPath.endsWith("domains")) {
	    if (domainPath.getParent() == null) {
		domainPath = Paths.get(System.getProperty("user.dir") + "/src/main/resources/logs");
		break;
	    }
	    domainPath = domainPath.getParent();
	}
    }

    public static Path getDomainPath() {
	return domainPath;
    }

    private Logs logs;

    @Getter
    @Setter
    @ToString
    private static class Logs {
	private String path;
	private String fileLink;
	private String deletionInterval;
	private String fileExistTime;
	private String threadsNumber;
    }
}
