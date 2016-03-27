package com.peripateticgames.vertx.annotation.parser;

import com.peripateticgames.vertx.annotation.Inject;
import com.peripateticgames.vertx.annotation.parser.excpetion.VertxInjectionException;
import com.peripateticgames.vertx.annotation.parser.mockup.*;
import com.peripateticgames.vertx.annotation.parser.mockup.impl.ExampleServiceInterfaceImpl;
import io.vertx.core.*;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.Repeat;
import io.vertx.ext.unit.junit.RepeatRule;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.serviceproxy.ProxyHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.ReflectPermission;
import java.security.Permission;
import java.util.concurrent.Semaphore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by david.
 */

@RunWith(VertxUnitRunner.class)
public class InjectorTest {

    private static final String INVALID_ADDRESS = "invalid.address";
    private Vertx vertx;
    private SecurityManager securityManager;

    @Rule
    public RepeatRule rule = new RepeatRule();


    @Before
    public void setUp(TestContext context) throws Exception {
        vertx = Vertx.vertx();
        AbstractVerticle verticleMockup = new AbstractVerticle() {

            @Override
            public void start() throws Exception {
                super.start();
                ProxyHelper.registerService(ExampleServiceInterface.class, getVertx(), new ExampleServiceInterfaceImpl(), ExampleServiceInterfaceImpl.SERVICE);
            }
        };

        vertx.deployVerticle(verticleMockup, new DeploymentOptions(), context.asyncAssertSuccess());

        securityManager = System.getSecurityManager();
    }

    @After
    public void tearDown(TestContext context) throws Exception {
        vertx.close(context.asyncAssertSuccess());
        System.setSecurityManager(securityManager);
    }

    @Test
    @Repeat(100)
    public void testinjectProxiesWithPublicMembers(TestContext context) {
        final Async async = context.async();
        ExampleVerticleWithInjectForPublicMembers verticle = new ExampleVerticleWithInjectForPublicMembers();
        vertx.deployVerticle(verticle, new DeploymentOptions(),
                checkInjectionForProperty(context, verticle,
                        () -> verticle.proxy.sayHello(handler -> checkAnswer(async, handler))));
    }


    @Test
    @Repeat(100)
    public void testinjectProxiesWithProtectedMembers(TestContext context) {
        final Async async = context.async();
        ExampleVerticleWithInjectForProtectedMembers verticle = new ExampleVerticleWithInjectForProtectedMembers();
        vertx.deployVerticle(verticle, new DeploymentOptions(),
                checkInjectionForProperty(context, verticle,
                        () -> verticle.getProtectedProxy().sayHello(handler -> checkAnswer(async, handler))));
    }

    @Test
    @Repeat(100)
    public void testinjectProxiesWithPrivateMembers(TestContext context) {
        final Async async = context.async();
        ExampleVerticleWithInjectForPrivateMembers verticle = new ExampleVerticleWithInjectForPrivateMembers();
        vertx.deployVerticle(verticle, new DeploymentOptions(),
                checkInjectionForProperty(context, verticle,
                        () -> verticle.getPrivateProxy().sayHello(handler -> checkAnswer(async, handler))));

    }


    @Test
    @Repeat(100)
    public void testinjectProxiesWithPackageMembers(TestContext context) {
        final Async async = context.async();
        ExampleVerticleWithInjectForPackageMembers verticle = new ExampleVerticleWithInjectForPackageMembers();
        vertx.deployVerticle(verticle, new DeploymentOptions(), checkInjectionForProperty(context, verticle,
                () -> verticle.getPackageProxy().sayHello(handler -> checkAnswer(async, handler))));
    }

    @Test(expected = VertxInjectionException.class)
    @Repeat(100)
    public void testinjectProxiesExceptionProducedByInvalidClass(TestContext context) throws InterruptedException {

        final Async async = context.async();

        Verticle verticle = new AbstractVerticle() {

            @Inject(address = INVALID_ADDRESS)
            Verticle service;

            @Override
            public void start() throws Exception {
                super.start();
            }
        };

        Semaphore semaphore = new Semaphore(0);
        vertx.deployVerticle(verticle, new DeploymentOptions(), handle -> {
            if (handle.succeeded()) {
                semaphore.release();
            } else {
                context.fail("Verticle was not deployed");
            }
        });

        semaphore.acquire();
        try {
            Injector.injectProxies(verticle);
        } finally {
            async.complete();
        }
    }

    @Test(expected = VertxInjectionException.class)
    @Repeat(100)
    public void testinjectProxiesExceptionProducedBySecurityPolice(TestContext context) throws InterruptedException {

        final Async async = context.async();

        System.setSecurityManager(new SecurityManager() {
            @Override
            public void checkPermission(Permission perm) {
                if (perm instanceof ReflectPermission && "suppressAccessChecks".equals(perm.getName())) {
                    for (StackTraceElement elem : Thread.currentThread().getStackTrace()) {
                        if (AccessibleObject.class.getName().equals(elem.getClassName()) && "setAccessible".equals(elem.getMethodName())) {
                            throw new SecurityException();
                        }
                    }
                }
            }
        });

        Verticle verticle = new AbstractVerticle() {

            @Inject(address = INVALID_ADDRESS)
            ExampleServiceInterface service;

            @Override
            public void start() throws Exception {
                super.start();
            }
        };
        Semaphore semaphore = new Semaphore(0);

        vertx.deployVerticle(verticle, new DeploymentOptions(),  handle -> {
            if (handle.succeeded()) {
                semaphore.release();
            } else {
                context.fail("Verticle was not deployed");
            }
        });
        semaphore.acquire();
        try {
            Injector.injectProxies(verticle);
        } finally {
            async.complete();
        }
    }

    @Test
    @Repeat(100)
    public void testinjectProxiesWithSeveralInjections(TestContext context) throws InterruptedException {
        final Async async = context.async();

        ExampleVerticleWithSeveralInjects verticle = new ExampleVerticleWithSeveralInjects();
        Semaphore semaphore = new Semaphore(0);
        vertx.deployVerticle(verticle, new DeploymentOptions(), handle -> {
            if (handle.succeeded()) {
                semaphore.release();
            } else {
                context.fail("Verticle was not deployed");
            }
        });

        semaphore.acquire();

        Injector.injectProxies(verticle);

        verticle.firstProxy.sayHello((handler) -> {
                if (handler.succeeded()) {
                    context.assertEquals(handler.result(), ExampleServiceInterface.MESSAGE);
                    verticle.secondProxy.sayHello(secondHandler -> {
                        if (secondHandler.succeeded()) {
                            context.assertEquals(secondHandler.result(), ExampleServiceInterface.MESSAGE);
                        } else {
                            context.fail("Second Handler fail");
                        }
                        async.complete();
                    });
                } else {
                    context.fail("First Handler fail");
                    async.complete();
                }
        });
    }

    private Handler<AsyncResult<String>> checkInjectionForProperty(TestContext context, Verticle verticle,
                                                                   Runnable runnable) {
        return handle -> {
            if (handle.succeeded()) {
                Injector.injectProxies(verticle);
                runnable.run();
            } else {
                context.fail("Failure");
            }
        };
    }

    private void checkAnswer(Async async, AsyncResult<String> handler) {
        try {
            if (handler.succeeded()) {
                assertEquals(handler.result(), ExampleServiceInterface.MESSAGE);
            } else {
                fail();
            }
        } finally {
            async.complete();
        }
    }

}
