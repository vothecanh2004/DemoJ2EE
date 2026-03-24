package com.damh.qlnt.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalDebugExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleAllExceptions(Exception ex) {
        StringBuilder sb = new StringBuilder();
        sb.append("DEBUG EXCEPTION CAUGHT: ").append(ex.getClass().getName()).append("\n");
        sb.append("Message: ").append(ex.getMessage()).append("\n");
        for (StackTraceElement ste : ex.getStackTrace()) {
            sb.append(ste.toString()).append("\n");
        }
        return sb.toString();
    }
}
