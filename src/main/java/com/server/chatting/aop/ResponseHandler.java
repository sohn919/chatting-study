package com.server.chatting.aop;

import com.server.chatting.response.ApiUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResponseHandler {
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMapping() {
    }

//    @Around("postMapping() || putMapping() || getMapping()")
    public ResponseEntity<?> validationAdvice(ProceedingJoinPoint jp) throws Throwable {
        ResponseEntity<?> originalReturnValue = (ResponseEntity<?>) jp.proceed();

        Object body = originalReturnValue.getBody();
        HttpStatus statusCode = originalReturnValue.getStatusCode();

        return ResponseEntity.status(statusCode).body(ApiUtils.success(body));
    }
}
