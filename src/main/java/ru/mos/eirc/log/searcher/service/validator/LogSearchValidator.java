package ru.mos.eirc.log.searcher.service.validator;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.entities.SignificantDateInterval;
import org.springframework.stereotype.Component;
import ru.mos.eirc.log.searcher.util.ApplicationProperties;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static ru.mos.eirc.log.searcher.enums.CorrectionCheckResult.*;

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
	    SearchInfoResult searchInfoResult = new SearchInfoResult();
	    searchInfoResult.setErrorCode(ERROR3701.getErrorCode());
	    searchInfoResult.setErrorMessage(ERROR3701.getErrorMessage());
	    return searchInfoResult;
	}
	return null;
    }

    private SearchInfoResult check44Error(SearchInfo searchInfo) {
	Path domainPath = ApplicationProperties.getDomainPath();
	if (searchInfo.getLocation() == null) {
	    searchInfo.setLocation(String.valueOf(domainPath));
	} else {
	    Path locationPath = Paths.get(String.valueOf(domainPath), searchInfo.getLocation());
	    if (!locationPath.toFile().exists()) {
		SearchInfoResult searchInfoResult = new SearchInfoResult();
		searchInfoResult.setErrorCode(ERROR44.getErrorCode());
		searchInfoResult.setErrorMessage(ERROR44.getErrorMessage());
		return searchInfoResult;
	    }
	    searchInfo.setLocation(String.valueOf(locationPath));
	}

	return null;
    }

    private SearchInfoResult check37Error(SearchInfo searchInfo) {
	if (searchInfo.getRegularExpression() == null || searchInfo.getDateIntervals() == null) {
	    SearchInfoResult searchInfoResult = new SearchInfoResult();
	    searchInfoResult.setErrorCode(ERROR37.getErrorCode());
	    searchInfoResult.setErrorMessage(ERROR37.getErrorMessage());
	    return searchInfoResult;
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
		SearchInfoResult searchInfoResult = new SearchInfoResult();
		searchInfoResult.setErrorCode(ERROR19.getErrorCode());
		searchInfoResult.setErrorMessage(ERROR19.getErrorMessage());
		return searchInfoResult;
	    }

	    if (interval.getDateFrom().isAfter(LocalDateTime.now())) {
		SearchInfoResult searchInfoResult = new SearchInfoResult();
		searchInfoResult.setErrorCode(ERROR18.getErrorCode());
		searchInfoResult.setErrorMessage(ERROR18.getErrorMessage());
		return searchInfoResult;
	    }

	    if (interval.getDateFrom().isAfter(interval.getDateTo())) {
		SearchInfoResult searchInfoResult = new SearchInfoResult();
		searchInfoResult.setErrorCode(ERROR1.getErrorCode());
		searchInfoResult.setErrorMessage(ERROR1.getErrorMessage());
		return searchInfoResult;
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
