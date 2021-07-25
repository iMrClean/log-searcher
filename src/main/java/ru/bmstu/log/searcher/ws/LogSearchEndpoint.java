package ru.bmstu.log.searcher.ws;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.bmstu.log.searcher.service.LogSearchService;
import ru.bmstu.log.searcher.service.LogSearchValidator;
import ru.bmstu.logsearch.SearchInfo;
import ru.bmstu.logsearch.SearchInfoResult;

@Log4j2
@Endpoint
public class LogSearchEndpoint {

    private static final String NAMESPACE_URI = "http://bmstu.ru/logsearch";

    private final LogSearchService logSearchService;

    private final LogSearchValidator logSearchValidator;

    @Autowired
    public LogSearchEndpoint(LogSearchService logSearchService, LogSearchValidator logSearchValidator) {
	this.logSearchService = logSearchService;
        this.logSearchValidator = logSearchValidator;
    }

    /**
     * Метод поиска логов на сервере, по заданным входным данным
     *
     * @param searchInfo входные данные
     * @return результат операции
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SearchInfo")
    @ResponsePayload
    public SearchInfoResult logSearch(@RequestPayload SearchInfo searchInfo) {
        SearchInfoResult searchInfoResult = logSearchValidator.validate(searchInfo);
        if (searchInfoResult != null) {
            return searchInfoResult;
        }
        searchInfoResult = logSearchService.logSearch(searchInfo);
        logSearchService.fileGenerate(searchInfo, searchInfoResult);
        return searchInfoResult;
    }

}
