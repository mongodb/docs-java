/**
 * This file demonstrates how to open a change stream by using the Java driver.
 * It connects to a MongoDB deployment, accesses the "sample_mflix" database, and listens
 * to change events in the "movies" collection. The code uses a change stream with a pipeline
 * to only filter for "insert" and "update" events.
 */

package usage.examples;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.FullDocument;

public class Watch {
    public static void main( String[] args ) {

        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Access the "movies" collection in the "sample_mflix" database
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            // Create a pipeline to only filter for "insert" and "update" events
            List<Bson> pipeline = Arrays.asList(
                Aggregates.match(
                        Filters.in("operationType",
                                Arrays.asList("insert", "update"))));
            
            // Create a change stream with a full document lookup
            ChangeStreamIterable<Document> changeStream = database.watch(pipeline)
                .fullDocument(FullDocument.UPDATE_LOOKUP);
           
            // Define a mutable integer to count received events
            final int[] numberOfEvents = {0};

            // Start listening to change events and print events as they occur
            changeStream.forEach(event -> {
            System.out.println("Received a change to the collection: " + event);
                // Increment the count of received events and exit after receiving 2 events
                if (++numberOfEvents[0] >= 2) {
                  System.exit(0);
                }
            });
        }
    }
}
