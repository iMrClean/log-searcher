package ru.bmstu.log.searcher.util;

import org.springframework.core.io.Resource;
import ru.bmstu.logsearch.Logs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AppUtils {

    public static void transform(InputStream in, Resource resource, File file) throws TransformerException, IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Source streamSource = new StreamSource(in);
        Source xslt = new StreamSource(resource.getFile());
        Result streamResult = new StreamResult(file.toString());
        transformerFactory.newTransformer(xslt).transform(streamSource, streamResult);
    }

    public static <T> void marshal(T object, ByteArrayOutputStream out) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(object, out);
    }

}
