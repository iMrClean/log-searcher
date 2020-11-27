//package ru.mos.eirc.log.searcher.sheduler;
//
//import lombok.extern.log4j.Log4j2;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Arrays;
//import java.util.stream.Stream;
//
//@Log4j2
//@Component
//public class FileRemover {
//
//    @Scheduled(fixedRateString = "#{${logs.deletionInterval} * 24 * 60 * 60 * 1000}")
//    private void removeFiles() {
//
//	try {
//	    String filesDir = configProperties.getPath();
//	    long fileExistTime = configProperties.getFileExistTime() * 24 * 60 * 60 * 1000L;
//
//	    if(!Paths.get(filesDir).toFile().exists()) {
//		Files.createDirectory(Paths.get(filesDir));
//	    }
//	    Stream<Path> stream = Files.list(Paths.get(filesDir));
//	    stream.map(Path::toFile)
//			    .filter(file -> (System.currentTimeMillis() - file.lastModified()) > fileExistTime)
//			    .forEach(File::deleteOnExit);
//	    stream.close();
//	} catch (IOException e) {
//	    log.error("Exception raised: {}", Arrays.toString(e.getStackTrace()));
//	}
//    }
//}
