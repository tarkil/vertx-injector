package com.peripateticgames.vertx.annotation.parser.excpetion;

/**
 * Created by david.
 */
public class VertxInjectionException extends RuntimeException {
    public VertxInjectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
