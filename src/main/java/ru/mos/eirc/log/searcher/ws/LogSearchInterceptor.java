package ru.mos.eirc.log.searcher.ws;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

@Log4j2
@Component
public class LogSearchInterceptor implements EndpointInterceptor {

    @Override
    public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
	log.info("Request Handling");
	return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
	log.info("Response Handling");
	return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
	log.info("Exception Handling");
	return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
	log.info("Execute Code After Completion");
    }
}