package com.work.daily.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ExceptionController implements ErrorController {

    private final String ERROR_PATH_FORBIDDEN = "contents/error/403";
    private final String ERROR_PATH_NOT_FOUND = "contents/error/404";
    private final String ERROR_PATH_ETC = "contents/error/etc";

    @GetMapping("/error")
    public String exceptionHandler(HttpServletRequest request){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = Integer.parseInt(status.toString());

        if(status != null){

            if(statusCode == HttpStatus.FORBIDDEN.value()){
                return ERROR_PATH_FORBIDDEN;

            }else if(statusCode == HttpStatus.NOT_FOUND.value()){
                return ERROR_PATH_NOT_FOUND;

            }else{
                return ERROR_PATH_ETC;
            }
        }

        return ERROR_PATH_ETC;
    }

}
