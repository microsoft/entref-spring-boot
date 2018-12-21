package com.microsoft.cse.reference.spring.dal.integration;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Note: we do integration tests for the endpoints, as unit tests aren't feasible
 * https://stackoverflow.com/questions/23435937/how-to-test-spring-data-repositories
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = BasicRouteTests.Config.class)
public class BasicRouteTests {
    @Autowired
    TestRestTemplate rest;

    @LocalServerPort
    Integer httpPort;

    @Test
    public void ValidateTitlesEndpoint() throws JSONException {
        String obj = this.rest.getForObject("http://localhost:" + httpPort + "/titles", String.class);

        String raw = "{\n" +
                "  \"_embedded\" : {\n" +
                "    \"titles\" : [ {\n" +
                "      \"tconst\" : \"tt0000003\",\n" +
                "      \"titleType\" : \"short\",\n" +
                "      \"primaryTitle\" : \"Pauvre Pierrot\",\n" +
                "      \"originalTitle\" : \"Pauvre Pierrot\",\n" +
                "      \"isAdult\" : false,\n" +
                "      \"startYear\" : 1892,\n" +
                "      \"endYear\" : null,\n" +
                "      \"runtimeMinutes\" : 4,\n" +
                "      \"genres\" : [ \"Animation\", \"Comedy\", \"Romance\" ],\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:8080/titles/tt0000003\"\n" +
                "        },\n" +
                "        \"title\" : {\n" +
                "          \"href\" : \"http://localhost:8080/titles/tt0000003\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"tconst\" : \"tt0000192\",\n" +
                "      \"titleType\" : \"short\",\n" +
                "      \"primaryTitle\" : \"Ella Lola, a la Trilby\",\n" +
                "      \"originalTitle\" : \"Ella Lola, a la Trilby\",\n" +
                "      \"isAdult\" : false,\n" +
                "      \"startYear\" : 1898,\n" +
                "      \"endYear\" : null,\n" +
                "      \"runtimeMinutes\" : null,\n" +
                "      \"genres\" : [ \"Short\" ],\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:8080/titles/tt0000192\"\n" +
                "        },\n" +
                "        \"title\" : {\n" +
                "          \"href\" : \"http://localhost:8080/titles/tt0000192\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"tconst\" : \"tt0001022\",\n" +
                "      \"titleType\" : \"short\",\n" +
                "      \"primaryTitle\" : \"A Rose of the Tenderloin\",\n" +
                "      \"originalTitle\" : \"A Rose of the Tenderloin\",\n" +
                "      \"isAdult\" : false,\n" +
                "      \"startYear\" : 1909,\n" +
                "      \"endYear\" : null,\n" +
                "      \"runtimeMinutes\" : null,\n" +
                "      \"genres\" : [ \"Drama\", \"Short\" ],\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:8080/titles/tt0001022\"\n" +
                "        },\n" +
                "        \"title\" : {\n" +
                "          \"href\" : \"http://localhost:8080/titles/tt0001022\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"tconst\" : \"tt0001008\",\n" +
                "      \"titleType\" : \"short\",\n" +
                "      \"primaryTitle\" : \"The Prince and the Pauper\",\n" +
                "      \"originalTitle\" : \"The Prince and the Pauper\",\n" +
                "      \"isAdult\" : false,\n" +
                "      \"startYear\" : 1909,\n" +
                "      \"endYear\" : null,\n" +
                "      \"runtimeMinutes\" : null,\n" +
                "      \"genres\" : [ \"Short\" ],\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:8080/titles/tt0001008\"\n" +
                "        },\n" +
                "        \"title\" : {\n" +
                "          \"href\" : \"http://localhost:8080/titles/tt0001008\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"tconst\" : \"tt0075472\",\n" +
                "      \"titleType\" : \"tvSeries\",\n" +
                "      \"primaryTitle\" : \"All Creatures Great and Small\",\n" +
                "      \"originalTitle\" : \"All Creatures Great and Small\",\n" +
                "      \"isAdult\" : false,\n" +
                "      \"startYear\" : 1978,\n" +
                "      \"endYear\" : 1990,\n" +
                "      \"runtimeMinutes\" : 50,\n" +
                "      \"genres\" : [ \"Comedy\", \"Drama\" ],\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:8080/titles/tt0075472\"\n" +
                "        },\n" +
                "        \"title\" : {\n" +
                "          \"href\" : \"http://localhost:8080/titles/tt0075472\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"tconst\": \"tt0000698\",\n" +
                "      \"titleType\": \"short\",\n" +
                "      \"primaryTitle\": \"The Heart of O Yama\",\n" +
                "      \"originalTitle\": \"The Heart of O Yama\",\n" +
                "      \"isAdult\": false,\n" +
                "      \"startYear\": 1908,\n" +
                "      \"endYear\": null,\n" +
                "      \"runtimeMinutes\": 15,\n" +
                "      \"genres\": [ \"Drama\", \"Romance\", \"Short\" ],\n" +
                "      \"_links\": {\n" +
                "        \"self\": {\n" +
                "          \"href\": \"http://localhost:8080/titles/tt0000698\"\n" +
                "        },\n" +
                "        \"title\": {\n" +
                "          \"href\": \"http://localhost:8080/titles/tt0000698\"\n" +
                "        }\n" +
                "      }\n" +
                "    } ]" +
                "  }, " +
                "  \"_links\" : {\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"http://localhost:8080/titles{?page,size,sort}\",\n" +
                "      \"templated\" : true\n" +
                "    },\n" +
                "    \"profile\" : {\n" +
                "      \"href\" : \"http://localhost:8080/profile/titles\"\n" +
                "    },\n" +
                "    \"search\" : {\n" +
                "      \"href\" : \"http://localhost:8080/titles/search\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"page\" : {\n" +
                "    \"size\" : 20,\n" +
                "    \"totalElements\" : 6,\n" +
                "    \"totalPages\" : 1,\n" +
                "    \"number\" : 0\n" +
                "  }\n" +
                "}";

        raw = raw.replace("localhost:8080/", "localhost:" + httpPort + "/");

        JSONObject expected = new JSONObject(raw);
        JSONAssert.assertEquals(expected, new JSONObject(obj), true);
    }

