package docs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.*;

import java.util.Arrays;

import org.bson.Document;

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
        Cursor c = new Cursor();
        // c.setupPaintCollection();
        c.forEachRemainingIteration();
        c.manualIteration();
    }

    private void forEachRemainingIteration(){
        // begin forEachRemainingIteration
        MongoCursor<Document> cursor = collection.find().cursor();
        cursor.forEachRemaining(doc -> System.out.println(doc.toJson()));
        // end forEachRemainingIteration
    }

    private void manualIteration(){
        // begin manualIteration
        MongoCursor<Document> cursor = collection.find().cursor();
        while (cursor.hasNext()){
            System.out.println(cursor.next().toJson());
        }
        // end manualIteration

        // begin close 
        cursor.close();
        // end close 
    }

    public void setupPaintCollection(){
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
