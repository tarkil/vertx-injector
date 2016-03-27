package com.peripateticgames.vertx.annotation.parser.mockup;

import com.peripateticgames.vertx.annotation.Inject;
import com.peripateticgames.vertx.annotation.parser.mockup.impl.ExampleServiceInterfaceImpl;
import io.vertx.core.AbstractVerticle;

/**
 * Created by david.
 */
public class ExampleVerticleWithInjectForPrivateMembers extends AbstractVerticle {

    @Inject(address = ExampleServiceInterfaceImpl.SERVICE)
    private ExampleServiceInterface proxy;

    public ExampleServiceInterface getPrivateProxy() {
        return proxy;
    }
}
