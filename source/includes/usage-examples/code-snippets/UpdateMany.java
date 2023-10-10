/**
 * This file demonstrates how to update multiple documents in a collection by using
 * the Java driver.
 * It connects to a MongoDB deployment, accesses the "sample_mflix" database, and updates
 * documents in the "movies" collection that match a specified query.
 */

package usage.examples;

import static com.mongodb.client.model.Filters.gt;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

public class UpdateMany {

    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Access the "movies" collection in the "sample_mflix" database
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            // Define a query that matches documents to be updated
            Bson query = gt("num_mflix_comments", 50);

            // Define the update operations to perform
            Bson updates = Updates.combine(
                    Updates.addToSet("genres", "Frequently Discussed"),
                    Updates.currentTimestamp("lastUpdated"));

            try {
                // Perform the previously specified update operations 
                UpdateResult result = collection.updateMany(query, updates);

                // Print the number of updated documents
                System.out.println("Modified document count: " + result.getModifiedCount());

            // Handle any exceptions that might occur during the operation
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
    }
}
