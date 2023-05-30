package com.spotlight.platform.userprofile.api.model.profile.primitives;

import com.spotlight.platform.helpers.FixtureHelpers;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserProfileFixtures {
    public static final UserId USER_ID = UserId.valueOf("existing-user-id");
    public static final UserId NON_EXISTING_USER_ID = UserId.valueOf("non-existing-user-id");
    public static final UserId INVALID_USER_ID = UserId.valueOf("invalid-user-id-%");
    public static final List<UserProfilePropertyValue> COLLECTION_BEFORE_ADD =
            Arrays.asList(UserProfilePropertyValue.valueOf("Before_Item1"), UserProfilePropertyValue.valueOf("Before_Item2"));
    public static final List<UserProfilePropertyValue> COLLECTION_AFTER_ADD =
            Arrays.asList(UserProfilePropertyValue.valueOf("New_Item1"), UserProfilePropertyValue.valueOf("New_Item2"));

    public static final Instant LAST_UPDATE_TIMESTAMP = Instant.parse("2021-06-01T09:16:36.123Z");

    public static Map<UserProfilePropertyName, UserProfilePropertyValue> USER_PROPERTIES =
            Map.of(UserProfilePropertyName.valueOf("replace_property"), UserProfilePropertyValue.valueOf("property_before_Value"),
                    UserProfilePropertyName.valueOf("increment_property"), UserProfilePropertyValue.valueOf(50),
                    UserProfilePropertyName.valueOf("collect_property"), UserProfilePropertyValue.valueOf(COLLECTION_BEFORE_ADD));

    public static UserProfile USER_PROFILE = new UserProfile(USER_ID, LAST_UPDATE_TIMESTAMP, USER_PROPERTIES);


    public static final String SERIALIZED_USER_PROFILE = FixtureHelpers.fixture("/fixtures/model/profile/userProfile.json");

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> EXISTING_PROPERTY_REPLACE =
            Map.of(UserProfilePropertyName.valueOf("replace_property"), UserProfilePropertyValue.valueOf("property_after_Value"));

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> EXISTING_PROPERTY_INCREMENT_POSITIVE =
            Map.of(UserProfilePropertyName.valueOf("increment_property"), UserProfilePropertyValue.valueOf(10));

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> EXISTING_PROPERTY_INCREMENT_NEGATIVE =
            Map.of(UserProfilePropertyName.valueOf("increment_property"), UserProfilePropertyValue.valueOf(-10));

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> EXISTING_PROPERTY_COLLECT =
            Map.of(UserProfilePropertyName.valueOf("collect_property"), UserProfilePropertyValue.valueOf(COLLECTION_AFTER_ADD));

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> NON_EXISTING_REPLACE_PROPERTY =
            Map.of(UserProfilePropertyName.valueOf("new_replace_property"), UserProfilePropertyValue.valueOf("I am now born"));

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> NON_EXISTING_INCREMENT_POSITIVELY_PROPERTY =
            Map.of(UserProfilePropertyName.valueOf("new_increment_property"), UserProfilePropertyValue.valueOf(10));

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> NON_EXISTING_INCREMENT_NEGATIVELY_PROPERTY =
            Map.of(UserProfilePropertyName.valueOf("new_increment_property"), UserProfilePropertyValue.valueOf(-10));

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> NON_EXISTING_COLLECT_PROPERTY =
            Map.of(UserProfilePropertyName.valueOf("new_collect_property"), UserProfilePropertyValue.valueOf(COLLECTION_AFTER_ADD));

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> INCREMENT_COMMAND_WITH_INVALID_VALUE =
            Map.of(UserProfilePropertyName.valueOf("increment_property"), UserProfilePropertyValue.valueOf("I am not a number"));

    public static final Map<UserProfilePropertyName, UserProfilePropertyValue> COLLECT_COMMAND_WITH_INVALID_VALUE =
            Map.of(UserProfilePropertyName.valueOf("collect_property"), UserProfilePropertyValue.valueOf(7));

}
