package org.example;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.cursor.TimeoutMode;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;


public class csot {

    public static void main(String[] args) {
        MongoClient mongoClient = new csot().mongoClientSettings();
    }

    private MongoClient mongoClientSettings(){
        // start-mongoclientsettings
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("<connection string>"))
                .timeout(5L, SECONDS)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        // end-mongoclientsettings

        return mongoClient;
    }

    private MongoClient connectionString(){
        // start-string
        String uri = "<connection string>/?timeoutMS=5000";
        MongoClient mongoClient = MongoClients.create(uri);
        // end-string

        return mongoClient;
    }

    private void operationTimeout(){
        // start-operation-timeout
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("<connection string>"))
                .timeout(5L, SECONDS)
                .build();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase("db");
            MongoCollection<Document> collection = database.getCollection("people");

            InsertOneResult result = collection.insertOne(new Document("name", "Francine Loews"));
            System.out.println("Inserted a document with ID: " + result.getInsertedId());
        }
        // end-operation-timeout
    }

    private void overrideTimeout(){
        // start-override
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("<connection string>"))
                .timeout(5L, SECONDS)
                .build();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase("db");
            MongoCollection<Document> collection = database
                    .getCollection("people")
                    .withTimeout(10L, SECONDS);

            // ... perform operations on MongoCollection
        }
        // end-override
    }

    private void cursorTimeout(){
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("<connection string>"))
                .timeout(5L, SECONDS)
                .build();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoCollection<Document> collection = mongoClient
                    .getDatabase("db")
                    .getCollection("people");

            // start-cursor-lifetime
            FindIterable<Document> cursorWithLifetimeTimeout = collection
                    .find(gte("age", 40))
                    .timeoutMode(TimeoutMode.CURSOR_LIFETIME);
            // end-cursor-lifetime

            // start-cursor-iteration
            try (MongoCursor<Document> cursorWithIterationTimeout = collection
                    .find(gte("age", 40))
                    .timeoutMode(TimeoutMode.ITERATION)
                    .cursor()
            ) {
                while (cursorWithIterationTimeout.hasNext()) {
                    System.out.println(cursorWithIterationTimeout.next().toJson());
                }
            }
            // end-cursor-iteration
        }

    }
}
