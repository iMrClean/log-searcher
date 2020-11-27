package ru.mos.eirc.log.searcher.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import ru.mos.eirc.log.searcher.ws.LogSearchInterceptor;

import java.util.List;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    private final LogSearchInterceptor logSearchInterceptor;

    @Autowired
    public WebServiceConfig(LogSearchInterceptor logSearchInterceptor) {
	this.logSearchInterceptor = logSearchInterceptor;
    }

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
	MessageDispatcherServlet servlet = new MessageDispatcherServlet();
	servlet.setApplicationContext(applicationContext);
	servlet.setTransformWsdlLocations(true);
	return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "LogSearcher")
    public Wsdl11Definition wsdl11Definition() {
	return new SimpleWsdl11Definition(new ClassPathResource("wsdl/LogSearcher.wsdl"));
    }

    @Bean(name = "LogSearcherSchema")
    public XsdSchema logSearcherSchema() {
	return new SimpleXsdSchema(new ClassPathResource("wsdl/LogSearcherSchema.xsd"));
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
	PayloadValidatingInterceptor validatingInterceptor = new PayloadValidatingInterceptor();
	validatingInterceptor.setValidateRequest(true);
	validatingInterceptor.setValidateResponse(true);
	validatingInterceptor.setXsdSchema(logSearcherSchema());
	interceptors.add(validatingInterceptor);
	interceptors.add(logSearchInterceptor);
    }
}