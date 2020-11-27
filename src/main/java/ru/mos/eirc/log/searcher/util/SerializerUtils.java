package ru.mos.eirc.log.searcher.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class SerializerUtils {

    public static <T> T unmarshal(String xml, Class<T> clazz) throws JAXBException {
	StreamSource streamSource = new StreamSource(new StringReader(xml));
	JAXBContext context = JAXBContext.newInstance(clazz);
	Unmarshaller unmarshaller = context.createUnmarshaller();
	return unmarshaller.unmarshal(streamSource, clazz).getValue();
    }

    public static <T> String marshal(Class<T> clazz) throws JAXBException {
	StringWriter sw = new StringWriter();
	JAXBContext context = JAXBContext.newInstance(clazz);
	Marshaller marshaller = context.createMarshaller();
	marshaller.marshal(clazz, sw);
	return sw.toString();
    }

    public static <T> void marshal(Class<T> clazz, File file) throws JAXBException {
	JAXBContext context = JAXBContext.newInstance(clazz);
	Marshaller marshaller = context.createMarshaller();
	marshaller.marshal(clazz, file);
    }

    public static <T> void testMarshal(Class<T> clazz, File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	marshaller.marshal(clazz, file);
    }

    public static <T> void testMarshal(Class<T> clazz, Result streamResult) throws JAXBException {
	JAXBContext context = JAXBContext.newInstance(clazz);
	Marshaller marshaller = context.createMarshaller();
	marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	marshaller.marshal(clazz, streamResult);
    }

    public static <T> void testMarshal(Class<T> clazz, ByteArrayOutputStream outputStream) throws JAXBException {
	JAXBContext context = JAXBContext.newInstance(clazz);
	Marshaller marshaller = context.createMarshaller();
	marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	marshaller.marshal(clazz, outputStream);
    }
}
