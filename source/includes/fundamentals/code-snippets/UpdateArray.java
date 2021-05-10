package docs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.UpdateOptions;

public class UpdateArray {
    private final MongoCollection<Document> collection;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private UpdateArray() {
        final String uri = System.getenv("DRIVER_REF_URI");

        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("crudOps");
        collection = database.getCollection("updateArray");
    }

    public static void main(String [] args){
        UpdateArray updateArray = new UpdateArray();
        
        System.out.println("$ example:");
        updateArray.setUpDocument();
        updateArray.updateValueExample();
        updateArray.preview();

        System.out.println("$<> example:");
        updateArray.setUpDocument();
        updateArray.updateValueOptionsExample();
        updateArray.preview();

        System.out.println("$[] example:");
        updateArray.setUpDocument();
        updateArray.updateAllElementsExample();
        updateArray.preview();

        System.out.println("Push example:");
        updateArray.setUpDocument();
        updateArray.pushElementsExample();
        updateArray.preview();
    }

    private void updateValueExample(){
        // begin updateValueExample
        Bson filter = Filters.eq("instock.warehouse", 'B');
        Bson update = Updates.inc("instock.$.qty", -3);
        collection.updateOne(filter, update);
        // end updateValueExample
    }

    private void updateValueOptionsExample(){
        // begin updateValueOptionsExample
        Bson filter = Filters.eq("_id", 1);
        UpdateOptions options = new UpdateOptions().arrayFilters(Arrays.asList(Filters.eq("location.warehouse", 'B')));
        Bson update = Updates.pull("instock.$[location].warehouse", 'B' );
        collection.updateOne(filter, update, options);
        // end updateValueOptionsExample
    }

    private void updateAllElementsExample(){
        // begin updateAllElementsExample
        Bson filter = Filters.eq("_id", 1);
        Bson update = Updates.inc("instock.$[].qty", 5);
        collection.updateOne(filter, update);
        // end updateAllElementsExample
    }

    private void pushElementsExample(){
        // begin pushElementsExample
        Bson filter = Filters.eq("_id", 1);
        Document doc = new Document("qty", "11").append("warehouse", Arrays.asList('D'));
        Bson update = Updates.push("instock", doc);
        collection.updateOne(filter, update);
        // end pushElementsExample
    }
    
    private void preview(){
        collection.find().forEach(doc -> System.out.println(doc.toJson()));
    }

    private void setUpDocument(){
        collection.drop();
        collection.insertOne(
            Document.parse("{ _id: 1, color: 'green', instock: [ { qty: 8, warehouse: ['A', 'E'] }, { qty: 13, warehouse: ['B', 'C'] } , { qty: 15, warehouse: ['B', 'F']} ] }")
            );
    }
}
