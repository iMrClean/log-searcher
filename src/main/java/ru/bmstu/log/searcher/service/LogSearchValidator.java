package ru.bmstu.log.searcher.service;

import org.springframework.stereotype.Component;
import ru.bmstu.log.searcher.enums.CorrectionCheckResult;
import ru.bmstu.log.searcher.util.ApplicationProperties;
import ru.bmstu.logsearch.FileExtension;
import ru.bmstu.logsearch.SearchInfo;
import ru.bmstu.logsearch.SearchInfoResult;
import ru.bmstu.logsearch.SignificantDateInterval;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static ru.bmstu.log.searcher.enums.CorrectionCheckResult.*;

@Component
public class LogSearchValidator {

    public SearchInfoResult validate(SearchInfo searchInfo) {
	SearchInfoResult searchInfoResult = check3701Error(searchInfo);
	if (searchInfoResult != null)
	    return searchInfoResult;
	searchInfoResult = check44Error(searchInfo);
	if (searchInfoResult != null)
	    return searchInfoResult;
	searchInfoResult = check37Error(searchInfo);
	if (searchInfoResult != null)
	    return searchInfoResult;
	searchInfoResult = checkIntervalsError(searchInfo);
	return searchInfoResult;
    }

    private SearchInfoResult check3701Error(SearchInfo searchInfo) {
	if (searchInfo.isRealization() && searchInfo.getFileExtension() == null) {
	    return CorrectionCheckResult.getSearchInfoError(ERROR3701);
	}
	return null;
    }

    private SearchInfoResult check44Error(SearchInfo searchInfo) {
	Path domainPath = ApplicationProperties.getDomainPath();
	Path locationPath = Paths.get(String.valueOf(domainPath), searchInfo.getLocation());
	if (!locationPath.toFile().exists()) {
	    return CorrectionCheckResult.getSearchInfoError(ERROR44);
	}
	searchInfo.setLocation(String.valueOf(locationPath));

	return null;
    }

    private SearchInfoResult check37Error(SearchInfo searchInfo) {
	if (searchInfo.getRegularExpression() == null) {
	    searchInfo.setRegularExpression("");
	}
	if (searchInfo.getFileExtension() == null){
	    searchInfo.setFileExtension(FileExtension.XML.name());
	}
	if (searchInfo.getLocation() == null) {
	    return CorrectionCheckResult.getSearchInfoError(ERROR37);
	}
	return null;
    }

    private SearchInfoResult checkIntervalsError(SearchInfo searchInfo) {
	List<SignificantDateInterval> intervals = searchInfo.getDateIntervals();
	fillIntervals(intervals);
	for (SignificantDateInterval interval : intervals) {
	    try {
		LocalDateTime.parse(interval.getDateFrom().toString());
		LocalDateTime.parse(interval.getDateTo().toString());
	    } catch (DateTimeParseException e) {
		return CorrectionCheckResult.getSearchInfoError(ERROR19);
	    }

	    if (interval.getDateFrom().isAfter(LocalDateTime.now())) {
		return CorrectionCheckResult.getSearchInfoError(ERROR18);
	    }

	    if (interval.getDateFrom().isAfter(interval.getDateTo())) {
		return CorrectionCheckResult.getSearchInfoError(ERROR1);
	    }
	}

	return null;
    }

    private void fillIntervals(List<SignificantDateInterval> intervals) {
	if (intervals.isEmpty()) {
	    SignificantDateInterval significantDateInterval = new SignificantDateInterval();
	    significantDateInterval.setDateFrom(LocalDateTime.MIN);
	    significantDateInterval.setDateTo(LocalDateTime.MAX);
	    intervals.add(significantDateInterval);
	}
	for (SignificantDateInterval interval : intervals) {
	    if (interval.getDateFrom() == null) {
		interval.setDateFrom(LocalDateTime.MIN);
	    }
	    if (interval.getDateTo() == null) {
		interval.setDateTo(LocalDateTime.MAX);
	    }
	}
    }
}
