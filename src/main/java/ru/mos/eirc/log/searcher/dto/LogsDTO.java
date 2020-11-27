package ru.mos.eirc.log.searcher.dto;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Logs", propOrder = { "searchInfo", "searchInfoResult" })
@XmlRootElement(name = "logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogsDTO {

    private SearchInfo searchInfo;

    private SearchInfoResult searchInfoResult;

}
