package com.peripateticgames.vertx.annotation.parser.mockup;

import com.peripateticgames.vertx.annotation.Inject;
import com.peripateticgames.vertx.annotation.parser.mockup.impl.ExampleServiceInterfaceImpl;
import io.vertx.core.AbstractVerticle;

/**
 * Created by david.
 */
public class ExampleVerticleWithInjectForPackageMembers extends AbstractVerticle {

    @Inject(address = ExampleServiceInterfaceImpl.SERVICE)
    ExampleServiceInterface proxy;

    public ExampleServiceInterface getPackageProxy() {
        return proxy;
    }
}
