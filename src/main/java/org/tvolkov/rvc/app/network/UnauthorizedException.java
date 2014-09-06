package org.tvolkov.rvc.app.network;

public class UnauthorizedException extends RuntimeException {
     public UnauthorizedException(Exception e) {
         super(e);
    }
}
