package com.peripateticgames.vertx.annotation.parser.mockup;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * Created by david.
 */

@ProxyGen
public interface ExampleServiceInterface {
    static final String MESSAGE = "Hello world!";
    void sayHello(Handler<AsyncResult<String>> result);
}
