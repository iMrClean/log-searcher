package ru.mos.eirc.log.searcher.service.component;

import com.example.logsearch.entities.FileExtension;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.mos.eirc.log.searcher.dto.LogsDTO;
import ru.mos.eirc.log.searcher.util.ApplicationProperties;
import ru.mos.eirc.log.searcher.util.SerializerUtils;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

@Log4j2
@Component
public class FileGenerate {

    private final ResourceLoader resourceLoader;

    @Autowired
    public FileGenerate(ResourceLoader resourceLoader) {
	this.resourceLoader = resourceLoader;
    }

    @Async
    public void fileGenerate(SearchInfo searchInfo, SearchInfoResult searchInfoResult) {
	log.info("Starting file generation");
	long start = System.currentTimeMillis();
	try {
	    LogsDTO logs = new LogsDTO(searchInfo, searchInfoResult);
	    File file = generateUniqueFile(searchInfo.getFileExtension());
	    String fileExtension = searchInfo.getFileExtension();

	    Result streamResult = new StreamResult(file.toString());
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();

	    if (fileExtension.equals(FileExtension.XML.name())) {
		SerializerUtils.testMarshal(logs.getClass(), streamResult);
	    } else {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		Source streamSource = new StreamSource(inputStream);
		String resourcePath = String.format("classpath:xsl/%s.xslt", fileExtension.toLowerCase());
		Resource resource = resourceLoader.getResource(resourcePath);
		Source xslt = new StreamSource(resource.getFile());
		transformerFactory.newTransformer(xslt).transform(streamSource, streamResult);
		SerializerUtils.testMarshal(logs.getClass(), outputStream);
	    }
	    log.info("File generated in {} ms", (System.currentTimeMillis() - start));
	} catch (Exception e) {
	    log.error("Exception was thrown cause: {}", Arrays.toString(e.getStackTrace()));
	}
    }

    private File generateUniqueFile(String fileExtension) {
	File dir = ApplicationProperties.getDomainPath().toFile();
	String uniqueName = "result_log";
	String extension = "." + fileExtension.toLowerCase();

	File file = new File(dir, uniqueName + extension);

	int num = 0;
	while (file.exists()) {
	    num++;
	    file = new File(dir, uniqueName + "_" + num + extension);
	}

	return file;
    }

    //    private void createReport(Path tempFile, String fileExtension, File file) {
    //        try {
    //            JRXmlDataSource dataSource = new JRXmlDataSource(tempFile.toFile());
    //
    //            File styleSheet = new File("src/main/resources/jrxml/report_style.jrxml");
    //            JasperDesign jasperDesign = JRXmlLoader.load(styleSheet);
    //            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
    //            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
    //
    //            switch (fileExtension) {
    //                case "PDF" : {
    //                    JasperExportManager.exportReportToPdfFile(jasperPrint, file.toString());
    //                    break;
    //                }
    //                case "RTF" : {
    //                    JRRtfExporter exporter = new JRRtfExporter();
    //                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    //                    exporter.setExporterOutput(new SimpleWriterExporterOutput(file));
    //                    exporter.exportReport();
    //                    break;
    //                }
    //                case "DOC" : {
    //                    JRDocxExporter exporter = new JRDocxExporter();
    //                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    //                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
    //                    exporter.exportReport();
    //                    break;
    //                }
    //            }
    //
    //        } catch (JRException e) {
    //            e.printStackTrace();
    //        }
    //    }
}
