package com.peripateticgames.vertx.annotation.parser.mockup;

import com.peripateticgames.vertx.annotation.Inject;
import com.peripateticgames.vertx.annotation.parser.mockup.impl.ExampleServiceInterfaceImpl;
import io.vertx.core.AbstractVerticle;

/**
 * Created by david.
 */
public class ExampleVerticleWithSeveralInjects extends AbstractVerticle {

    @Inject(address = ExampleServiceInterfaceImpl.SERVICE)
    public ExampleServiceInterface firstProxy;

    @Inject(address = ExampleServiceInterfaceImpl.SERVICE)
    public ExampleServiceInterface secondProxy;
}
