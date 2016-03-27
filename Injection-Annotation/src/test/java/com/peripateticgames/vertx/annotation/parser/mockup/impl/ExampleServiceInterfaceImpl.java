package com.peripateticgames.vertx.annotation.parser.mockup.impl;

import com.peripateticgames.vertx.annotation.parser.mockup.ExampleServiceInterface;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * Created by david.
 */

public class ExampleServiceInterfaceImpl  implements ExampleServiceInterface{

    public static final String SERVICE = "example.service";

    @Override
    public void sayHello(Handler<AsyncResult<String>> result) {
        result.handle(Future.succeededFuture(ExampleServiceInterface.MESSAGE));
    }
}
