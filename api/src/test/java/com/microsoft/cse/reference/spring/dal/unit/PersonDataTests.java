package com.microsoft.cse.reference.spring.dal.unit;

import com.microsoft.cse.reference.spring.dal.config.Constants;
import com.microsoft.cse.reference.spring.dal.config.DevelopmentConfig;
import com.microsoft.cse.reference.spring.dal.config.MongoConfig;
import com.microsoft.cse.reference.spring.dal.controllers.PersonRepository;
import com.microsoft.cse.reference.spring.dal.converters.BooleanToInteger;
import com.microsoft.cse.reference.spring.dal.converters.IntegerToBoolean;
import com.microsoft.cse.reference.spring.dal.models.Person;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
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
public class PersonDataTests {
    @Autowired
    public MongoTemplate mongoTemplate;

    @Autowired
    public PersonRepository repo;

    @Before
    public void setUp() {
        this.mongoTemplate.dropCollection(Constants.DB_PERSON_COLLECTION);

        this.mongoTemplate.insert(Document.parse("{\n" +
                "    \"nconst\" : \"nm0001500\",\n" +
                "    \"primaryName\" : \"Karl Malden\",\n" +
                "    \"birthYear\" : 1912,\n" +
                "    \"deathYear\" : 2009,\n" +
                "    \"primaryProfession\" : \"actor,soundtrack,director\",\n" +
                "    \"knownForTitles\" : \"tt0047296,tt0066206,tt0048973,tt0044081\"\n" +
                "  },"), Constants.DB_PERSON_COLLECTION);
    }

    @Test
    public void findById_Success() {
        Person actual = this.repo.findById("nm0001500").get();

        assertThat(actual.nconst, is("nm0001500"));
        assertThat(actual.primaryName, is("Karl Malden"));
        assertThat(actual.birthYear, is(1912));
        assertThat(actual.deathYear, is(2009));
        assertThat(actual.primaryProfession, is(Arrays.asList("actor", "soundtrack", "director")));
        assertThat(actual.knownForTitles, is(Arrays.asList("tt0047296", "tt0066206", "tt0048973", "tt0044081")));
    }

    @Test
    public void findById_Failure() {
        Optional<Person> actual = this.repo.findById("not-real");

        assertThat(actual.isPresent(), is(false));
    }

    @Test
    public void findByPrimaryName_Success() {
        List<Person> actuals = this.repo.findByPrimaryName("Karl Malden");

        assertThat(actuals.size(), is(1));

        Person actual = actuals.get(0);

        assertThat(actual.nconst, is("nm0001500"));
        assertThat(actual.primaryName, is("Karl Malden"));
        assertThat(actual.birthYear, is(1912));
        assertThat(actual.deathYear, is(2009));
        assertThat(actual.primaryProfession, is(Arrays.asList("actor", "soundtrack", "director")));
        assertThat(actual.knownForTitles, is(Arrays.asList("tt0047296", "tt0066206", "tt0048973", "tt0044081")));
    }

    @Test
    public void findByPrimaryName_Failure() {
        List<Person> actuals = this.repo.findByPrimaryName("not real");

        assertThat(actuals.size(), is(0));
    }

    @Test
    public void deleteAll_Success() {
        this.repo.deleteAll();

        assertThat(this.repo.count(), is(0L));
    }

    @Test
    public void deleteById_Success() {
        this.repo.deleteById("nm0001500");

        assertThat(this.repo.count(), is(0L));
    }

    @Test
    public void insert_Success() {
        String id = "nm0000001";
        Person newPerson = new Person();
        newPerson.nconst = id;
        newPerson.birthYear = 2020;
        newPerson.primaryName = "Tim Tam";
        newPerson.primaryProfession = Arrays.asList("Test", "Dancer");

        assertThat(this.repo.insert(newPerson), is(newPerson));
        assertThat(this.repo.count(), is(2L));

        Person actual = this.repo.findById(id).get();

        assertThat(actual.nconst, is(id));
        assertThat(actual.primaryName, is("Tim Tam"));
        assertThat(actual.birthYear, is(2020));
        assertThat(actual.deathYear, is(nullValue()));
        assertThat(actual.primaryProfession, is(Arrays.asList("Test", "Dancer")));
        assertThat(actual.knownForTitles, is(nullValue()));
    }

    @Test
    public void update_Success() {
        Person update = this.repo.findById("nm0001500").get();

        update.deathYear = 2010;
        update.primaryProfession.add("Test");

        assertThat(this.repo.save(update), is(update));

        Person actual = this.repo.findById("nm0001500").get();
        assertThat(actual.nconst, is("nm0001500"));
        assertThat(actual.primaryName, is("Karl Malden"));
        assertThat(actual.birthYear, is(1912));
        assertThat(actual.deathYear, is(2010));
        assertThat(actual.primaryProfession, is(Arrays.asList("actor", "soundtrack", "director", "Test")));
        assertThat(actual.knownForTitles, is(Arrays.asList("tt0047296", "tt0066206", "tt0048973", "tt0044081")));
    }
}
