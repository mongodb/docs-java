/**
 * This file demonstrates how to update a document in a collection by using
 * the Java driver.
 * It connects to a MongoDB deployment, accesses the "sample_mflix" database, and
 * updates a document in the "movies" collection that matches a specified query.
 */

package usage.examples;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

public class UpdateOne {

    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Access the "movies" collection in the "sample_mflix" database
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            // Define a query that matches a document to be updated
            Document query = new Document().append("title",  "Cool Runnings 2");

            // Define the update operations to perform
            Bson updates = Updates.combine(
                    Updates.set("runtime", 99),
                    Updates.addToSet("genres", "Sports"),
                    Updates.currentTimestamp("lastUpdated"));

            // Instruct the driver to insert a new document if no documents match the query
            UpdateOptions options = new UpdateOptions().upsert(true);

            try {
                // Perform the previously specified update operations 
                UpdateResult result = collection.updateOne(query, updates, options);

                // Print the number of updated documents
                System.out.println("Modified document count: " + result.getModifiedCount());

                // Print the upserted document ID, which only contains a value if an upsert was performed
                System.out.println("Upserted id: " + result.getUpsertedId());
            
            // Handle any exceptions that might occur during the operation
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
    }
}
