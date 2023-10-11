/**
 * This file demonstrates how to run a command by using the Java driver.
 * The file connects to a MongoDB deployment, accesses the "sample_mflix" database,
 * and runs a command to retrieve database statistics.
 */

package usage.examples;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


public class RunCommand {
    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {

            // Accesses the "sample_mflix" database
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");

            try {
                // Defines a command to retrieve database statistics using the "dbStats" command
                Bson command = new BsonDocument("dbStats", new BsonInt64(1));
                Document commandResult = database.runCommand(command);
                // Prints the database statistics
                System.out.println("dbStats: " + commandResult.toJson());
                
            // Handles any exception that might occur during the command execution
            } catch (MongoException me) {
                System.err.println("An error occurred: " + me);
            }
        }
    }
}
