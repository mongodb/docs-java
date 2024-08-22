package fundamentals;

// begin imports
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.WriteConcern;
import org.bson.Document;

import java.util.Arrays;
// end imports

public class Transaction {
    public static void main(String[] args) {
        // Connect to MongoDB
        String connectionString = "mongodb://localhost:27017"; // Update with your MongoDB URI
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase("yourDatabaseName");
            MongoCollection<Document> collection = database.getCollection("yourCollectionName");

            // Set transaction options
            TransactionOptions txnOptions = TransactionOptions.builder()
                    .writeConcern(WriteConcern.MAJORITY)
                    .build();

            try (ClientSession session = mongoClient.startSession()) {

                // Use withTransaction and lambda for transactional operations
                session.withTransaction(() -> {
                    collection.insertMany(session, Arrays.asList(
                            new Document("title", "The Bluest Eye").append("author", "Toni Morrison"),
                            new Document("title", "Sula").append("author", "Toni Morrison"),
                            new Document("title", "Song of Solomon").append("author", "Toni Morrison")
                    ));
                    return null; // Return value as expected by the lambda
                }, txnOptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
