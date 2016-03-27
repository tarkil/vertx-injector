package com.peripateticgames.vertx;

import io.vertx.core.AbstractVerticle;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

/**
 * Created by david.
 */
public class InjectorAgent {
    public static void premain(String arg, Instrumentation inst) {
        new AgentBuilder.Default().type(ElementMatchers.isSubTypeOf(AbstractVerticle.class))
            .transform((builder, type, contextClassLoader) ->
                builder.method(ElementMatchers.named("start"))
                        .intercept(MethodDelegation.to(InjectorInterceptor.class)
                                .andThen(SuperMethodCall.INSTANCE))

            )
            .installOn(inst);
    }
}
