package com.peripateticgames.vertx.annotation.parser;

import com.peripateticgames.vertx.annotation.Inject;
import com.peripateticgames.vertx.annotation.parser.excpetion.VertxInjectionException;
import io.vertx.core.*;
import io.vertx.serviceproxy.ProxyHelper;

import java.lang.reflect.Field;

/**
 * Created by david.
 */
public class Injector {


    public static <T extends Verticle> void injectProxies(T verticle) {
        for (Field field : verticle.getClass().getDeclaredFields()) {
            Inject[] annotationsByType = field.getAnnotationsByType(Inject.class);
            if (annotationsByType.length == 1) {
                Inject injectAnnotation = annotationsByType[0];
                final String proxyAddress = injectAnnotation.address();
                try {
                    Object proxy = ProxyHelper.createProxy(field.getType(), verticle.getVertx(), proxyAddress);
                    boolean fieldAccessibility = field.isAccessible();
                    field.setAccessible(true);
                    field.set(verticle, proxy);
                    field.setAccessible(fieldAccessibility);
                } catch (IllegalAccessException | IllegalStateException | SecurityException e) {
                    throw new VertxInjectionException(String.format("Error setting the value of property %s of type %s in class %s",
                            field.getName(), field.getType().getName(), verticle.getClass().getName()), e.getCause());
                }

            }
        }
    }
}
