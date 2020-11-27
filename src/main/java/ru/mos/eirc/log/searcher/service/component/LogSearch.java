package ru.mos.eirc.log.searcher.service.component;

import com.example.logsearch.entities.ResultLogs;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.entities.SignificantDateInterval;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
@Component
public class LogSearch {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[dd.MM.yyyy HH:mm:ss][dd.MM.yyyy H:mm:ss ]");
    private static final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.log*");
    private static final Pattern accessLogPattern = Pattern.compile("access.log*");

    public SearchInfoResult logSearch(SearchInfo searchInfo) {
	log.info("Starting logs search");
	long start = System.currentTimeMillis();
	SearchInfoResult searchInfoResult = new SearchInfoResult();

	FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
		Path name = file.getFileName();

		if (accessLogPattern.matcher(name.toString()).find()) {
		    return FileVisitResult.CONTINUE;
		}

		if (matcher.matches(name)) {
		    List<String> fileLines = getFileLines(file);
		    List<ResultLogs> resultLogsArray = getResultLogsList(file, fileLines, searchInfo);
		    if (!resultLogsArray.isEmpty()) {
			searchInfoResult.getResultLogs().addAll(resultLogsArray);
		    }
		}
		return FileVisitResult.CONTINUE;
	    }
	};

	try {
	    Path locationPath = Paths.get(searchInfo.getLocation());
	    Files.walkFileTree(locationPath, matcherVisitor);
	} catch (IOException e) {
	    log.error("Exception raised: {}" , Arrays.toString(e.getStackTrace()));
	}
	if (searchInfoResult.getResultLogs().isEmpty()) {
	    searchInfoResult.setEmptyResultMessage("No logs found");
	} else {
	    searchInfoResult.getResultLogs().sort(Comparator.comparing(ResultLogs::getTimeMoment));
	}

	log.info("Logs search finished in {} ms" , (System.currentTimeMillis() - start));
	return searchInfoResult;
    }

    private List<String> getFileLines(Path file) throws IOException {
	StringBuilder sb = new StringBuilder();
	List<String> fileLines = new ArrayList<>();
	for (String line : Files.readAllLines(file, StandardCharsets.ISO_8859_1)) {
	    if (!line.trim().endsWith(">")) {
		if (sb.length() == 0) {
		    sb.append(line);
		} else {
		    sb.append(", ").append(line);
		}
	    } else {
		sb.append(line.trim());
		fileLines.add(sb.toString());
		sb = new StringBuilder();
	    }
	}
	return fileLines;
    }

    private List<ResultLogs> getResultLogsList(Path file, List<String> fileLines, SearchInfo searchInfo) {
	return fileLines.stream().parallel().filter(line -> line.startsWith("####"))
			.filter(Pattern.compile(searchInfo.getRegularExpression(), Pattern.CASE_INSENSITIVE).asPredicate()).map(line -> {
			    String parsedLine = line.substring(5, 24);
			    if (parsedLine.endsWith(",")) {
				parsedLine = line.substring(5, 23);
			    }
			    LocalDateTime parsedDate = LocalDateTime.parse(parsedLine, formatter);
			    ResultLogs resultLogs = new ResultLogs();

			    for (SignificantDateInterval interval : searchInfo.getDateIntervals()) {
				if ((parsedDate.isAfter(interval.getDateFrom()) || parsedDate.isEqual(interval.getDateFrom())) && parsedDate
						.isBefore(interval.getDateTo())) {

				    String content = line.substring(29);

				    resultLogs.setTimeMoment(parsedDate);
				    resultLogs.setContent(content);
				    resultLogs.setFileName(file.toString());
				}
			    }
			    return resultLogs;
			}).filter(log -> log.getFileName() != null).collect(Collectors.toList());
    }
}
