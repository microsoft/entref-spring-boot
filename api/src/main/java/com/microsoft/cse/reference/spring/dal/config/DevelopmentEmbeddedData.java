package com.microsoft.cse.reference.spring.dal.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Responsible for populating test data in development deployments
 */
@Component
@Profile({"default", "development"})
public class DevelopmentEmbeddedData {
    @Autowired
    MongoTemplate mongoInterface;

    Logger logger = LoggerFactory.getLogger(MongoConfig.class);

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            // load all the testdata sets into their given collections
            mongoInterface.insert(parseTestData("testdata/titles.testdata.json"), Constants.DB_TITLE_COLLECTION);
            mongoInterface.insert(parseTestData("testdata/names.testdata.json"), Constants.DB_PERSON_COLLECTION);
            mongoInterface.insert(parseTestData("testdata/principals_mapping.testdata.json"), Constants.DB_PRINCIPAL_COLLECTION);

            logger.info(Constants.STATUS_TEST_DATA_USED);
        } catch (IOException e) {
            logger.error(Constants.ERR_TEST_DATA_FAIL, e);
        }
    }

    /**
     * Parse test data from a resources file
     * @param resourcePath
     * @return
     * @throws IOException
     */
    private List<Document> parseTestData(String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        InputStream inputStream = resource.getInputStream();

        // first we read the data
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }

        String jsonData = textBuilder.toString();

        // then we convert that data to a json node
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonData);

        // we ensure it is an array of documents to insert
        if (!jsonNode.isArray()) {
            throw new InvalidObjectException(Constants.ERR_TEST_DATA_FORMAT);
        }

        // we parse and store the elements of the array
        Iterator<JsonNode> it = jsonNode.elements();
        ArrayList results = new ArrayList<Document>();

        while (it.hasNext()) {
            JsonNode node = it.next();

            Document parsed = Document.parse(node.toString());

            results.add(parsed);
        }

        // return those elements
        return results;
    }
}
