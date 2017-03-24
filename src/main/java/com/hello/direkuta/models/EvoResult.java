package com.hello.direkuta.models;

import com.google.common.collect.Maps;

/**
 * Created by jnorgan on 3/23/17.
 */
public class EvoResult {
  public enum ResultType {
    SUCCESS,
    FAILURE,
    ERROR
  }

  public final ResultType resultType;
  public final APIResponse response;

  public EvoResult(final ResultType resultType,
                   final APIResponse response) {
    this.resultType = resultType;
    this.response = response;
  }
  public static EvoResult create(final ResultType resultType) {
    return new EvoResult(resultType, new APIResponse(Maps.newHashMap(), "", resultType.toString()));
  }
}
