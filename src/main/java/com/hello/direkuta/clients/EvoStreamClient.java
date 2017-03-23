package com.hello.direkuta.clients;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hello.direkuta.models.EvoAPIRequest;
import com.hello.direkuta.services.EvoStreamService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by jnorgan on 3/23/17.
 */
public class EvoStreamClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(EvoStreamClient.class);
  private static String DEFAULT_ENDPOINT = "http://127.0.0.1:7777";

  final EvoStreamService service;

  public EvoStreamClient(final EvoStreamService service) {
    this.service = service;
  }

  public static EvoStreamClient create() {
    return EvoStreamClient.create(DEFAULT_ENDPOINT);
  }

  public static EvoStreamClient create(final String baseUrl) {
    final OkHttpClient client = new OkHttpClient.Builder()
        .build();

    final ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    final JacksonConverterFactory converterFactory = JacksonConverterFactory.create(mapper);
    final Retrofit retrofit = new Retrofit.Builder()
        .addConverterFactory(converterFactory)
        .client(client)
        .baseUrl(baseUrl)
        .build();

    final EvoStreamService service = retrofit.create(EvoStreamService.class);
    return new EvoStreamClient(service);
  }

  public EvoAPIRequest.Result executeCommand(final EvoAPIRequest request) {
    final Call<Map<String, Object>> call = service.get(request.getCommandName(), request.getEncodedParams());

    try {
      final Response<Map<String, Object>> response = call.execute();
      if(response.isSuccessful()) {
        final Map<String, Object> resp = response.body();
        LOGGER.info("action=api-request result=success response={}", resp.toString());
        return EvoAPIRequest.Result.SUCCESS;
      }
    } catch (IOException e) {
      LOGGER.error("error=api-request msg={}", e.getMessage());
    }

    LOGGER.error("error=api-request-failure");
    return EvoAPIRequest.Result.FAILURE;
  }
}
