/**
 * This file demonstrates how to replace a document in a collection by using
 * the Java driver.
 * It connects to a MongoDB deployment, accesses the "sample_mflix" database,
 * and replaces a single document in the "movies" collection.
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
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;

public class ReplaceOne {

    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            Bson query = eq("title", "Music of the Heart");

            // Creates a new document containing "title" and "fullplot" fields
            Document replaceDocument = new Document().
                    append("title", "50 Violins").
                    append("fullplot", " A dramatization of the true story of Roberta Guaspari who co-founded the Opus 118 Harlem School of Music");

            // Instructs the driver to insert a new document if none match the query
            ReplaceOptions opts = new ReplaceOptions().upsert(true);

            // Runs a write operation that replaces a matching document with the new document
            UpdateResult result = collection.replaceOne(query, replaceDocument, opts);

            // Prints the number of modified documents and the upserted document ID, which only contains a value if an upsert was performed
            System.out.println("Modified document count: " + result.getModifiedCount());
            System.out.println("Upserted id: " + result.getUpsertedId());

        // Prints a message if an error occurs during the operation
        } catch (MongoException me) {
            System.err.println("Unable to replace due to an error: " + me);
        }
    }
}
