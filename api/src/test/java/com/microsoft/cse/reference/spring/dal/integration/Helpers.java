package com.microsoft.cse.reference.spring.dal.integration;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;

import java.io.IOException;

/**
 * Integration test utilities
 */
public class Helpers {
    /**
     * Setup a mongo instance
     * @param net the net instance to bind to
     * @return the mongo instance
     * @throws IOException thrown when unable to bind
     */
    static MongodExecutable SetupMongo(Net net) throws IOException {
        MongodStarter starter = MongodStarter.getDefaultInstance();

        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.DEVELOPMENT)
                .net(net)
                .build();

        MongodExecutable mongoProc = starter.prepare(mongodConfig);
        mongoProc.start();

        return mongoProc;
    }
}
