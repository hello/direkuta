package com.hello.direkuta.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jnorgan on 3/24/17.
 */
public class EvoStreamConfiguration {
  @JsonProperty("default_alias_expiration")
  private Integer defaultAliasExpiration;

  @JsonProperty("rtsp_outbound_port")
  private Integer outboundPort;

  public EvoStreamConfiguration() {
  }

  public Integer getDefaultAliasExpiration() {
    return this.defaultAliasExpiration;
  }

  public Integer getOutboundPort() {
    return this.outboundPort;
  }
}
