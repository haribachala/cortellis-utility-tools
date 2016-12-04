package com.clarivate.cortellis.alerts.exceptions;

/**
 * Created by U6015446 on 21-Nov-16.
 */
public class MessageProducerException extends  Exception{

    public MessageProducerException(Throwable t) {
        super(t);
    }

    public MessageProducerException(String message) {
        super(message);
    }
}
