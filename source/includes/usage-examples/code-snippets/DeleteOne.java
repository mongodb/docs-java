/**
 * This file demonstrates how to delete a document in a collection by using the
 * Java driver.
 * The file connects to a MongoDB deployment, accesses the "sample_mflix" database,
 * and deletes a document in the "movies" collection based on a specified query.
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
import com.mongodb.client.result.DeleteResult;

public class DeleteOne {

    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {

            // Accesses the "movies" collection in the "sample_mflix" database
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            // Specifies a query to find a document with a "title" of "The Garbage Pail Kids Movie"
            Bson query = eq("title", "The Garbage Pail Kids Movie");

            try {
                // Deletes the document with a "title" of "The Garbage Pail Kids Movie"
                DeleteResult result = collection.deleteOne(query);
                // Prints the number of deleted documents
                System.out.println("Deleted document count: " + result.getDeletedCount());

            // Handles any exceptions that might occur during the delete operation
            } catch (MongoException me) {
                System.err.println("Unable to delete due to an error: " + me);
            }
        }
    }
}