    @Test
    public void ValidatePeopleEndpoint() throws JSONException {
        String obj = this.rest.getForObject("http://localhost:" + httpPort + "/people", String.class);

        String raw = "{\n" +
                "  \"_embedded\" : {\n" +
                "    \"persons\" : [ {\n" +
                "      \"nconst\" : \"nm0000496\",\n" +
                "      \"primaryName\" : \"Juliette Lewis\",\n" +
                "      \"birthYear\" : 1973,\n" +
                "      \"deathYear\" : null,\n" +
                "      \"primaryProfession\" : [ \"actress\", \"soundtrack\", \"director\" ],\n" +
                "      \"knownForTitles\" : [ \"tt0116367\", \"tt1322269\", \"tt0110632\", \"tt0101540\" ],\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:8080/people/nm0000496\"\n" +
                "        },\n" +
                "        \"person\" : {\n" +
                "          \"href\" : \"http://localhost:8080/people/nm0000496\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"nconst\" : \"nm0000342\",\n" +
                "      \"primaryName\" : \"James Cromwell\",\n" +
                "      \"birthYear\" : 1940,\n" +
                "      \"deathYear\" : null,\n" +
                "      \"primaryProfession\" : [ \"actor\", \"producer\", \"soundtrack\" ],\n" +
                "      \"knownForTitles\" : [ \"tt0112431\", \"tt0120689\", \"tt2245084\", \"tt0119488\" ],\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:8080/people/nm0000342\"\n" +
                "        },\n" +
                "        \"person\" : {\n" +
                "          \"href\" : \"http://localhost:8080/people/nm0000342\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"nconst\" : \"nm0000230\",\n" +
                "      \"primaryName\" : \"Sylvester Stallone\",\n" +
                "      \"birthYear\" : 1946,\n" +
                "      \"deathYear\" : null,\n" +
                "      \"primaryProfession\" : [ \"actor\", \"writer\", \"producer\" ],\n" +
                "      \"knownForTitles\" : [ \"tt3076658\", \"tt0089927\", \"tt0084602\", \"tt0075148\" ],\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:8080/people/nm0000230\"\n" +
                "        },\n" +
                "        \"person\" : {\n" +
                "          \"href\" : \"http://localhost:8080/people/nm0000230\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"nconst\" : \"nm0001500\",\n" +
                "      \"primaryName\" : \"Karl Malden\",\n" +
                "      \"birthYear\" : 1912,\n" +
                "      \"deathYear\" : 2009,\n" +
                "      \"primaryProfession\" : [ \"actor\", \"soundtrack\", \"director\" ],\n" +
                "      \"knownForTitles\" : [ \"tt0047296\", \"tt0066206\", \"tt0048973\", \"tt0044081\" ],\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:8080/people/nm0001500\"\n" +
                "        },\n" +
                "        \"person\" : {\n" +
                "          \"href\" : \"http://localhost:8080/people/nm0001500\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"nconst\" : \"nm0000548\",\n" +
                "      \"primaryName\" : \"Elizabeth Montgomery\",\n" +
                "      \"birthYear\" : 1933,\n" +
                "      \"deathYear\" : 1995,\n" +
                "      \"primaryProfession\" : [ \"actress\", \"soundtrack\", \"miscellaneous\" ],\n" +
                "      \"knownForTitles\" : [ \"tt0076981\", \"tt0088713\", \"tt0057733\", \"tt0073273\" ],\n" +
                "      \"_links\" : {\n" +
                "        \"self\" : {\n" +
                "          \"href\" : \"http://localhost:8080/people/nm0000548\"\n" +
                "        },\n" +
                "        \"person\" : {\n" +
                "          \"href\" : \"http://localhost:8080/people/nm0000548\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"nconst\": \"nm0000428\",\n" +
                "      \"primaryName\": \"D.W. Griffith\",\n" +
                "      \"birthYear\": 1875,\n" +
                "      \"deathYear\": 1948,\n" +
                "      \"primaryProfession\": [ \"director\", \"writer\", \"producer\" ],\n" +
                "      \"knownForTitles\": [ \"tt0006864\", \"tt0010484\", \"tt0004972\", \"tt0012532\" ],\n" +
                "      \"_links\": {\n" +
                "        \"self\": {\n" +
                "          \"href\": \"http://localhost:8080/people/nm0000428\"\n" +
                "        },\n" +
                "        \"person\": {\n" +
                "          \"href\": \"http://localhost:8080/people/nm0000428\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"nconst\": \"nm0492757\",\n" +
                "      \"primaryName\": \"Florence Lawrence\",\n" +
                "      \"birthYear\": 1886,\n" +
                "      \"deathYear\": 1938,\n" +
                "      \"primaryProfession\": [\n \"actress\" ],\n" +
                "      \"knownForTitles\": [ \"tt0143358\", \"tt0200577\", \"tt0000770\", \"tt0200909\" ],\n" +
                "      \"_links\": {\n" +
                "        \"self\": {\n" +
                "          \"href\": \"http://localhost:8080/people/nm0492757\"\n" +
                "        },\n" +
                "        \"person\": {\n" +
                "          \"href\": \"http://localhost:8080/people/nm0492757\"\n" +
                "        }\n" +
                "      }\n" +
                "    }, {\n" +
                "      \"nconst\": \"nm0555522\",\n" +
                "      \"primaryName\": \"Arthur Marvin\",\n" +
                "      \"birthYear\": 1859,\n" +
                "      \"deathYear\": 1911,\n" +
                "      \"primaryProfession\": [ \"cinematographer\", \"director\", \"camera_department\" ],\n" +
                "      \"knownForTitles\": [ \"tt0291476\", \"tt0000412\", \"tt0233612\", \"tt0300052\" ],\n" +
                "      \"_links\": {\n" +
                "        \"self\": {\n" +
                "          \"href\": \"http://localhost:8080/people/nm0555522\"\n" +
                "        },\n" +
                "        \"person\": {\n" +
                "          \"href\": \"http://localhost:8080/people/nm0555522\"\n" +
                "        }\n" +
                "      }\n" +
                "    }" +
                "  ]\n" +
                "},\n" +
                "  \"_links\" : {\n" +
                "    \"self\" : {\n" +
                "      \"href\" : \"http://localhost:8080/people{?page,size,sort}\",\n" +
                "      \"templated\" : true\n" +
                "    },\n" +
                "    \"profile\" : {\n" +
                "      \"href\" : \"http://localhost:8080/profile/people\"\n" +
                "    },\n" +
                "    \"search\" : {\n" +
                "      \"href\" : \"http://localhost:8080/people/search\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"page\" : {\n" +
                "    \"size\" : 20,\n" +
                "    \"totalElements\" : 8,\n" +
                "    \"totalPages\" : 1,\n" +
                "    \"number\" : 0\n" +
                "  }\n" +
                "}";
        raw = raw.replace("localhost:8080/", "localhost:" + httpPort + "/");

        JSONObject expected = new JSONObject(raw);
        JSONAssert.assertEquals(expected, new JSONObject(obj), true);
    }

