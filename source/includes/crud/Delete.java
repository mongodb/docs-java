// Deletes a document from a collection by using the Java driver

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

            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            Bson deleteOneQuery = eq("title", "The Garbage Pail Kids Movie");

            try {
                // Deletes the first document that has a "title" value of "The Garbage Pail Kids Movie"
                DeleteResult deleteOneResult = collection.deleteOne(deleteOneQuery);
                System.out.println("DeleteOne document count: " + deleteOneResult.getDeletedCount());

            // Prints a message if any exceptions occur during the operation
            } catch (MongoException me) {
                System.err.println("Unable to delete due to an error: " + me);
            }

            Bson deleteManyQuery = lt("imdb.rating", 1.9);

            try {
                // Deletes all documents that have an "imdb.rating" value less than 1.9
                DeleteResult deleteManyResult = collection.deleteMany(deleteManyQuery);
                
                // Prints the number of deleted documents
                System.out.println("DeleteMany document count: " + deleteManyResult.getDeletedCount());
            
            // Prints a message if any exceptions occur during the operation
            } catch (MongoException me) {
                System.err.println("Unable to delete due to an error: " + me);
            }
        }
    }
}