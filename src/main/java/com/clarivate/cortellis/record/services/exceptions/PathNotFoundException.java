package com.clarivate.cortellis.record.services.exceptions;

/**
 * Created by U6015446 on 03-Dec-16.
 */
public class PathNotFoundException extends Exception {

    public  PathNotFoundException(String message){
           super(message);
    }
    public  PathNotFoundException(Throwable t){
        super(t);
    }


    public String PathNotFoundException(String message, int statusCode){
        return   exceptionWithStatusCode(message,statusCode);
    }

    public String exceptionWithStatusCode(String message, int code){

        return  "exception: " +message + " status: " + code;

    }
}
