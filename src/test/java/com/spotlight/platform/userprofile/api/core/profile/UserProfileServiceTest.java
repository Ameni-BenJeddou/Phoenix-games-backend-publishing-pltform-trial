package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.exceptions.InvalidCommandException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.command.Command;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfileFixtures;

import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.spotlight.platform.userprofile.api.core.profile.UserProfileService.INVALID_COMMAND_TYPE;
import static com.spotlight.platform.userprofile.api.core.profile.UserProfileService.INVALID_VALUE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserProfileServiceTest {
    private final UserProfileDao userProfileDaoMock = mock(UserProfileDao.class);
    private final UserProfileService userProfileService = new UserProfileService(userProfileDaoMock);

    @Nested
    @DisplayName("get")
    class GetTests {
        @Test
        void Test_getForExistingUser_returnsUser() {
            when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

            assertThat(userProfileService.get(UserProfileFixtures.USER_ID)).usingRecursiveComparison()
                    .isEqualTo(UserProfileFixtures.USER_PROFILE);
        }

        @Test
        void Test_getForNonExistingUser_throwsException() {
            when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userProfileService.get(UserProfileFixtures.USER_ID)).isExactlyInstanceOf(
                    EntityNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Commands")
    class CommandTests {
        @Test
        void Test_replaceForValidCommand_returnOkay() {
            //Arrange
            Command replace_Command = new Command(UserProfileFixtures.USER_ID, "replace", UserProfileFixtures.EXISTING_PROPERTY_REPLACE);
            var expected_Value = UserProfileFixtures.EXISTING_PROPERTY_REPLACE
                    .get(UserProfilePropertyName.valueOf("replace_property"));
            //Act
            UserProfile updatedUser = userProfileService.processCommand(UserProfileFixtures.USER_PROPERTIES, replace_Command);

            //Assert
            assertThat(updatedUser.userProfileProperties().get((UserProfilePropertyName.valueOf("replace_property")))).
                    isEqualTo(expected_Value);
        }

        @Test
        void Test_incrementPositivelyForValidCommand_returnOkay() {
            //Arrange
            Command increment_Command = new Command(UserProfileFixtures.USER_ID, "increment", UserProfileFixtures.EXISTING_PROPERTY_INCREMENT_POSITIVE);
            int expected_Value = (int) UserProfileFixtures.EXISTING_PROPERTY_INCREMENT_POSITIVE
                    .get(UserProfilePropertyName.valueOf("increment_property")).getValueObject()
                    + (int) UserProfileFixtures.USER_PROFILE.userProfileProperties()
                    .get(UserProfilePropertyName.valueOf("increment_property")).getValueObject();
            //Act
            UserProfile updatedUser = userProfileService.processCommand(UserProfileFixtures.USER_PROPERTIES, increment_Command);

            //Assert
            assertThat(updatedUser.userProfileProperties().get((UserProfilePropertyName.valueOf("increment_property")))).
                    isEqualTo(UserProfilePropertyValue.valueOf(expected_Value));

        }

        @Test
        void Test_incrementNegativelyForValidCommand_returnOkay() {
            //Arrange
            Command increment_Command = new Command(UserProfileFixtures.USER_ID, "increment", UserProfileFixtures.EXISTING_PROPERTY_INCREMENT_NEGATIVE);
            int expected_Value = (int) UserProfileFixtures.USER_PROFILE.userProfileProperties()
                    .get(UserProfilePropertyName.valueOf("increment_property")).getValueObject() +
                    (int) UserProfileFixtures.EXISTING_PROPERTY_INCREMENT_NEGATIVE
                            .get(UserProfilePropertyName.valueOf("increment_property")).getValueObject();
            //Act
            UserProfile updatedUser = userProfileService.processCommand(UserProfileFixtures.USER_PROPERTIES, increment_Command);

            //Assert
            assertThat(updatedUser.userProfileProperties().get((UserProfilePropertyName.valueOf("increment_property")))).
                    isEqualTo(UserProfilePropertyValue.valueOf(expected_Value));
        }

        @Test
        void Test_collectForValidCommand_returnOkay() {
            //Arrange
            Command collect_Command = new Command(UserProfileFixtures.USER_ID, "collect", UserProfileFixtures.EXISTING_PROPERTY_COLLECT);
            List<UserProfilePropertyValue> expected_Value = new ArrayList<>((List<UserProfilePropertyValue>) UserProfileFixtures.USER_PROFILE.userProfileProperties()
                    .get(UserProfilePropertyName.valueOf("collect_property")).getValueObject());
            expected_Value.addAll(UserProfileFixtures.COLLECTION_AFTER_ADD);
            //Act
            UserProfile updatedUser = userProfileService.processCommand(UserProfileFixtures.USER_PROPERTIES, collect_Command);

            //Assert
            assertThat(updatedUser.userProfileProperties().get((UserProfilePropertyName.valueOf("collect_property")))).
                    isEqualTo(UserProfilePropertyValue.valueOf(expected_Value));
        }

        @Test
        void Test_replaceForValidCommandNonExistingProperty_returnOkay() {
            //Arrange
            Command replace_Command = new Command(UserProfileFixtures.USER_ID, "replace", UserProfileFixtures.NON_EXISTING_REPLACE_PROPERTY);
            UserProfilePropertyValue expected_Value = UserProfileFixtures.NON_EXISTING_REPLACE_PROPERTY
                    .get(UserProfilePropertyName.valueOf("new_replace_property"));
            //Act
            UserProfile updatedUser = userProfileService.processCommand(UserProfileFixtures.USER_PROPERTIES, replace_Command);

            //Assert
            assertThat(updatedUser.userProfileProperties().get((UserProfilePropertyName.valueOf("new_replace_property")))).
                    isEqualTo(expected_Value);
        }

        @Test
        void Test_incrementPositivelyForValidCommandNonExistingProperty_returnOkay() {
            //Arrange
            Command increment_Command = new Command(UserProfileFixtures.USER_ID, "increment", UserProfileFixtures.NON_EXISTING_INCREMENT_POSITIVELY_PROPERTY);
            UserProfilePropertyValue expected_Value = UserProfileFixtures.NON_EXISTING_INCREMENT_POSITIVELY_PROPERTY
                    .get(UserProfilePropertyName.valueOf("new_increment_property"));
            //Act
            UserProfile updatedUser = userProfileService.processCommand(UserProfileFixtures.USER_PROPERTIES, increment_Command);

            //Assert
            assertThat(updatedUser.userProfileProperties().get((UserProfilePropertyName.valueOf("new_increment_property")))).
                    isEqualTo(expected_Value);
        }

        @Test
        void Test_incrementNegativelyForValidCommandNonExistingProperty_returnOkay() {
            //Arrange
            Command increment_Command = new Command(UserProfileFixtures.USER_ID, "increment", UserProfileFixtures.NON_EXISTING_INCREMENT_NEGATIVELY_PROPERTY);
            UserProfilePropertyValue expected_Value = UserProfileFixtures.NON_EXISTING_INCREMENT_NEGATIVELY_PROPERTY
                    .get(UserProfilePropertyName.valueOf("new_increment_property"));
            //Act
            UserProfile updatedUser = userProfileService.processCommand(UserProfileFixtures.USER_PROPERTIES, increment_Command);

            //Assert
            assertThat(updatedUser.userProfileProperties().get((UserProfilePropertyName.valueOf("new_increment_property")))).
                    isEqualTo(expected_Value);
        }

        @Test
        void Test_collectForValidCommandNonExistingProperty_returnOkay() {
            //Arrange
            Command collect_Command = new Command(UserProfileFixtures.USER_ID, "collect", UserProfileFixtures.NON_EXISTING_COLLECT_PROPERTY);
            UserProfilePropertyValue expected_Value = UserProfileFixtures.NON_EXISTING_COLLECT_PROPERTY
                    .get(UserProfilePropertyName.valueOf("new_collect_property"));
            //Act
            UserProfile updatedUser = userProfileService.processCommand(UserProfileFixtures.USER_PROPERTIES, collect_Command);

            //Assert
            assertThat(updatedUser.userProfileProperties().get((UserProfilePropertyName.valueOf("new_collect_property")))).
                    isEqualTo(expected_Value);
        }


        @Test
        void Test_IncrementForInValidCommandInvalidValue_throwException() {
            //Arrange
            Command increment_Command = new Command(UserProfileFixtures.USER_ID, "increment", UserProfileFixtures.INCREMENT_COMMAND_WITH_INVALID_VALUE);

            //Act
            InvalidCommandException exception = catchThrowableOfType(() ->
                    userProfileService.processCommand(UserProfileFixtures.USER_PROPERTIES, increment_Command), InvalidCommandException.class);

            //Assert
            assertThat(exception.getMessage()).isEqualTo(INVALID_VALUE);
        }

        @Test
        void Test_CollectForInValidCommandInvalidValue_throwException() {
            //Arrange
            Command collect_Command = new Command(UserProfileFixtures.USER_ID, "collect", UserProfileFixtures.COLLECT_COMMAND_WITH_INVALID_VALUE);

            //Act
            InvalidCommandException exception = catchThrowableOfType(() ->
                    userProfileService.processCommand(UserProfileFixtures.USER_PROPERTIES, collect_Command), InvalidCommandException.class);

            //Assert
            assertThat(exception.getMessage()).isEqualTo(INVALID_VALUE);
        }

        @Test
        void Test_whenInValidCommandType_thenThrowException() {
            //Arrange
            Command invalid_Command = new Command(UserProfileFixtures.USER_ID, "invalid", UserProfileFixtures.EXISTING_PROPERTY_REPLACE);

            //Act
            InvalidCommandException exception = catchThrowableOfType(() ->
                    userProfileService.processCommand(UserProfileFixtures.USER_PROPERTIES, invalid_Command), InvalidCommandException.class);

            //Assert
            assertThat(exception.getMessage()).isEqualTo(INVALID_COMMAND_TYPE);
        }
    }
}