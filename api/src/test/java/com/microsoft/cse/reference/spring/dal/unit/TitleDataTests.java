package com.microsoft.cse.reference.spring.dal.unit;

import com.microsoft.cse.reference.spring.dal.config.Constants;
import com.microsoft.cse.reference.spring.dal.config.DevelopmentConfig;
import com.microsoft.cse.reference.spring.dal.config.MongoConfig;
import com.microsoft.cse.reference.spring.dal.controllers.TitleRepository;
import com.microsoft.cse.reference.spring.dal.converters.BooleanToInteger;
import com.microsoft.cse.reference.spring.dal.converters.IntegerToBoolean;
import com.microsoft.cse.reference.spring.dal.models.Title;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Define the tests using the built-in DataMongoTest attribute
 * However, since the builtin doesn't load other beans, we need to load
 * the converters, and the config that loads the converters - we do that with @Import
 */
@RunWith(SpringRunner.class)
@DataMongoTest
@EnableWebSecurity
@EnableResourceServer
@Import({IntegerToBoolean.class,
        BooleanToInteger.class,
        MongoConfig.class,
        DevelopmentConfig.class})
public class TitleDataTests {
    @Autowired
    public MongoTemplate mongoTemplate;

    @Autowired
    public TitleRepository repo;

    @Before
    public void setUp() {
        this.mongoTemplate.dropCollection(Constants.DB_TITLE_COLLECTION);

        this.mongoTemplate.insert(Document.parse("{\n" +
                "    \"tconst\" : \"tt0075472\",\n" +
                "    \"titleType\" : \"tvSeries\",\n" +
                "    \"primaryTitle\" : \"All Creatures Great and Small\",\n" +
                "    \"originalTitle\" : \"All Creatures Great and Small\",\n" +
                "    \"isAdult\" : 0,\n" +
                "    \"startYear\" : 1978,\n" +
                "    \"endYear\" : 1990,\n" +
                "    \"runtimeMinutes\" : 50,\n" +
                "    \"genres\" : \"Comedy,Drama\"\n" +
                "  }"), Constants.DB_TITLE_COLLECTION);
    }

    @Test
    public void findById_Success() {
        Title actual = this.repo.findById("tt0075472").get();

        assertThat(actual.tconst, is("tt0075472"));
        assertThat(actual.titleType, is("tvSeries"));
        assertThat(actual.primaryTitle, is("All Creatures Great and Small"));
        assertThat(actual.originalTitle, is("All Creatures Great and Small"));
        assertThat(actual.isAdult, is(false));
        assertThat(actual.startYear, is(1978));
        assertThat(actual.endYear, is(1990));
        assertThat(actual.runtimeMinutes, is(50));
        assertThat(actual.genres, is(Arrays.asList("Comedy", "Drama")));
    }

    @Test
    public void findById_Failure() {
        Optional<Title> actual = this.repo.findById("not-real");

        assertThat(actual.isPresent(), is(false));
    }

    @Test
    public void findByPrimaryTitle_Success() {
        List<Title> actuals = this.repo.findByPrimaryTitle("All Creatures Great and Small");

        assertThat(actuals.size(), is(1));

        Title actual = actuals.get(0);

        assertThat(actual.tconst, is("tt0075472"));
        assertThat(actual.titleType, is("tvSeries"));
        assertThat(actual.primaryTitle, is("All Creatures Great and Small"));
        assertThat(actual.originalTitle, is("All Creatures Great and Small"));
        assertThat(actual.isAdult, is(false));
        assertThat(actual.startYear, is(1978));
        assertThat(actual.endYear, is(1990));
        assertThat(actual.runtimeMinutes, is(50));
        assertThat(actual.genres, is(Arrays.asList("Comedy", "Drama")));
    }

    @Test
    public void findByPrimaryTitle_Failure() {
        List<Title> actuals = this.repo.findByPrimaryTitle("not real");

        assertThat(actuals.size(), is(0));
    }

    @Test
    public void deleteAll_Success() {
        this.repo.deleteAll();

        assertThat(this.repo.count(), is(0L));
    }

    @Test
    public void deleteById_Success() {
        this.repo.deleteById("tt0075472");

        assertThat(this.repo.count(), is(0L));
    }

    @Test
    public void insert_Success() {
        String id = "tt0000001";
        Title newTitle = new Title();
        newTitle.tconst = id;
        newTitle.originalTitle = "Test Movie";
        newTitle.isAdult = true;
        newTitle.genres = Arrays.asList("Comedy", "Horror");

        assertThat(this.repo.insert(newTitle), is(newTitle));
        assertThat(this.repo.count(), is(2L));

        Title actual = this.repo.findById(id).get();

        assertThat(actual.tconst, is(id));
        assertThat(actual.titleType, is(nullValue()));
        assertThat(actual.primaryTitle, is(nullValue()));
        assertThat(actual.originalTitle, is("Test Movie"));
        assertThat(actual.isAdult, is(true));
        assertThat(actual.startYear, is(nullValue()));
        assertThat(actual.endYear, is(nullValue()));
        assertThat(actual.runtimeMinutes, is(nullValue()));
        assertThat(actual.genres, is(Arrays.asList("Comedy", "Horror")));
    }

    @Test
    public void update_Success() {
        Title update = this.repo.findById("tt0075472").get();

        update.isAdult = true;
        update.genres.add("Test");

        assertThat(this.repo.save(update), is(update));

        Title actual = this.repo.findById("tt0075472").get();
        assertThat(actual.tconst, is("tt0075472"));
        assertThat(actual.titleType, is("tvSeries"));
        assertThat(actual.primaryTitle, is("All Creatures Great and Small"));
        assertThat(actual.originalTitle, is("All Creatures Great and Small"));
        assertThat(actual.isAdult, is(true));
        assertThat(actual.startYear, is(1978));
        assertThat(actual.endYear, is(1990));
        assertThat(actual.runtimeMinutes, is(50));
        assertThat(actual.genres, is(Arrays.asList("Comedy", "Drama", "Test")));
    }
}
