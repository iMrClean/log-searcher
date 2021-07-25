package ru.bmstu.log.searcher.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String text) {
	return LocalDateTime.parse(text);
    }

    @Override
    public String marshal(LocalDateTime date) {
	return date != null ? date.toString() : null;
    }

}
