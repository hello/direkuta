package com.hello.direkuta.handlers;

import com.google.common.collect.Sets;

import com.amazonaws.util.EC2MetadataUtils;
import com.hello.direkuta.models.EvoEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * Created by jnorgan on 3/20/17.
 */
public class OutStreamEventHandler extends EventHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(OutStreamEventHandler.class);
  private static final Set<String> HANDLED_EVENTS = Sets.newHashSet("outStreamCreated", "outStreamClosed");
  private static final String REDIS_KEY = "StreamTracking";
  private static final String REDIS_KEY_SEPARATOR = "|";

  private final JedisPool jedisPool;

  public OutStreamEventHandler(final JedisPool jedisPool) {
    this.jedisPool = jedisPool;
  }

  @Override
  public Set<String> getEventTypes() {
    final Set<String> handledEvents = Sets.newHashSet();
    handledEvents.addAll(HANDLED_EVENTS);
    return handledEvents;
  }

  @Override
  public String handleEventInternal(final EvoEvent event) {
    LOGGER.info("action=event-handled event_type={}", event.eventType);
    //persist state of stream

    Jedis jedis = null;

    final String instanceId = EC2MetadataUtils.getInstanceId();
    try {
      jedis = jedisPool.getResource();
      final String redisKey = instanceId + REDIS_KEY_SEPARATOR + event.payload.get("name") + REDIS_KEY_SEPARATOR + event.eventType.toString();
      jedis.zadd(REDIS_KEY, event.timestamp, redisKey);

    }catch (JedisDataException exception) {
      LOGGER.error("error=jedis-data-exception message={}", exception.getMessage());
      if(jedis != null) {
        jedis.close();
      }
    }
    return "Event Handled!";
  }
}
