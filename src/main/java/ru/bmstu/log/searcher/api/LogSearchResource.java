package ru.bmstu.log.searcher.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bmstu.log.searcher.service.LogSearchService;
import ru.bmstu.log.searcher.service.LogSearchValidator;
import ru.bmstu.logsearch.SearchInfo;
import ru.bmstu.logsearch.SearchInfoResult;

@Log4j2
@RestController
@RequestMapping("/api")
public class LogSearchResource {

    private final LogSearchService logSearchService;

    private final LogSearchValidator logSearchValidator;

    @Autowired
    public LogSearchResource(LogSearchService logSearchService, LogSearchValidator logSearchValidator) {
	this.logSearchService = logSearchService;
	this.logSearchValidator = logSearchValidator;
    }

    /**
     * Метод поиска логов на сервере, по заданным входным данным
     *
     * @param searchInfo входные данные
     * @return результат операции
     */
    @PostMapping("/logSearch")
    public ResponseEntity<SearchInfoResult> logSearch(@RequestBody SearchInfo searchInfo) {
	SearchInfoResult searchInfoResult = logSearchValidator.validate(searchInfo);
	if (searchInfoResult != null) {
	    return ResponseEntity.badRequest().body(searchInfoResult);
	}
	searchInfoResult = logSearchService.logSearch(searchInfo);
	logSearchService.fileGenerate(searchInfo, searchInfoResult);
	return ResponseEntity.ok(searchInfoResult);
    }

}
