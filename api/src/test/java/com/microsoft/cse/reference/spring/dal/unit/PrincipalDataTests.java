package com.microsoft.cse.reference.spring.dal.unit;

import com.microsoft.cse.reference.spring.dal.config.Constants;
import com.microsoft.cse.reference.spring.dal.config.DevelopmentConfig;
import com.microsoft.cse.reference.spring.dal.config.MongoConfig;
import com.microsoft.cse.reference.spring.dal.controllers.PrincipalRepository;
import com.microsoft.cse.reference.spring.dal.converters.*;
import com.microsoft.cse.reference.spring.dal.models.Principal;
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
@Import({EmptyStringToNull.class,
        NullToEmptyString.class,
        JsonArrayToStringList.class,
        MongoConfig.class,
        DevelopmentConfig.class})
public class PrincipalDataTests {
    @Autowired
    public MongoTemplate mongoTemplate;

    @Autowired
    public PrincipalRepository repo;

    @Before
    public void setUp() {
        this.mongoTemplate.dropCollection(Constants.DB_PRINCIPAL_COLLECTION);

        this.mongoTemplate.insert(Document.parse("{\n" +
                "    \"tconst\" : \"tt0000442\",\n" +
                "    \"ordering\" : 1,\n" +
                "    \"nconst\" : \"nm0622273\",\n" +
                "    \"category\" : \"actress\",\n" +
                "    \"job\" : \"\",\n" +
                "    \"characters\" : \"[\\\"Barnemordersken\\\"]\"\n" +
                "  },"), Constants.DB_PRINCIPAL_COLLECTION);
    }

    @Test
    public void storageOperation_Success() {
        // we aren't really testing findAll here, we're testing that
        // our model inflates as expected - hence the name storageOperation
        Principal actual = this.repo.findAll().get(0);

        assertThat(actual.tconst, is("tt0000442"));
        assertThat(actual.ordering, is(1));
        assertThat(actual.nconst, is("nm0622273"));
        assertThat(actual.category, is("actress"));
        assertThat(actual.job, is(nullValue()));
        assertThat(actual.characters, is(Arrays.asList("Barnemordersken")));
    }

    @Test
    public void findById_Failure() {
        Optional<Principal> actual = this.repo.findById("not-real");

        assertThat(actual.isPresent(), is(false));
    }

    @Test
    public void findByNconst_Success() {
        List<Principal> actuals = this.repo.findByNconst("nm0622273");

        assertThat(actuals.size(), is(1));

        Principal actual = actuals.get(0);

        assertThat(actual.tconst, is("tt0000442"));
        assertThat(actual.ordering, is(1));
        assertThat(actual.nconst, is("nm0622273"));
        assertThat(actual.category, is("actress"));
        assertThat(actual.job, is(nullValue()));
        assertThat(actual.characters, is(Arrays.asList("Barnemordersken")));
    }

    @Test
    public void findByNconst_Failure() {
        List<Principal> actuals = this.repo.findByNconst("not real");

        assertThat(actuals.size(), is(0));
    }

    @Test
    public void findByTconst_Success() {
        List<Principal> actuals = this.repo.findByTconst("tt0000442");

        assertThat(actuals.size(), is(1));

        Principal actual = actuals.get(0);

        assertThat(actual.tconst, is("tt0000442"));
        assertThat(actual.ordering, is(1));
        assertThat(actual.nconst, is("nm0622273"));
        assertThat(actual.category, is("actress"));
        assertThat(actual.job, is(nullValue()));
        assertThat(actual.characters, is(Arrays.asList("Barnemordersken")));
    }

    @Test
    public void findByTconst_Failure() {
        List<Principal> actuals = this.repo.findByTconst("not real");

        assertThat(actuals.size(), is(0));
    }

    @Test
    public void deleteAll_Success() {
        this.repo.deleteAll();

        assertThat(this.repo.count(), is(0L));
    }

    @Test
    public void insert_Success() {
        String id = "tt0000001";
        Principal newPrincipal = new Principal();
        newPrincipal.tconst = id;
        newPrincipal.nconst = "nm0000001";

        assertThat(this.repo.insert(newPrincipal), is(newPrincipal));
        assertThat(this.repo.count(), is(2L));

        Principal actual = this.repo.findAll().get(1);

        assertThat(actual.tconst, is("tt0000001"));
        assertThat(actual.ordering, is(nullValue()));
        assertThat(actual.nconst, is("nm0000001"));
        assertThat(actual.category, is(nullValue()));
        assertThat(actual.job, is(nullValue()));
        assertThat(actual.characters, is(nullValue()));
    }

    @Test
    public void update_Success() {
        Principal update = this.repo.findAll().get(0);

        update.job = "Test";
        update.characters.add("Test");

        assertThat(this.repo.save(update), is(update));

        Principal actual = this.repo.findAll().get(0);
        assertThat(actual.tconst, is("tt0000442"));
        assertThat(actual.ordering, is(1));
        assertThat(actual.nconst, is("nm0622273"));
        assertThat(actual.category, is("actress"));
        assertThat(actual.job, is("Test"));
        assertThat(actual.characters, is(Arrays.asList("Barnemordersken", "Test")));
    }
}
