package org.globex.retail.ai.complaint.rest;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Path("/api/v1")
public class AgentResource {

    @Path("/request")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> handleUserRequest(String request) {
        if (request == null || request.isEmpty()) {
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST)
                    .entity(new JsonObject().put("error", "Invalid data received. Null or empty request").encode()).build());
        }
        return Uni.createFrom().item(() -> request).emitOn(Infrastructure.getDefaultWorkerPool())
                .onItem().transform(r -> {
                    Log.infof("Agent request received: %s", r);
                    return "success";
                })
                .onItem().transform(response -> Response.status(Response.Status.OK)
                        .entity(new JsonObject().put("response", response)
                                .put("timestamp", Instant.now().truncatedTo(ChronoUnit.SECONDS).toString()).encode()).build())
                .onFailure().recoverWithItem(throwable -> {
                    Log.error("Failed to handle request", throwable);
                    return Response.serverError().entity(new JsonObject().put("error", "Internal server error processing the request")).build();
                });
    }
}
