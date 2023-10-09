package usage.examples;

import static com.mongodb.client.model.Filters.lt;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

public class DeleteMany {
    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {

            // Access the "movies" collection in the "sample_mflix" database
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            // Specify a query to find documents with an "imdb.rating" value less than 1.9
            Bson query = lt("imdb.rating", 1.9);

            try {
                // Delete documents that have an "imdb.rating" value less than 1.9
                DeleteResult result = collection.deleteMany(query);
                // Print the number of deleted documents
                System.out.println("Deleted document count: " + result.getDeletedCount());
            
            // Handle any exceptions that might occur during the delete operation
            } catch (MongoException me) {
                System.err.println("Unable to delete due to an error: " + me);
            }
        }
    }
}

