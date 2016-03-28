package com.peripateticgames.vertx.example.services;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * Created by david.
 */

@ProxyGen
public interface ExampleServiceInterface {

    static final public String ADDRESS = "example.service";
    static final String MESSAGE = "Hello world with injection!";
    void sayHello(Handler<AsyncResult<String>> result);
}
