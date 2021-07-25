package ru.bmstu.log.searcher.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
	MessageDispatcherServlet servlet = new MessageDispatcherServlet();
	servlet.setApplicationContext(applicationContext);
	servlet.setTransformWsdlLocations(true);
	return new ServletRegistrationBean<>(servlet, "/ws/LogSearcher.wsdl", "*.xsd");
    }

    @Bean(name = "LogSearcher")
    public Wsdl11Definition wsdl11Definition() {
	return new SimpleWsdl11Definition(new ClassPathResource("wsdl/LogSearcher.wsdl"));
    }

    @Bean(name = "LogSearcherSchema")
    public XsdSchema logSearcherSchema() {
	return new SimpleXsdSchema(new ClassPathResource("xsd/LogSearcherSchema.xsd"));
    }

}
