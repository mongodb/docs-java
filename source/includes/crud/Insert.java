// Inserts a sample document describing a movie by using the Java driver

package org.example;

import java.util.Arrays;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.InsertManyResult;

import java.util.List;

public class Insert {
    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            try {
                // Inserts a sample document describing a movie into the collection
                InsertOneResult result = collection.insertOne(new Document()
                        .append("_id", new ObjectId())
                        .append("title", "Ski Bloopers")
                        .append("genres", Arrays.asList("Documentary", "Comedy")));

                // Prints the ID of the inserted document
                System.out.println("insertOne() document id: " + result.getInsertedId());
            
            // Prints a message if any exceptions occur during the operation
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }

            // Creates two sample documents containing a "title" field
            List<Document> movieList = Arrays.asList(
                    new Document().append("title", "Short Circuit 3"),
                    new Document().append("title", "The Lego Frozen Movie"));

            try {
                // Inserts sample documents describing movies into the collection
                InsertManyResult result = collection.insertMany(movieList);

                // Prints the IDs of the inserted documents
                System.out.println("insertMany() document ids: " + result.getInsertedIds());
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }
        }
    }
}
