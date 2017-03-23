package com.hello.direkuta;

import com.google.common.collect.Sets;

import com.hello.direkuta.clients.EvoStreamClient;
import com.hello.direkuta.configuration.DirekutaConfiguration;
import com.hello.direkuta.handlers.EventHandler;
import com.hello.direkuta.handlers.OutStreamEventHandler;
import com.hello.direkuta.resources.v1.EventResources;
import com.hello.direkuta.resources.v1.MediaServerResources;
import com.hello.suripu.coredropwizard.util.CustomJSONExceptionMapper;

import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.TimeZone;

import io.dropwizard.Application;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.server.AbstractServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import redis.clients.jedis.JedisPool;


public class Direkuta extends Application<DirekutaConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Direkuta.class);

    public static void main(final String[] args) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        DateTimeZone.setDefault(DateTimeZone.UTC);
        new Direkuta().run(args);
    }

    @Override
    public void initialize(final Bootstrap<DirekutaConfiguration> bootstrap) {
        bootstrap.addBundle(new DBIExceptionsBundle());
    }

    @Override
    public void run(DirekutaConfiguration configuration, Environment environment) throws Exception {
        //Doing this programmatically instead of in config files
        AbstractServerFactory sf = (AbstractServerFactory) configuration.getServerFactory();
        // disable all default exception mappers
        sf.setRegisterDefaultExceptionMappers(false);

        environment.jersey().register(new CustomJSONExceptionMapper(configuration.getDebug()));

        final JedisPool jedisPool = new JedisPool(
            configuration.getRedisConfiguration().getHost(),
            configuration.getRedisConfiguration().getPort()
        );

        final Set<EventHandler> eventHandlers = Sets.newHashSet();
        eventHandlers.add(new OutStreamEventHandler(jedisPool));
        environment.jersey().register(new EventResources(eventHandlers));

        final EvoStreamClient evoStreamClient = EvoStreamClient.create();
        environment.jersey().register(new MediaServerResources(evoStreamClient));

    }
}
