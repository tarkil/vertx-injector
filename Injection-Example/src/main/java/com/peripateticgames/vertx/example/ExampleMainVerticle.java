package com.peripateticgames.vertx.example;

import com.peripateticgames.vertx.annotation.Inject;
import com.peripateticgames.vertx.example.services.ExampleServiceInterface;
import com.peripateticgames.vertx.example.services.impl.ExampleServiceInterfaceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * Created by david.
 */
public class ExampleMainVerticle extends AbstractVerticle {

    static private Logger logger = LoggerFactory.getLogger(App.class);

    @Inject(address = ExampleServiceInterface.ADDRESS)
    ExampleServiceInterface service;


    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        router.route().handler(routingContext -> {

            service.sayHello(handle -> {
                // This handler will be called for every request
                HttpServerResponse response = routingContext.response();
                response.putHeader("content-type", "text/plain");
                if (handle.succeeded()) {


                    // Write to the response and end it
                    response.end(handle.result());
                } else {
                    response.end("Error calling the service");
                }
            });

        });

        server.requestHandler(router::accept).listen(8080);

        logger.info("Web server started at http://127.0.0.1:8080");

    }
}
