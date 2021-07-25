package ru.bmstu.log.searcher.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import ru.bmstu.log.searcher.util.ApplicationProperties;
import ru.bmstu.log.searcher.util.AppUtils;
import ru.bmstu.logsearch.*;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.*;
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
@Service
public class LogSearchServiceImpl implements LogSearchService {

    private static final PathMatcher MATCHER = FileSystems.getDefault().getPathMatcher("glob:*.log*");
    private static final Pattern ACCESS_LOG_PATTERN = Pattern.compile("access.log*");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("[dd.MM.yyyy HH:mm:ss][dd.MM.yyyy H:mm:ss ]");
    private static final int LINE_SUBSTRING_START = 5;
    private static final int LINE_SUBSTRING_END = 24;
    private static final int LINE_SUBSTRING_CONTENT = 29;

    private final ResourceLoader resourceLoader;

    @Autowired
    public LogSearchServiceImpl(ResourceLoader resourceLoader) {
	this.resourceLoader = resourceLoader;
    }

    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
	SearchInfoResult searchInfoResult = new SearchInfoResult();

	FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
		Path name = file.getFileName();

		if (ACCESS_LOG_PATTERN.matcher(name.toString()).find()) {
		    log.info("Skipped '{}', path {}", file.getFileName(), file.getParent());
		    return FileVisitResult.CONTINUE;
		}

		if (MATCHER.matches(name)) {
		    log.info("Matches '{}', path '{}'", file.getFileName(), file.getParent());
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
	    log.error("Exception raised: {}", Arrays.toString(e.getStackTrace()));
	}

	log.info("SearchInfoResult resultLogs size : {}", searchInfoResult.getResultLogs().size());

	if (searchInfoResult.getResultLogs().isEmpty())
	    searchInfoResult.setEmptyResultMessage("Not found logs");
	else
	    searchInfoResult.getResultLogs().sort(Comparator.comparing(ResultLogs::getTimeMoment));

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
	log.info("ParsedLines '{}', size :{}", file.getFileName(), fileLines.size());
	return fileLines;
    }

    private List<ResultLogs> getResultLogsList(Path file, List<String> fileLines, SearchInfo searchInfo) {
	List<ResultLogs> result = fileLines.stream().parallel().filter(line -> line.startsWith("####"))
			.filter(Pattern.compile("<" + searchInfo.getRegularExpression() + ">", Pattern.CASE_INSENSITIVE).asPredicate())
			.map(line -> {
			    String parsedLine = line.substring(LINE_SUBSTRING_START, LINE_SUBSTRING_END);
			    LocalDateTime parsedDate = LocalDateTime.parse(parsedLine, FORMATTER);
			    ResultLogs resultLogs = new ResultLogs();

			    for (SignificantDateInterval interval : searchInfo.getDateIntervals()) {
				if (parsedDate.isAfter(interval.getDateFrom()) && parsedDate.isBefore(interval.getDateTo())) {
				    String content = line.substring(LINE_SUBSTRING_CONTENT);

				    resultLogs.setTimeMoment(parsedDate);
				    resultLogs.setContent(content);
				    resultLogs.setFileName(file.getFileName().toString());
				}
			    }
			    return resultLogs;
			}).filter(log -> log.getContent() != null && log.getFileName() != null && log.getTimeMoment() != null)
			.collect(Collectors.toList());
	log.info("ResultLogsList '{}', size : {}", file.getFileName(), result.size());
	return result;
    }

    private static final String EXCEPTION = "Exception raised: {}";

    @Override
    public void fileGenerate(SearchInfo searchInfo, SearchInfoResult searchInfoResult) {
	try {
	    Logs logs = new Logs();
	    logs.setSearchInfo(searchInfo);
	    logs.setSearchInfoResult(searchInfoResult);
	    File file = generateUniqueFile(searchInfo.getFileExtension());
	    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
		AppUtils.marshal(logs, out);
		try (InputStream in = new ByteArrayInputStream(out.toByteArray())) {
		    String resourcePath = String.format("classpath:xsl/%s.xslt", searchInfo.getFileExtension().toLowerCase());
		    Resource resource = resourceLoader.getResource(resourcePath);
		    AppUtils.transform(in, resource, file);
		} catch (TransformerException | IOException e) {
		    log.error(e, e);
		}
	    } catch (JAXBException e) {
		log.error(e, e);
	    }
	} catch (Exception e) {
	    log.error(e, e);
	}
    }
    //
    //    private void savePdfFile(File file,
    //		    FileExtension fileExtension,
    //		    Source streamSource,
    //		    TransformerFactory transformerFactory) {
    //	try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
    //	    Resource resource = resourceLoader.getResource("classpath:xsl/pdf.xslt");
    //	    Source xslt = new StreamSource(resource.getFile());
    //
    //	    resource = resourceLoader.getResource("classpath:fop.xml");
    //	    FopFactory fopFactory = FopFactory.newInstance(resource.getFile());
    //	    Fop fop = fileExtension.value().equalsIgnoreCase("rtf") ?
    //			    fopFactory.newFop(MimeConstants.MIME_RTF, out) :
    //			    fopFactory.newFop(MimeConstants.MIME_PDF, out);
    //	    Result streamResult = new SAXResult(fop.getDefaultHandler());
    //
    //	    transformerFactory.newTransformer(xslt).transform(streamSource, streamResult);
    //	} catch (Exception e) {
    //	    log.error(EXCEPTION, Arrays.toString(e.getStackTrace()));
    //	}
    //    }

    private File generateUniqueFile(String fileExtension) {
	File dir = ApplicationProperties.getAbsolutePath().toFile();
	String uniqueName = "result_log";
	String extension = "." + fileExtension.toLowerCase();

	File file = new File(dir, uniqueName + extension);
	log.info("Create file '{}'",file.toPath());
	int num = 0;
	while (file.exists()) {
	    num++;
	    file = new File(dir, uniqueName + "_" + num + extension);
	}

	return file;
    }
}
