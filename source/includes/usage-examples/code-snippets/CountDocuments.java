/**
 * This file demonstrates how to perform count operations by using the Java driver.
 * It connects to a MongoDB deployment, accesses the "sample_mflix" database,
 * and counts the number of documents in the "movies" collection.
 * The code also counts the number of documents that match a specified query.
 */

package usage.examples;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class CountDocuments {
    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Access the "movies" collection in the "sample_mflix" database
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            // Define a query to find movies from Spain
            Bson query = eq("countries", "Spain");

            try {
                // Retrieve and print the estimated number of documents in the "movies" collection
                long estimatedCount = collection.estimatedDocumentCount();
                System.out.println("Estimated number of documents in the movies collection: " + estimatedCount);

                // Count the number of movies from Spain by using the defined query
                long matchingCount = collection.countDocuments(query);
                System.out.println("Number of movies from Spain: " + matchingCount);
            
            // Handle any exceptions that might occur during the operation
            } catch (MongoException me) {
                System.err.println("An error occurred: " + me);
            }
        }
    }
}

