package com.spotlight.platform.userprofile.api.web.resources;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.command.Command;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfileFixtures;
import com.spotlight.platform.userprofile.api.web.UserProfileApiApplication;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.vyarus.dropwizard.guice.test.ClientSupport;
import ru.vyarus.dropwizard.guice.test.jupiter.ext.TestDropwizardAppExtension;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@Execution(ExecutionMode.SAME_THREAD)
class UserResourceIntegrationTest {
    @RegisterExtension
    static TestDropwizardAppExtension APP = TestDropwizardAppExtension.forApp(UserProfileApiApplication.class)
            .randomPorts()
            .hooks(builder -> builder.modulesOverride(new AbstractModule() {
                @Provides
                @Singleton
                public UserProfileDao getUserProfileDao() {
                    return mock(UserProfileDao.class);
                }
            }))
            .randomPorts()
            .create();

    @BeforeEach
    void beforeEach(UserProfileDao userProfileDao) {
        reset(userProfileDao);
    }

    @Nested
    @DisplayName("getUserProfile")
    class GetUserProfileTests {
        private static final String USER_ID_PATH_PARAM = "userId";
        private static final String URL = "/users/{%s}/profile".formatted(USER_ID_PATH_PARAM);

        @Test
        void existingUser_correctObjectIsReturned(ClientSupport client, UserProfileDao userProfileDao) {
            when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request().get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
            assertThatJson(response.readEntity(UserProfile.class)).isEqualTo(UserProfileFixtures.SERIALIZED_USER_PROFILE);
        }

        @Test
        void nonExistingUser_returns404(ClientSupport client, UserProfileDao userProfileDao) {
            when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.empty());

            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request().get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
        }

        @Test
        void validationFailed_returns400(ClientSupport client) {
            var response = client.targetRest()
                    .path(URL)
                    .resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.INVALID_USER_ID)
                    .request()
                    .get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST_400);
        }

        @Test
        void unhandledExceptionOccured_returns500(ClientSupport client, UserProfileDao userProfileDao) {
            when(userProfileDao.get(any(UserId.class))).thenThrow(new RuntimeException("Some unhandled exception"));

            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request().get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Nested
    @DisplayName("processCommand")
    class ProcessCommandTests {
        private static final String USER_ID_PATH_PARAM = "userId";
        private static final String URL = "/users/" + USER_ID_PATH_PARAM + "/command";

        @Test
        public void testReceiveCommand_AllValidCommands_then200(ClientSupport client, UserProfileDao userProfileDao) {
            // Arrange
            when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));
            List<Command> commands = createAllValidCommands();

            // Act
            Response response = client.targetRest().path(URL).request().put(Entity.json(commands));

            // Assert
            assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        }

        @Test
        public void testReceiveCommand_AllInvalidCommands_then400(ClientSupport client, UserProfileDao userProfileDao) {
            // Arrange
            when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));
            List<Command> commands = createAllInvalidCommands();

            // Act
            Response response = client.targetRest().path(URL).request().put(Entity.json(commands));

            // Assert
            assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        }

        @Test
        public void testReceiveCommand_SomeInvalidCommands_then202(ClientSupport client, UserProfileDao userProfileDao) {
            // Arrange
            when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));
            List<Command> commands = createSomeInvalidCommands();

            // Act
            Response response = client.targetRest().path(URL).request().put(Entity.json(commands));

            // Assert
            assertThat(response.getStatus()).isEqualTo(Response.Status.ACCEPTED.getStatusCode());
        }


        private List<Command> createAllValidCommands() {
            List<Command> commands = new ArrayList<>();
            Command replace_Command = new Command(UserProfileFixtures.USER_ID, "replace", UserProfileFixtures.EXISTING_PROPERTY_REPLACE);
            Command increment_Command = new Command(UserProfileFixtures.USER_ID, "increment", UserProfileFixtures.EXISTING_PROPERTY_INCREMENT_POSITIVE);
            Command collect_Command = new Command(UserProfileFixtures.USER_ID, "collect", UserProfileFixtures.EXISTING_PROPERTY_COLLECT);
            commands.add(replace_Command);
            commands.add(increment_Command);
            commands.add(collect_Command);
            return commands;
        }

        private List<Command> createAllInvalidCommands() {
            List<Command> commands = new ArrayList<>();
            Command invalid_Command1 = new Command(UserProfileFixtures.USER_ID, "invalid", UserProfileFixtures.EXISTING_PROPERTY_REPLACE);
            Command invalid_Command2 = new Command(UserProfileFixtures.USER_ID, "invalid", UserProfileFixtures.EXISTING_PROPERTY_INCREMENT_POSITIVE);
            commands.add(invalid_Command1);
            commands.add(invalid_Command2);
            return commands;
        }

        private List<Command> createSomeInvalidCommands() {
            List<Command> commands = new ArrayList<>();
            Command replace_Command = new Command(UserProfileFixtures.USER_ID, "replace", UserProfileFixtures.EXISTING_PROPERTY_REPLACE);
            Command invalid_Command = new Command(UserProfileFixtures.USER_ID, "invalid", UserProfileFixtures.EXISTING_PROPERTY_INCREMENT_POSITIVE);
            commands.add(replace_Command);
            commands.add(invalid_Command);
            return commands;
        }

    }
}