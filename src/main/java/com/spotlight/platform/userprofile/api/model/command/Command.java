package com.spotlight.platform.userprofile.api.model.command;

import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@AllArgsConstructor
@Getter
@Setter
public class Command {

    private UserId userId;
    private String type;
    private Map<UserProfilePropertyName, UserProfilePropertyValue> properties;

}
