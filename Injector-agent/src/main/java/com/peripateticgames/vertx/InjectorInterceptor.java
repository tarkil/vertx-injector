package com.peripateticgames.vertx;

import com.peripateticgames.vertx.annotation.parser.Injector;
import io.vertx.core.AbstractVerticle;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * Created by david.
 */
public class InjectorInterceptor {

    static public <T extends AbstractVerticle>  void toStart(@This T object) {
        Injector.injectProxies(object);
    }

}
