package com.hello.direkuta.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

/**
 * Created by jnorgan on 3/16/17.
 */
@JsonDeserialize(builder = EvoEvent.Builder.class)
public class EvoEvent {

  @JsonProperty("processId")
  public final Integer processId;

  @JsonProperty("id")
  public final Integer id; //Need to determine what this is

  @JsonProperty("type")
  public final EventType eventType;

  @JsonProperty("timestamp")
  public final Long timestamp;

  @JsonProperty("payload")
  public final Map<String, Object> payload;

  private EvoEvent(final Integer processId,
                   final Integer id,
                   final EventType eventType,
                   final Long timestamp,
                   final Map<String, Object> payload) {
    this.processId = processId;
    this.id = id;
    this.eventType = eventType;
    this.timestamp = timestamp;
    this.payload = payload;
  }

    public static class Builder {

    private Integer processId;
    private Integer id;
    private EventType eventType;
    private Long timestamp;
    private Map<String, Object> payload;

    public Builder() {
    }

    public Builder withProcessId(@JsonProperty("processId") final Integer processId) {
      this.processId = processId;
      return this;
    }

    public Builder withId(@JsonProperty("id") final Integer id) {
      this.id = id;
      return this;
    }

    public Builder withType(@JsonProperty("type") final EventType eventType) {
      this.eventType = eventType;
      return this;
    }

    public Builder withTimestamp(@JsonProperty("timestamp") final long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder withPayload(@JsonProperty("payload") final Map<String, Object> payload) {
      this.payload = payload;
      return this;
    }

    public EvoEvent build() {
      return new EvoEvent(this.processId, this.id, this.eventType, this.timestamp, this.payload);
    }
  }
}
