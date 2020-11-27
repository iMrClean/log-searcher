package ru.mos.eirc.log.searcher.service;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;

public interface LogSearchService {

    SearchInfoResult logSearch(SearchInfo searchInfo);

}
