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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
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

    @PUT
    @Path("command")
    public Response receiveCommand(@Valid List<Command> commands) {
        Map<String, String> command_status = new HashMap<>();
        boolean is_errors = false;
        for (int i = 0; i < commands.size(); i++) {
            Command command = commands.get(i);
            try {
                UserProfile userProfile = userProfileService.get(command.getUserId());
                userProfileService.processCommand(userProfile.userProfileProperties(), command);
                command_status.put("command number " + i, " was successful");
            } catch (InvalidCommandException | EntityNotFoundException exception) {
                is_errors = true;
                command_status.put("command number " + i, " has failed because: " + exception.getMessage());
            }
        }
        if (!command_status.containsValue(" was successful"))
            return Response.status(Response.Status.BAD_REQUEST).build();
        else if (is_errors)
            return Response.accepted(command_status).build();
        else
            return Response.ok().build();
    }
}
