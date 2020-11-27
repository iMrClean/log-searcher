package ru.mos.eirc.log.searcher.util.adapter;

import org.springframework.format.Formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    @Override
    public LocalDateTime parse(String text, Locale locale) {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
	return LocalDateTime.parse(text, formatter);
    }

    @Override
    public String print(LocalDateTime localDateTime, Locale locale) {
	return localDateTime.toString();
    }
}
