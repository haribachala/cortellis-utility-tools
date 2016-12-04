package com.clarivate.cortellis.record.services.exceptions;

/**
 * Created by U6015446 on 30-Nov-16.
 */
public class FileNotFoundException extends  Exception {

    public  FileNotFoundException(String message){
                super(message);
    }
    public  FileNotFoundException(Throwable t){
        super(t);
    }


    public String FileNotFoundException(String message, int statusCode){
         return   exceptionWithStatusCode(message,statusCode);
    }

    public String exceptionWithStatusCode(String message, int code){

           return  "exception: " +message + " status: " + code;

    }
}
