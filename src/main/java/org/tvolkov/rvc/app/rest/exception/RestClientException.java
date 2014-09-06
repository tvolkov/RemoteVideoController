package org.tvolkov.rvc.app.rest.exception;

public class RestClientException extends RuntimeException {

    public RestClientException(String message) {
        super(message);
    }

    public RestClientException(Exception e){
        super(e);
    }
}
