package ru.mos.eirc.log.searcher.service;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mos.eirc.log.searcher.service.component.FileGenerate;
import ru.mos.eirc.log.searcher.service.component.LogSearch;

@Log4j2
@Service
public class LogSearchServiceImpl implements LogSearchService {

    private final LogSearch logSearch;

    private final FileGenerate fileGenerate;

    @Autowired
    public LogSearchServiceImpl(LogSearch logSearch, FileGenerate fileGenerate) {
	this.logSearch = logSearch;
	this.fileGenerate = fileGenerate;
    }

    public SearchInfoResult logSearch(SearchInfo searchInfo) {
	SearchInfoResult searchInfoResult = logSearch.logSearch(searchInfo);
	if (searchInfo.isRealization()) {
	    fileGenerate.fileGenerate(searchInfo, searchInfoResult);
	}
	return searchInfoResult;
    }

}
