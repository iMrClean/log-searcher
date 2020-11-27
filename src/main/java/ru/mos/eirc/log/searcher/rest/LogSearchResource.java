package ru.mos.eirc.log.searcher.rest;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mos.eirc.log.searcher.service.LogSearchService;
import ru.mos.eirc.log.searcher.service.validator.LogSearchValidator;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
public class LogSearchResource {

    private final LogSearchService logSearchService;

    private final LogSearchValidator logSearchValidator;

    @Autowired
    public LogSearchResource(LogSearchService logSearchService, LogSearchValidator logSearchValidator) {
	this.logSearchService = logSearchService;
	this.logSearchValidator = logSearchValidator;
    }

    @PostMapping("/logSearch")
    public ResponseEntity<SearchInfoResult> logSearch(@Valid @RequestBody SearchInfo searchInfo) {
	SearchInfoResult searchInfoResult = logSearchValidator.validate(searchInfo);
	if (searchInfoResult != null) {
	    return ResponseEntity.badRequest().body(searchInfoResult);
	}
	return ResponseEntity.ok(logSearchService.logSearch(searchInfo));
    }
}
