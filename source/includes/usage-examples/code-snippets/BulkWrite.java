/**
 * This file demonstrates MongoDB bulk write operations by using the Java driver.
 * It connects to a MongoDB deployment, accesses the "sample_mflix" database,
 * and performs bulk write operations on the "movies" collection.
 */

package usage.examples;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.DeleteOneModel;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;

public class BulkWrite {
    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {

            // Accesses the "movies" collection in the "sample_mflix" database
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            try {
                BulkWriteResult result = collection.bulkWrite(
                        Arrays.asList(
                                // Inserts three documents into the "movies" collection
                                new InsertOneModel<>(new Document("name", "A Sample Movie")),
                                new InsertOneModel<>(new Document("name", "Another Sample Movie")),
                                new InsertOneModel<>(new Document("name", "Yet Another Sample Movie")),

                                // Updates a document's "name" value from "A Sample Movie" to "An Old Sample Movie"
                                new UpdateOneModel<>(new Document("name", "A Sample Movie"),
                                        new Document("$set", new Document("name", "An Old Sample Movie")),
                                        // Create the document if it's not found
                                        new UpdateOptions().upsert(true)),

                                // Deletes a document that has a "name" value of "Yet Another Sample Movie"
                                new DeleteOneModel<>(new Document("name", "Yet Another Sample Movie")),

                                // Replaces the previous document with a new document whose "name" value is "The Other Sample Movie"
                                new ReplaceOneModel<>(new Document("name", "Yet Another Sample Movie"),
                                        new Document("name", "The Other Sample Movie").append("runtime",  "42"))
                                ));

                // Prints the number of inserted, updated, and deleted documents
                System.out.println("Result statistics:" +
                        "\ninserted: " + result.getInsertedCount() +
                        "\nupdated: " + result.getModifiedCount() +
                        "\ndeleted: " + result.getDeletedCount());

            // Handles any exceptions that might occur during bulk write operations
            } catch (MongoException me) {
                System.err.println("The bulk write operation failed due to an error: " + me);
            }
        }
    }
}

