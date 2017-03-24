package com.hello.direkuta.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.apache.commons.codec.binary.Base64.encodeBase64;

/**
 * Created by jnorgan on 3/23/17.
 */
public class EvoAPIRequest {
  private static final Logger LOGGER = LoggerFactory.getLogger(EvoAPIRequest.class);

  private String commandName;
  private Map<String, String> params;

  private EvoAPIRequest(final String commandName,
                        final Map<String, String> params) {
    this.commandName = commandName;
    this.params = params;
  }

  public static class Builder {
    private String commandName;
    private Map<String, String> params;

    public Builder() {}

    public Builder withCommandName(final String commandName) {
      this.commandName = commandName;
      return this;
    }

    public Builder withParameters(final Map<String, String> params) {
      this.params = params;
      return this;
    }

    public EvoAPIRequest build() {
      return new EvoAPIRequest(this.commandName, this.params);
    }

  }

  public String getCommandName() {
    return commandName;
  }
  public Map<String, String> getParams() {
    return params;
  }

  public String getEncodedParams() {
    final StringBuilder paramsBuilder = new StringBuilder();
    for(final Map.Entry entry : params.entrySet()){
      final String param = entry.getKey() + "=" + entry.getValue() + " ";
      paramsBuilder.append(param);
    }
    final String paramsString = paramsBuilder.toString();
    return new String(encodeBase64(paramsString.getBytes()));
  }
}
