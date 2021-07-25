package ru.bmstu.log.searcher.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Log4j2
@Aspect
@Component
public class LoggingAspect {

    /**
     * Pointcut, который соответствует конечным точкам.
     */
    @Pointcut("within(@org.springframework.stereotype.Controller *)"
		    + " || within(@org.springframework.web.bind.annotation.RestController *)"
		    + " || within(@org.springframework.ws.server.endpoint.annotation.Endpoint *)")
    public void controllerPointcut() {
	// Метод пуст, так как это всего лишь Pointcut, реализация находится в Around.
    }

        /**
         * Advice который регистрирует методы, генерирующие исключения.
         *
         * @param joinPoint точка соединения для advice.
         * @param e         исключение.
         */
        @AfterThrowing(pointcut = "controllerPointcut()", throwing = "e")
        public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
    	log.error("Exception in {}() with cause = '{}' and exception = '{}'", joinPoint.getSignature().getName(),
    			e.getCause() != null ? e.getCause() : "NULL", e.getMessage(), e);
        }

    /**
     * Advice который регистрирует вход и выход из метода.
     *
     * @param joinPoint точка соединения для advice.
     * @return результат.
     * @throws Throwable throws {@link IllegalArgumentException}.
     */
    @Around("controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
	log.info("Start method : {}", joinPoint.getSignature().getName());
	long start = System.currentTimeMillis();
	try {
	    Object result = joinPoint.proceed();
	    log.info("End method : {} in {} ms", joinPoint.getSignature().getName(), (System.currentTimeMillis() - start));
	    return result;
	} catch (IllegalArgumentException e) {
	    log.error("Illegal argument: {} ", Arrays.toString(joinPoint.getArgs()));
	    throw e;
	}
    }

}
