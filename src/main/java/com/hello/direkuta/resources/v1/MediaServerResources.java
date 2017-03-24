package com.hello.direkuta.resources.v1;

import com.google.common.collect.Maps;

import com.amazonaws.util.EC2MetadataUtils;
import com.codahale.metrics.annotation.Timed;
import com.hello.direkuta.clients.EvoStreamClient;
import com.hello.direkuta.models.EvoAPIRequest;
import com.hello.direkuta.models.EvoResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/v1/server")
public class MediaServerResources {
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaServerResources.class);

    private final EvoStreamClient evoStreamClient;

    public MediaServerResources(final EvoStreamClient evoStreamClient) {
        this.evoStreamClient = evoStreamClient;
    }


    @GET
    @Timed
    @Path("/version")
    public String getStatus() {
        final Map<String, String> params = Maps.newHashMap();
        final EvoAPIRequest request = new EvoAPIRequest.Builder()
            .withCommandName("Version")
            .withParameters(params)
            .build();
        final EvoResult result = evoStreamClient.executeRequest(request);
        if(EvoResult.ResultType.SUCCESS != result.resultType) {
            return result.resultType.toString();
        }
        return result.response.data.get("releaseNumber").toString();
    }

    @GET
    @Timed
    @Path("/alias")
    public String setStreamAlias() {
        final Map<String, String> params = Maps.newHashMap();
        params.put("localStreamName", "Pi3");
        params.put("aliasName", UUID.randomUUID().toString());
        params.put("expirePeriod", "-60");

        final EvoAPIRequest request = new EvoAPIRequest.Builder()
            .withCommandName("addStreamAlias")
            .withParameters(params)
            .build();
        final EvoResult result = evoStreamClient.executeRequest(request);
        if(EvoResult.ResultType.SUCCESS != result.resultType) {
            return result.resultType.toString();
        }
        final String hostName = EC2MetadataUtils.getNetworkInterfaces().get(0).getPublicHostname();

        return "rtsp://" + hostName + ":5544/" + result.response.data.get("aliasName");
    }
}