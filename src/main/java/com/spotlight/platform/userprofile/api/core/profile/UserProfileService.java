package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.exceptions.InvalidCommandException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.command.Command;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;

import javax.inject.Inject;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileService {
    private final UserProfileDao userProfileDao;
    public static final String INVALID_COMMAND_TYPE = "This command type is invalid";
    public static final String INVALID_VALUE = "This value is invalid";

    @Inject
    public UserProfileService(UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    public UserProfile get(UserId userId) {
        return userProfileDao.get(userId).orElseThrow(EntityNotFoundException::new);
    }

    public UserProfile processCommand(Map<UserProfilePropertyName, UserProfilePropertyValue> propertyMap, Command command) {
        Map<UserProfilePropertyName, UserProfilePropertyValue> newmap = new HashMap<>(propertyMap);
        switch (command.getType()) {
            case "replace" -> {
                return replace(newmap, command);
            }
            case "increment" -> {
                return increment(newmap, command);
            }
            case "collect" -> {
                return collect(newmap, command);
            }
            default -> throw new InvalidCommandException(INVALID_COMMAND_TYPE);
        }
    }

    private UserProfile replace(Map<UserProfilePropertyName, UserProfilePropertyValue> propertyMap, Command command) {
        propertyMap.putAll(command.getProperties());
        UserProfile updatedUserProfile = new UserProfile(command.getUserId(), Instant.now(), propertyMap);
        userProfileDao.put(updatedUserProfile);
        return updatedUserProfile;
    }

    private UserProfile increment(Map<UserProfilePropertyName, UserProfilePropertyValue> propertyMap, Command command) {
        for (Map.Entry<UserProfilePropertyName, UserProfilePropertyValue> property : command.getProperties().entrySet()) {
            if (!(property.getValue().getValueObject() instanceof Integer)) {
                throw new InvalidCommandException(INVALID_VALUE);
            } else {
                if (!propertyMap.containsKey(property.getKey())) {

                    propertyMap.put(property.getKey(), property.getValue());
                } else {
                    int newValue = (int) propertyMap.get(property.getKey()).getValueObject()
                            + (int) property.getValue().getValueObject();
                    propertyMap.put(property.getKey(), UserProfilePropertyValue.valueOf(newValue));
                }
            }
        }
        UserProfile updatedUser = new UserProfile(command.getUserId(), Instant.now(), propertyMap);
        userProfileDao.put(updatedUser);
        return updatedUser;
    }

    private UserProfile collect(Map<UserProfilePropertyName, UserProfilePropertyValue> propertyMap, Command command) {
        for (Map.Entry<UserProfilePropertyName, UserProfilePropertyValue> property : command.getProperties().entrySet()) {
            if (!(property.getValue().getValueObject() instanceof List)) {
                throw new InvalidCommandException(INVALID_VALUE);
            } else {
                if (!propertyMap.containsKey(property.getKey())) {
                    propertyMap.put(property.getKey(), property.getValue());
                } else {
                    List<UserProfilePropertyValue> newValue = new ArrayList<>((List<UserProfilePropertyValue>) propertyMap.get(property.getKey()).getValueObject());
                    newValue.addAll((List<UserProfilePropertyValue>) property.getValue().getValueObject());
                    propertyMap.put(property.getKey(), UserProfilePropertyValue.valueOf(newValue));
                }
            }
        }
        UserProfile updatedUser = new UserProfile(command.getUserId(), Instant.now(), propertyMap);
        userProfileDao.put(updatedUser);
        return updatedUser;

    }
}
