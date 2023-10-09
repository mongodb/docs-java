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

            // Access the "movies" collection in the "sample_mflix" database
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            try {
                BulkWriteResult result = collection.bulkWrite(
                        Arrays.asList(
                                // Insert three documents into the "movies" collection
                                new InsertOneModel<>(new Document("name", "A Sample Movie")),
                                new InsertOneModel<>(new Document("name", "Another Sample Movie")),
                                new InsertOneModel<>(new Document("name", "Yet Another Sample Movie")),

                                // Update a document's "name" value from "A Sample Movie" to "An Old Sample Movie"
                                new UpdateOneModel<>(new Document("name", "A Sample Movie"),
                                        new Document("$set", new Document("name", "An Old Sample Movie")),
                                        // Create the document if it's not found
                                        new UpdateOptions().upsert(true)),

                                // Delete a document that has a "name" value of "Yet Another Sample Movie"
                                new DeleteOneModel<>(new Document("name", "Yet Another Sample Movie")),

                                // Replace a document that has a "name" value of "Yet Another Sample Movie" with a new document
                                new ReplaceOneModel<>(new Document("name", "Yet Another Sample Movie"),
                                        new Document("name", "The Other Sample Movie").append("runtime",  "42"))
                                ));

                // Print the number of inserted, updated, and deleted documents
                System.out.println("Result statistics:" +
                        "\ninserted: " + result.getInsertedCount() +
                        "\nupdated: " + result.getModifiedCount() +
                        "\ndeleted: " + result.getDeletedCount());

            // Handle any exceptions that might occur during bulk write operations
            } catch (MongoException me) {
                System.err.println("The bulk write operation failed due to an error: " + me);
            }
        }
    }
}

