package com.spotlight.platform.userprofile.api.model.profile.primitives;

import com.spotlight.platform.helpers.FixtureHelpers;
import com.spotlight.platform.userprofile.api.model.command.Command;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserProfileFixtures {
    public static final UserId USER_ID = UserId.valueOf("existing-user-id");
    public static final UserId NON_EXISTING_USER_ID = UserId.valueOf("non-existing-user-id");
    public static final UserId INVALID_USER_ID = UserId.valueOf("invalid-user-id-%");

    public static final Instant LAST_UPDATE_TIMESTAMP = Instant.parse("2021-06-01T09:16:36.123Z");

    public static final UserProfile USER_PROFILE = new UserProfile(USER_ID, LAST_UPDATE_TIMESTAMP,
            Map.of(UserProfilePropertyName.valueOf("replace_property"), UserProfilePropertyValue.valueOf("property_before_Value"),
                    UserProfilePropertyName.valueOf("increment_property"), UserProfilePropertyValue.valueOf(50),
                    UserProfilePropertyName.valueOf("collect_property"), UserProfilePropertyValue.valueOf((Arrays.asList("Before_Item1", "Before_Item2", "Before_Item3")))));

    public static final String SERIALIZED_USER_PROFILE = FixtureHelpers.fixture("/fixtures/model/profile/userProfile.json");

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> EXISTING_PROPERTY_REPLACE =
            Map.of(UserProfilePropertyName.valueOf("replace_property"), UserProfilePropertyValue.valueOf("property_after_Value"));

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> EXISTING_PROPERTY_INCREMENT_POSITIVE =
            Map.of(UserProfilePropertyName.valueOf("increment_property"), UserProfilePropertyValue.valueOf(10));

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> EXISTING_PROPERTY_INCREMENT_NEGATIVE =
            Map.of(UserProfilePropertyName.valueOf("increment_property"), UserProfilePropertyValue.valueOf(-10));

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> EXISTING_PROPERTY_COLLECT =
            Map.of(UserProfilePropertyName.valueOf("collect_property"), UserProfilePropertyValue.valueOf(Arrays.asList("Added_Item1", "Added_Item2", "Added_Item3")));

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> NON_EXISTING_PROPERTY =
            Map.of(UserProfilePropertyName.valueOf("propertyInvalid"), UserProfilePropertyValue.valueOf("I do not exist"));
}
