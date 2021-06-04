package com.mycompany.app;


import com.mongodb.ConnectionString;
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
    private static final ConnectionString URI = new ConnectionString(System.getenv("DRIVER_URL"));

    public static void main(String[] args) throws InterruptedException {
        // start current-api-example
        MongoClient client = MongoClients.create(URI);
        MongoDatabase db = client.getDatabase(DATABASE);
        MongoCollection<Document> col = db.getCollection(COLLECTION);
        Document doc = col.find().first();
        System.out.println(doc.toJson());
        // end current-api-example

    }



}
