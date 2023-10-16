/**
 * This file demonstrates how to find distinct values of a field by using the Java driver.
 * It connects to a MongoDB deployment, accesses the "sample_mflix" database, and finds 
 * distinct "year" values for documents that match the specified query filter.
 */

package usage.examples;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Distinct {
    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            try {
                // Runs an operation that retrieves the distinct "year" values of matching documents
                DistinctIterable<Integer> docs = collection.distinct("year", Filters.eq("directors", "Carl Franklin"), Integer.class);
                MongoCursor<Integer> results = docs.iterator();

                // Prints the distinct "year" values
                while(results.hasNext()) {
                    System.out.println(results.next());
                }

            // Prints a message if the operation generates an error
            } catch (MongoException me) {
                System.err.println("An error occurred: " + me);
            }
        }
    }
}

