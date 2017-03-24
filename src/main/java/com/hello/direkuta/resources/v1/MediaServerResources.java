package com.hello.direkuta.resources.v1;

import com.google.common.collect.Maps;

import com.amazonaws.util.EC2MetadataUtils;
import com.codahale.metrics.annotation.Timed;
import com.hello.direkuta.clients.EvoStreamClient;
import com.hello.direkuta.configuration.EvoStreamConfiguration;
import com.hello.direkuta.models.AddAliasRequest;
import com.hello.direkuta.models.EvoAPIRequest;
import com.hello.direkuta.models.EvoResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/server")
public class MediaServerResources {
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaServerResources.class);

    private final EvoStreamClient evoStreamClient;
    private final EvoStreamConfiguration evoStreamConfiguration;

    public MediaServerResources(final EvoStreamClient evoStreamClient,
                                final EvoStreamConfiguration evoStreamConfiguration) {
        this.evoStreamClient = evoStreamClient;
        this.evoStreamConfiguration = evoStreamConfiguration;
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

    @POST
    @Timed
    @Path("/alias")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    public String setStreamAlias(final AddAliasRequest aliasRequest) {
        final Map<String, String> params = Maps.newHashMap();
        params.put("localStreamName", aliasRequest.localStreamName);
        final String newAlias = (aliasRequest.aliasName == null) ? UUID.randomUUID().toString() : aliasRequest.aliasName;
        params.put("aliasName", newAlias);
        final String expirePeriod = (aliasRequest.expirePeriod == null) ? evoStreamConfiguration.getDefaultAliasExpiration().toString() : aliasRequest.expirePeriod.toString();
        params.put("expirePeriod", expirePeriod);

        final EvoAPIRequest request = new EvoAPIRequest.Builder()
            .withCommandName("addStreamAlias")
            .withParameters(params)
            .build();
        final EvoResult result = evoStreamClient.executeRequest(request);

        if(EvoResult.ResultType.SUCCESS != result.resultType) {
            LOGGER.error("error=add-alias-request-failed");
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).build());
        }

        final String hostName = EC2MetadataUtils.getNetworkInterfaces().get(0).getPublicHostname();
        return "rtsp://"+ hostName +":"+ evoStreamConfiguration.getOutboundPort() +"/"+ result.response.data.get("aliasName");
    }
}
