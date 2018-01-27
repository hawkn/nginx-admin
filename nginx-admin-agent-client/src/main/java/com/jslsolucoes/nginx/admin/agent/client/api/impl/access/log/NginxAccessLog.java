package com.jslsolucoes.nginx.admin.agent.client.api.impl.access.log;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.jslsolucoes.nginx.admin.agent.client.RestClient;
import com.jslsolucoes.nginx.admin.agent.client.api.NginxAgentClientApi;
import com.jslsolucoes.nginx.admin.agent.client.api.impl.DefaultNginxAgentClientApi;
import com.jslsolucoes.nginx.admin.agent.client.api.impl.HttpHeader;
import com.jslsolucoes.nginx.admin.agent.model.response.NginxExceptionResponse;
import com.jslsolucoes.nginx.admin.agent.model.response.NginxResponse;
import com.jslsolucoes.nginx.admin.agent.model.response.access.log.NginxAccessLogCollectResponse;
import com.jslsolucoes.nginx.admin.agent.model.response.access.log.NginxAccessLogRotateResponse;

public class NginxAccessLog extends DefaultNginxAgentClientApi implements NginxAgentClientApi {

	private final ScheduledExecutorService scheduledExecutorService;
	private final String endpoint;
	private final String authorizationKey;

	public NginxAccessLog(ScheduledExecutorService scheduledExecutorService, String endpoint, String authorizationKey) {
		this.scheduledExecutorService = scheduledExecutorService;
		this.endpoint = endpoint;
		this.authorizationKey = authorizationKey;
	}

	public CompletableFuture<NginxResponse> collect() {
		return CompletableFuture.supplyAsync(() -> {
			try (RestClient restClient = RestClient.build()) {
				WebTarget webTarget = restClient.target(endpoint);
				Response response = webTarget.path("accessLog").path("collect").request()
						.header(HttpHeader.AUTHORIZATION, authorizationKey).get();
				return responseFor(response, NginxAccessLogCollectResponse.class);
			} catch (Exception e) {
				return new NginxExceptionResponse(e);
			}
		}, scheduledExecutorService);
	}

	public CompletableFuture<NginxResponse> rotate() {
		return CompletableFuture.supplyAsync(() -> {
			try (RestClient restClient = RestClient.build()) {
				WebTarget webTarget = restClient.target(endpoint);
				Response response = webTarget.path("accessLog").path("rotate").request()
						.header(HttpHeader.AUTHORIZATION, authorizationKey).get();
				return responseFor(response, NginxAccessLogRotateResponse.class);
			} catch (Exception e) {
				return new NginxExceptionResponse(e);
			}
		}, scheduledExecutorService);
	}

}
