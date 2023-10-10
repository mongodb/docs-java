/**
 * This file performs basic CRUD operations and generates change events when run with the
 * Watch application.
 * It connects to a MongoDB deployment, accesses the "sample_mflix" database, and performs
 * insert, update, and delete operations on the "movies" collection. 
 */

package usage.examples;

import java.util.Arrays;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;

public class WatchCompanion {
    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Access the "movies" collection in the "sample_mflix" database
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            try {
                // Insert a sample document into the "movies" collection and print its ID
                InsertOneResult insertResult = collection.insertOne(new Document("test", "sample movie document"));
                System.out.println("Success! Inserted document id: " + insertResult.getInsertedId());

                // Update the sample document and print the number of modified documents
                UpdateResult updateResult = collection.updateOne(new Document("test", "sample movie document"), Updates.set("field2", "sample movie document update"));
                System.out.println("Updated " + updateResult.getModifiedCount() + " document.");

                // Delete the sample document and print the number of deleted documents
                DeleteResult deleteResult = collection.deleteOne(new Document("field2", "sample movie document update"));
                System.out.println("Deleted " + deleteResult.getDeletedCount() + " document.");
            
            // Handle any exceptions that might occur during the operations
            } catch (MongoException me) {
                System.err.println("Unable to insert, update, or replace due to an error: " + me);
            }
        }
    }
}
