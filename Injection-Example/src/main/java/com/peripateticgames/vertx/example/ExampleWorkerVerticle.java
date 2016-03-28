package com.peripateticgames.vertx.example;

import com.peripateticgames.vertx.example.services.ExampleServiceInterface;
import com.peripateticgames.vertx.example.services.impl.ExampleServiceInterfaceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * Created by david.
 */
public class ExampleWorkerVerticle extends AbstractVerticle {

    private ExampleServiceInterface service;
    @Override
    public void start() {
        service = new ExampleServiceInterfaceImpl();
        ProxyHelper.registerService(ExampleServiceInterface.class, getVertx(), service,  ExampleServiceInterface.ADDRESS);
    }
}
