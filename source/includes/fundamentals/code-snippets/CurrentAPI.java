package com.mycompany.app;


import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.net.URL;

public class CurrentAPI {

    private static final String COLLECTION = "test";
    private static final String DATABASE = "test";
    private static String URI = System.getenv("DRIVER_URL");

    public static void main(String[] args) throws InterruptedException {
        CurrentAPI c = new CurrentAPI();

        c.example1();
        c.example2();

    }

    private void example1() {
        // Connects to a MongoDB instance with the current API
        // start current-api-example
        MongoClient client = MongoClients.create(URI);
        MongoDatabase db = client.getDatabase(DATABASE);
        MongoCollection<Document> col = db.getCollection(COLLECTION);

        // Finds and prints a document in your collection
        Document doc = col.find().first();
        System.out.println(doc.toJson());
        // end current-api-example
        client.close();
    }

    private void example2() {
        // Sets a write concern on your client with the current API
        // start current-api-mongoclientsettings-example
        MongoClientSettings options = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(URI))
                .writeConcern(WriteConcern.W1).build();
        MongoClient client = MongoClients.create(options);
        // end current-api-mongoclientsettings-example
        client.close();
    }




}
