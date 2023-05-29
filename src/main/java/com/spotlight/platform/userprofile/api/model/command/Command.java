package com.spotlight.platform.userprofile.api.model.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class Command {
    @NotNull
    @JsonProperty
    private UserId userId;
    @JsonProperty
    @NotNull
    private String type;
    @JsonProperty
    @NotNull
    private Map<UserProfilePropertyName, UserProfilePropertyValue> properties;

    public UserId getUserId() {
        return userId;
    }

    public Command() {
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public Command(UserId userId, String type, Map<UserProfilePropertyName, UserProfilePropertyValue> properties) {
        this.userId = userId;
        this.type = type;
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<UserProfilePropertyName, UserProfilePropertyValue> getProperties() {
        return properties;
    }

    public void setProperties(Map<UserProfilePropertyName, UserProfilePropertyValue> properties) {
        this.properties = properties;
    }
}
