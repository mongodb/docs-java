package docs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;

import org.bson.Document;
import java.util.List;
import java.util.ArrayList;

public class Insert {
    
    private final MongoCollection<Document> collection;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private Insert() {
        final String uri = System.getenv("DRIVER_REF_URI");

        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("crudOps");
        collection = database.getCollection("insert");
    }

    public static void main(String[] args) {
        Insert insert = new Insert();

        System.out.println("Insert One:");
        insert.insertOneExample();
        insert.preview();

        System.out.println("Insert Many:");
        insert.insertManyExample();
        insert.preview();
    }

    private void insertOneExample() {
        collection.drop();
        // begin insertOneExample
        Document doc1 = new Document("_id", 1).append("color", "red").append("qty", 5);
        
        InsertOneResult result = collection.insertOne(doc1);
        System.out.println(result);
        // end insertOneExample
    }

    private void insertManyExample() {
        collection.drop();
        // begin insertManyExample
        List<Document> documents = new ArrayList<>();

        Document doc1 = new Document("_id", 1).append("color", "red").append("qty", 5);
        Document doc2 = new Document("_id", 2).append("color", "purple").append("qty", 10);
        Document doc3 = new Document("_id", 3).append("color", "blue").append("qty", 8);
       
        documents.add(doc1);
        documents.add(doc2);
        documents.add(doc3);
        
        InsertManyResult result = collection.insertMany(documents);
        System.out.println(result);
        //end insertManyExample
    }

    private void preview(){
        collection.find().forEach(doc -> System.out.println(doc.toJson()));
    }
}
