package com.hello.direkuta.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

/**
 * Created by jnorgan on 3/23/17.
 */
@JsonDeserialize(builder = APIResponse.Builder.class)
public class APIResponse {
  @JsonProperty("data")
  public final Map<String, Object> data;

  @JsonProperty("description")
  public final String description;

  @JsonProperty("status")
  public final String status;

  public APIResponse(final Map<String, Object> data, final String description, final String status) {
    this.data = data;
    this.description = description;
    this.status = status;
  }

  public String getResponseString() {
    return data.toString();
  }

  public static final class Builder {
    private Map<String, Object> data;
    private String description;
    private String status;

    private Builder() {
    }

    public static Builder anAddAliasResponse() {
      return new Builder();
    }

    public Builder withData(@JsonProperty("data") final Map<String, Object> data) {
      this.data = data;
      return this;
    }

    public Builder withDescription(@JsonProperty("description") final String description) {
      this.description = description;
      return this;
    }

    public Builder withStatus(@JsonProperty("status") final String status) {
      this.status = status;
      return this;
    }

    public APIResponse build() {
      return new APIResponse(data, description, status);
    }
  }
}
