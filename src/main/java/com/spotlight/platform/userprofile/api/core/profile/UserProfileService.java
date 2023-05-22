package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.exceptions.InvalidCommandException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.command.Command;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class UserProfileService {
    private final UserProfileDao userProfileDao;
    public static final String INVALID_COMMAND_TYPE = "This command type is invalid";

    @Inject
    public UserProfileService(UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    public UserProfile get(UserId userId) {
        return userProfileDao.get(userId).orElseThrow(EntityNotFoundException::new);
    }

    public void processCommand(UserProfile userProfile, Command command) {
        switch (command.getType()) {
            case "replace" -> replace(userProfile, command);
            case "increment" -> increment(userProfile, command);
            case "collect" -> collect(userProfile, command);
            default -> throw new InvalidCommandException(INVALID_COMMAND_TYPE);
        }
    }

    private void replace(UserProfile userProfile, Command command) {
    }

    private void increment(UserProfile userProfile, Command command) {
    }

    private void collect(UserProfile userProfile, Command command) {
    }
}
