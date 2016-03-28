package com.peripateticgames.vertx.example;

import com.hazelcast.config.Config;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * Hello world!
 *
 */
public class App {

    static private  Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        logger.info("Starting Vertx");
        Config hazelcastConfig = new Config();


        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);

        VertxOptions options = new VertxOptions().setClusterManager(mgr);

        Vertx.clusteredVertx(options, ar -> {
            if(ar.failed()) {
                logger.error(ar.cause().getMessage());
            }  else {
                ar.result().deployVerticle(ExampleMainVerticle.class.getName());
                ar.result().deployVerticle(ExampleWorkerVerticle.class.getName(), new DeploymentOptions().setWorker(true));
            }

        });
    }
}
