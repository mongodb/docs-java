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
            // Access the "movies" collection in the "sample_mflix" database
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            // Define a query to find the document to be replaced
            Bson query = eq("title", "Music of the Heart");

            // Define the replacement document
            Document replaceDocument = new Document().
                    append("title", "50 Violins").
                    append("fullplot", " A dramatization of the true story of Roberta Guaspari who co-founded the Opus 118 Harlem School of Music");

            // Instruct the driver to insert a new document if no documents match the query
            ReplaceOptions opts = new ReplaceOptions().upsert(true);

            // Replace the queried document with the specified new document
            UpdateResult result = collection.replaceOne(query, replaceDocument, opts);

            // Print the number of modified documents
            System.out.println("Modified document count: " + result.getModifiedCount());

            // Print the upserted document ID, which only contains a value if an upsert was performed
            System.out.println("Upserted id: " + result.getUpsertedId());

        // Handle any exceptions that might occur during the operation 
        } catch (MongoException me) {
            System.err.println("Unable to replace due to an error: " + me);
        }
    }
}
