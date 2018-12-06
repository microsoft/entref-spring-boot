package com.microsoft.cse.reference.spring.dal.integration;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = CustomRouteTests.Config.class)
public class CustomRouteTests {
    @Autowired
    TestRestTemplate rest;

    @LocalServerPort
    Integer httpPort;

    @Test
    public void ValidatePeopleFromTitlesEndpoint() throws JSONException {
        String obj = this.rest.getForObject("http://localhost:" + httpPort + "/titles/tt0000698/people", String.class);

        String raw = "[\n" +
                "    {\n" +
                "        \"tconst\": \"tt0000698\",\n" +
                "        \"ordering\": 3,\n" +
                "        \"person\": {\n" +
                "            \"nconst\": \"nm0000428\",\n" +
                "            \"primaryName\": \"D.W. Griffith\",\n" +
                "            \"birthYear\": 1875,\n" +
                "            \"deathYear\": 1948,\n" +
                "            \"primaryProfession\": \"director,writer,producer\",\n" +
                "            \"knownForTitles\": \"tt0006864,tt0010484,tt0004972,tt0012532\"\n" +
                "        },\n" +
                "        \"category\": \"actor\",\n" +
                "        \"job\": null,\n" +
                "        \"characters\": [\n" +
                "            \"Footman\"\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"tconst\": \"tt0000698\",\n" +
                "        \"ordering\": 1,\n" +
                "        \"person\": {\n" +
                "            \"nconst\": \"nm0492757\",\n" +
                "            \"primaryName\": \"Florence Lawrence\",\n" +
                "            \"birthYear\": 1886,\n" +
                "            \"deathYear\": 1938,\n" +
                "            \"primaryProfession\": \"actress\",\n" +
                "            \"knownForTitles\": \"tt0143358,tt0200577,tt0000770,tt0200909\"\n" +
                "        },\n" +
                "        \"category\": \"actress\",\n" +
                "        \"job\": null,\n" +
                "        \"characters\": [\n" +
                "            \"O'Yama\"\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"tconst\": \"tt0000698\",\n" +
                "        \"ordering\": 6,\n" +
                "        \"person\": {\n" +
                "            \"nconst\": \"nm0555522\",\n" +
                "            \"primaryName\": \"Arthur Marvin\",\n" +
                "            \"birthYear\": 1859,\n" +
                "            \"deathYear\": 1911,\n" +
                "            \"primaryProfession\": \"cinematographer,director,camera_department\",\n" +
                "            \"knownForTitles\": \"tt0291476,tt0000412,tt0233612,tt0300052\"\n" +
                "        },\n" +
                "        \"category\": \"cinematographer\",\n" +
                "        \"job\": null,\n" +
                "        \"characters\": []\n" +
                "    }\n" +
                "]";

        JSONArray expected = new JSONArray(raw);
        JSONAssert.assertEquals(expected, new JSONArray(obj), true);
    }

    @Test
    public void ValidateCastFromTitlesEndpoint() throws JSONException {
        String obj = this.rest.getForObject("http://localhost:" + httpPort + "/titles/tt0000698/cast", String.class);

        String raw = "[\n" +
                "    {\n" +
                "        \"tconst\": \"tt0000698\",\n" +
                "        \"ordering\": 3,\n" +
                "        \"person\": {\n" +
                "            \"nconst\": \"nm0000428\",\n" +
                "            \"primaryName\": \"D.W. Griffith\",\n" +
                "            \"birthYear\": 1875,\n" +
                "            \"deathYear\": 1948,\n" +
                "            \"primaryProfession\": \"director,writer,producer\",\n" +
                "            \"knownForTitles\": \"tt0006864,tt0010484,tt0004972,tt0012532\"\n" +
                "        },\n" +
                "        \"category\": \"actor\",\n" +
                "        \"job\": null,\n" +
                "        \"characters\": [\n" +
                "            \"Footman\"\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"tconst\": \"tt0000698\",\n" +
                "        \"ordering\": 1,\n" +
                "        \"person\": {\n" +
                "            \"nconst\": \"nm0492757\",\n" +
                "            \"primaryName\": \"Florence Lawrence\",\n" +
                "            \"birthYear\": 1886,\n" +
                "            \"deathYear\": 1938,\n" +
                "            \"primaryProfession\": \"actress\",\n" +
                "            \"knownForTitles\": \"tt0143358,tt0200577,tt0000770,tt0200909\"\n" +
                "        },\n" +
                "        \"category\": \"actress\",\n" +
                "        \"job\": null,\n" +
                "        \"characters\": [\n" +
                "            \"O'Yama\"\n" +
                "        ]\n" +
                "    }\n" +
                "]";

        JSONArray expected = new JSONArray(raw);
        JSONAssert.assertEquals(expected, new JSONArray(obj), true);
    }

    @Test
    public void ValidateCrewFromTitlesEndpoint() throws JSONException {
        String obj = this.rest.getForObject("http://localhost:" + httpPort + "/titles/tt0000698/crew", String.class);

        String raw = "[\n" +
                "    {\n" +
                "        \"tconst\": \"tt0000698\",\n" +
                "        \"ordering\": 6,\n" +
                "        \"person\": {\n" +
                "            \"nconst\": \"nm0555522\",\n" +
                "            \"primaryName\": \"Arthur Marvin\",\n" +
                "            \"birthYear\": 1859,\n" +
                "            \"deathYear\": 1911,\n" +
                "            \"primaryProfession\": \"cinematographer,director,camera_department\",\n" +
                "            \"knownForTitles\": \"tt0291476,tt0000412,tt0233612,tt0300052\"\n" +
                "        },\n" +
                "        \"category\": \"cinematographer\",\n" +
                "        \"job\": null,\n" +
                "        \"characters\": []\n" +
                "    }\n" +
                "]";

        JSONArray expected = new JSONArray(raw);
        JSONAssert.assertEquals(expected, new JSONArray(obj), true);
    }

    @Test
    public void ValidateTitlesFromPeopleEndpoint() throws JSONException {
        String obj = this.rest.getForObject("http://localhost:" + httpPort + "/people/nm0000428/titles", String.class);

        String raw = "[\n" +
                "    {\n" +
                "        \"tconst\": \"tt0000698\",\n" +
                "        \"titleType\": \"short\",\n" +
                "        \"primaryTitle\": \"The Heart of O Yama\",\n" +
                "        \"originalTitle\": \"The Heart of O Yama\",\n" +
                "        \"isAdult\": false,\n" +
                "        \"startYear\": 1908,\n" +
                "        \"endYear\": null,\n" +
                "        \"runtimeMinutes\": 15,\n" +
                "        \"genres\": [\n" +
                "            \"Drama\",\n" +
                "            \"Romance\",\n" +
                "            \"Short\"\n" +
                "        ]\n" +
                "    }\n" +
                "]";

        JSONArray expected = new JSONArray(raw);
        JSONAssert.assertEquals(expected, new JSONArray(obj), true);
    }

    /**
     * A configuration instance for these tests
     */
    public static class Config extends PropertyMockingApplicationContextInitializer {
        @Override
        protected String getAllowedOrigin() {
            // we wish to allow * with CORS for the tests above
            return "*";
        }
    }
}
