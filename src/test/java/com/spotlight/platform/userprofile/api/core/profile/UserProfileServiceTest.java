package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.command.Command;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfileFixtures;

import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

            //Act
            UserProfile userProfile = userProfileService.replace(replace_Command);

            //Assert
            assertThat(userProfile.userProfileProperties().get((UserProfilePropertyName.valueOf("replace_property"))).toString()).isEqualTo("property_after_Value");
        }

        @Test
        void Test_incrementPositivelyForValidCommand_returnOkay() {
            //Arrange
            Command increment_Command = new Command(UserProfileFixtures.USER_ID, "increment", UserProfileFixtures.EXISTING_PROPERTY_INCREMENT_POSITIVE);

            //Act
            UserProfile userProfile = userProfileService.replace(increment_Command);

            //Assert
            assertThat(userProfile.userProfileProperties().get((UserProfilePropertyName.valueOf("increment_property"))).toString()).isEqualTo(String.valueOf(60));
        }

        @Test
        void Test_incrementNegativelyForValidCommand_returnOkay() {
            //Arrange
            Command increment_Command = new Command(UserProfileFixtures.USER_ID, "increment", UserProfileFixtures.EXISTING_PROPERTY_INCREMENT_NEGATIVE);

            //Act
            UserProfile userProfile = userProfileService.replace(increment_Command);

            //Assert
            assertThat(userProfile.userProfileProperties().get((UserProfilePropertyName.valueOf("increment_property"))).toString()).isEqualTo(String.valueOf(40));
        }

        @Test
        void Test_collectForValidCommand_returnOkay() {
            //Arrange
            Command replace_Command = new Command(UserProfileFixtures.USER_ID, "collect", UserProfileFixtures.EXISTING_PROPERTY_COLLECT);
            List<String> expected_List = Arrays.asList("Before_Item1", "Before_Item2", "Before_Item3", "Added_Item1", "Added_Item2", "Added_Item3");

            //Act
            UserProfile userProfile = userProfileService.replace(replace_Command);

            //Assert
            assertThat(userProfile.userProfileProperties().get((UserProfilePropertyName.valueOf("collect_property")))).asList()
                    .isEqualTo(expected_List);
        }
    }
}