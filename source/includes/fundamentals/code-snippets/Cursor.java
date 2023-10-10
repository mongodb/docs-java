package docs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ExplainVerbosity;
import com.mongodb.client.*;

import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.bson.Document;
import com.mongodb.client.model.Filters;

public class Cursor {
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private MongoCollection<Document> collection;

    private Cursor(){
        final String uri = System.getenv("DRIVER_REF_URI");

        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("test");
        collection = database.getCollection("orders");
    }

    public static void main(String [] args){
        // Initialize a Cursor instance and run multiple find operations
        Cursor c = new Cursor();
        c.setupPaintCollection();

        System.out.println("For Each Iteration");
        c.forEachIteration();

        System.out.println("First Example");
        c.firstExample();

        System.out.println("Available Example");
        c.availableExample();

        System.out.println("Explain Example");
        c.explainExample();

        System.out.println("Into Example");
        c.intoExample();

        System.out.println("Manual Iteration");
        c.manualIteration();
        
        System.out.println("Close Example");
        c.closeExample();

        System.out.println("Try With Resources");
        c.tryWithResourcesExample();
    }

    private void forEachIteration(){
        // Iterate through your cursor results
        // begin forEachIteration
        FindIterable<Document> iterable = collection.find();
        iterable.forEach(doc -> System.out.println(doc.toJson()));
        // end forEachIteration
    }

    private void firstExample(){
        // Retrieve the first matching document from your query
        // begin firstExample
        FindIterable<Document> iterable = collection.find();
        System.out.println(iterable.first());
        // end firstExample
    }

    private void availableExample(){
        // Retrieve the number of cursor results available locally without blocking
        // begin availableExample
        MongoCursor<Document> cursor = collection.find().cursor();
        System.out.println(cursor.available());
        // end availableExample
    }

    private void explainExample(){
        // View performance information about your find operation execution
        // begin explainExample
        Document explanation = collection.find().explain(ExplainVerbosity.EXECUTION_STATS);
        List<String> keys = Arrays.asList("queryPlanner", "winningPlan");
        System.out.println(explanation.getEmbedded(keys, Document.class).toJson());
        // end explainExample
    }

    private void intoExample(){
        // Store your query results in a list
        // begin intoExample
        List<Document> results = new ArrayList<>();
        FindIterable<Document> iterable = collection.find();
        iterable.into(results);
        System.out.println(results);
        // end intoExample
    }

    private void manualIteration(){
        // Continue your iteration only if more documents are available on the cursor
        // begin manualIteration
        MongoCursor<Document> cursor = collection.find().cursor();
        while (cursor.hasNext()){
            System.out.println(cursor.next().toJson());
        }
        // end manualIteration
    }

    private void closeExample(){
        // Free up a cursor's consumption of resources by calling close()
        // begin closeExample
        MongoCursor<Document> cursor = collection.find().cursor();
        
        try {
            while (cursor.hasNext()){
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
        // end closeExample
    }

    private void tryWithResourcesExample(){
        // Free up a cursor's consumption of resources automatically with a try statement
        // begin tryWithResourcesExample
        try(MongoCursor<Document> cursor = collection.find().cursor()) {
            while (cursor.hasNext()){
                System.out.println(cursor.next().toJson());
            }
        }
        // end tryWithResourcesExample
    }

    public void setupPaintCollection(){
        collection.drop();

        // Insert sample documents into the "paint" collection
        collection.insertMany(Arrays.asList(
            new Document("_id", 1).append("color", "red").append("qty", 5), 
            new Document("_id", 2).append("color", "purple").append("qty", 10), 
            new Document("_id", 3).append("color", "blue").append("qty", 9), 
            new Document("_id", 4).append("color", "white").append("qty", 6),
            new Document("_id", 5).append("color", "yellow").append("qty", 11),
            new Document("_id", 6).append("color", "pink").append("qty", 3),
            new Document("_id", 7).append("color", "green").append("qty", 8),
            new Document("_id", 8).append("color", "orange").append("qty", 7)
        ));
    }
}
