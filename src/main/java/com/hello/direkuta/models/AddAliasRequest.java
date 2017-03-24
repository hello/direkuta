package com.hello.direkuta.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by jnorgan on 3/24/17.
 */
@JsonDeserialize(builder = AddAliasRequest.Builder.class)
public class AddAliasRequest {
  @JsonProperty("localStreamName")
  public final String localStreamName;

  @JsonProperty("aliasName")
  public final String aliasName;

  @JsonProperty("expirePeriod")
  public final Integer expirePeriod;

  public AddAliasRequest(String localStreamName, String aliasName, Integer expirePeriod) {
    this.localStreamName = localStreamName;
    this.aliasName = aliasName;
    this.expirePeriod = expirePeriod;
  }


  public static final class Builder {
    public String localStreamName;
    public String aliasName;
    public Integer expirePeriod;

    private Builder() {
    }

    public static Builder anAddAliasRequest() {
      return new Builder();
    }

    public Builder withLocalStreamName(@JsonProperty("localStreamName") final String localStreamName) {
      this.localStreamName = localStreamName;
      return this;
    }

    public Builder withAliasName(@JsonProperty("aliasName") final String aliasName) {
      this.aliasName = aliasName;
      return this;
    }

    public Builder withExpirePeriod(@JsonProperty("expirePeriod") final Integer expirePeriod) {
      this.expirePeriod = expirePeriod;
      return this;
    }

    public AddAliasRequest build() {
      return new AddAliasRequest(localStreamName, aliasName, expirePeriod);
    }
  }
}
