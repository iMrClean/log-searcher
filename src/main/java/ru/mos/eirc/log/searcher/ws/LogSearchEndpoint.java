package ru.mos.eirc.log.searcher.ws;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.mos.eirc.log.searcher.service.LogSearchService;
import ru.mos.eirc.log.searcher.service.validator.LogSearchValidator;

@Endpoint
public class LogSearchEndpoint {

    private static final String NAMESPACE_URI = "http://entities.logsearch.example.com";

    private final LogSearchService logSearchService;

    private final LogSearchValidator logSearchValidator;

    @Autowired
    public LogSearchEndpoint(LogSearchService logSearchService, LogSearchValidator logSearchValidator) {
        this.logSearchService = logSearchService;
        this.logSearchValidator = logSearchValidator;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SearchInfo")
    @ResponsePayload
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        SearchInfoResult searchInfoResult = logSearchValidator.validate(searchInfo);
        if (searchInfoResult != null) {
            return searchInfoResult;
        }
        return logSearchService.logSearch(searchInfo);
    }

}
