package com.spotlight.platform.userprofile.api.web.resources;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.exceptions.InvalidCommandException;
import com.spotlight.platform.userprofile.api.core.profile.UserProfileService;
import com.spotlight.platform.userprofile.api.model.command.Command;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/users/{userId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserProfileService userProfileService;

    @Inject
    public UserResource(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Path("profile")
    @GET
    public UserProfile getUserProfile(@Valid @PathParam("userId") UserId userId) {
        return userProfileService.get(userId);
    }

    @Path("command")
    @PUT
    public Response receiveCommand(@Valid @PathParam("commandList") List<Command> commands) {
        Map<Command, String> incorrect_Commands = new HashMap<>();
        for (Command command : commands) {
            try {
                UserProfile userProfile = userProfileService.get(command.getUserId());
                userProfileService.processCommand(userProfile, command);
            } catch (InvalidCommandException | EntityNotFoundException exception) {
                incorrect_Commands.put(command, exception.getMessage());
            }
        }
        // is probably gibberish
        if (incorrect_Commands.isEmpty())
            return Response.status(Response.Status.NO_CONTENT).build();
        else return Response.ok(incorrect_Commands).build();
    }
}
