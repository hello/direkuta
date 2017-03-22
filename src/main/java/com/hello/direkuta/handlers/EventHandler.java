package com.hello.direkuta.handlers;

import com.hello.direkuta.models.EventType;
import com.hello.direkuta.models.EvoEvent;

import java.util.Set;

/**
 * Created by jnorgan on 2/27/17.
 */
public abstract class EventHandler {

  public EventHandler() {
  }

  public Boolean canHandleEvent(final EventType eventType) {
    return getEventTypes().contains(eventType.toString());
  }

  public abstract Set<String> getEventTypes();

  public String handleEvent(final EvoEvent event) {
    return handleEventInternal(event);
  }

  public abstract String handleEventInternal(final EvoEvent event);
}
