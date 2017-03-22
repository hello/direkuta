package com.hello.direkuta.resources.v1;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hello.direkuta.handlers.EventHandler;
import com.hello.direkuta.models.EvoEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/events")
public class EventResources {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventResources.class);

    @Context
    HttpServletRequest request;
    private Set<EventHandler> eventHandlers;

    public EventResources(final Set<EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }


    @GET
    @Timed
    public String getStatus() {
        LOGGER.debug("Getting status...");
        return "Status";

    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/process")
    public String passwordUpdate(final String jsonString) {
        EvoEvent event;
        try {
            event = new ObjectMapper().readValue(jsonString, EvoEvent.class);
        } catch (Exception ex) {
            LOGGER.error("error=bad-json-parse");
            LOGGER.info("json_message={}", jsonString);
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).build());
        }
        LOGGER.info("message=event-received type={}", event.eventType);
        LOGGER.info("json_message={}", jsonString);
        for (final EventHandler handler : eventHandlers) {
            if (handler.canHandleEvent(event.eventType)) {
                return handler.handleEvent(event);
            }
        }
        throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).build());
    }

}