    @Test
    public void CORS_Success_TitlesEndpointResponse() throws URISyntaxException {

        ResponseEntity<String> resTitles = this.rest.exchange(RequestEntity
                        .get(new URI("http://localhost:" + httpPort + "/titles"))
                        .header(HttpHeaders.ORIGIN, "http://test.com")
                        .build(),
                String.class
        );

        Assert.assertTrue(resTitles.getStatusCode().is2xxSuccessful());
        Assert.assertEquals(resTitles.getHeaders().getAccessControlAllowOrigin(), "*");
    }


    @Test
    public void CORS_Success_PeopleEndpointResponse() throws URISyntaxException {

        ResponseEntity<String> resPeople = this.rest.exchange(RequestEntity
                        .get(new URI("http://localhost:" + httpPort + "/people"))
                        .header(HttpHeaders.ORIGIN, "http://test.com")
                        .build(),
                String.class
        );

        Assert.assertTrue(resPeople.getStatusCode().is2xxSuccessful());
        Assert.assertEquals(resPeople.getHeaders().getAccessControlAllowOrigin(), "*");
    }


    @Test
    public void ValidateHealthEndpoint() throws URISyntaxException {
        ResponseEntity<String> resHealth = this.rest.exchange(RequestEntity
                        .get(new URI("http://localhost:" + httpPort + "/health"))
                        .build(),
                String.class
        );

        Assert.assertTrue(resHealth.getStatusCode().is2xxSuccessful());
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