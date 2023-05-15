package com.spotlight.platform.userprofile.api.core.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotlight.platform.helpers.FixtureHelpers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JsonMapperTest {
    private static final ObjectMapper JSON_MAPPER = JsonMapper.getInstance();

    private static final String FIXTURE_PATH = "/fixtures/core/json/testEntityWithInstantsIsoString.json";

    @Test
    void serializeInstants_CorrectlySerialized() throws Exception {
        var serializedEntity = JSON_MAPPER.writeValueAsString(new TestEntityWithInstants());

        assertThat(serializedEntity).isEqualToIgnoringWhitespace(FixtureHelpers.fixture(FIXTURE_PATH));
    }

    @Test
    void deserializeInstants_CorrectlyDeserialized() throws Exception {
        var deserializedEntity = JSON_MAPPER.readValue(FixtureHelpers.fixture(FIXTURE_PATH), TestEntityWithInstants.class);

        assertThat(deserializedEntity).usingRecursiveComparison().isEqualTo(new TestEntityWithInstants());
    }
}