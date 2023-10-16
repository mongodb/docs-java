/**
 * This file demonstrates how to insert multiple documents into a collection by
 * using the Java driver.
 * The file connects to a MongoDB deployment, accesses the "sample_mflix" database,
 * and inserts two new documents into the "movies" collection.
 */

package usage.examples;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;

public class InsertMany {

    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            // Creates two sample documents containing a "title" field
            List<Document> movieList = Arrays.asList(
                    new Document().append("title", "Short Circuit 3"),
                    new Document().append("title", "The Lego Frozen Movie"));

            try {
                // Runs a write operation that inserts sample documents into the collection
                InsertManyResult result = collection.insertMany(movieList);

                // Prints the IDs of the inserted documents
                System.out.println("Inserted document ids: " + result.getInsertedIds());
            
            // Prints a message if the operation generates an error
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
            }
        }
    }
}
