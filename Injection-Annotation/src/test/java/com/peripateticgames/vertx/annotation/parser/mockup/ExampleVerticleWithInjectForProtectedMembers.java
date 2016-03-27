package com.peripateticgames.vertx.annotation.parser.mockup;

import com.peripateticgames.vertx.annotation.Inject;
import com.peripateticgames.vertx.annotation.parser.mockup.impl.ExampleServiceInterfaceImpl;
import io.vertx.core.AbstractVerticle;

/**
 * Created by david.
 */
public class ExampleVerticleWithInjectForProtectedMembers extends AbstractVerticle {
    @Inject(address = ExampleServiceInterfaceImpl.SERVICE)
    protected ExampleServiceInterface protectedProxy;

    public ExampleServiceInterface getProtectedProxy() {
        return protectedProxy;
    }
}
