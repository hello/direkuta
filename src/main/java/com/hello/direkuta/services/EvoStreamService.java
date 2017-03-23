package com.hello.direkuta.services;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jnorgan on 3/23/17.
 */
public interface EvoStreamService {
  @GET("/{commandName}")
  Call<Map<String, Object>> get(@Path("commandName") String commandName, @Query("params") String params);
}
