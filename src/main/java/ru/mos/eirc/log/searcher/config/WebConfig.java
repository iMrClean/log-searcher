package ru.mos.eirc.log.searcher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mos.eirc.log.searcher.util.adapter.LocalDateTimeFormatter;

@Configuration
public class WebConfig {

    @Bean
    public LocalDateTimeFormatter dateTimeFormatter() {
	return new LocalDateTimeFormatter();
    }

}
