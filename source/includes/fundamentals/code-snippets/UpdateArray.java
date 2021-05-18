package docs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;

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

        System.out.println("$<> example:");
        updateArray.setUpDocument();
        updateArray.updateValueOptionsExample();

        System.out.println("$[] example:");
        updateArray.setUpDocument();
        updateArray.updateAllElementsExample();

        System.out.println("Push example:");
        updateArray.setUpDocument();
        updateArray.pushElementsExample();
    }

    private void updateValueExample(){
        // begin updateValueExample
        Bson filter = Filters.eq("instock.warehouse", 'B');
        Bson update = Updates.inc("instock.$.qty", -3);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                                        .returnDocument(ReturnDocument.AFTER);
        Document result = collection.findOneAndUpdate(filter, update, options);
        System.out.println(result.toJson());
        // end updateValueExample
    }

    private void updateValueOptionsExample(){
        // begin updateValueOptionsExample
        Bson filter = Filters.eq("_id", 1);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                                        .returnDocument(ReturnDocument.AFTER)
                                        .arrayFilters(Arrays.asList(
                                                Filters.eq("location.warehouse", 'B') ));
        Bson update = Updates.pull("instock.$[location].warehouse", 'B' );
        
        Document result = collection.findOneAndUpdate(filter, update, options);
        System.out.println(result.toJson());
        // end updateValueOptionsExample
    }

    private void updateAllElementsExample(){
        // begin updateAllElementsExample
        Bson filter = Filters.eq("_id", 1);
        Bson update = Updates.inc("instock.$[].qty", 5);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                                        .returnDocument(ReturnDocument.AFTER);
        Document result = collection.findOneAndUpdate(filter, update, options);
        System.out.println(result.toJson());
        // end updateAllElementsExample
    }

    private void pushElementsExample(){
        // begin pushElementsExample
        Bson filter = Filters.eq("_id", 1);
        Document doc = new Document("qty", 11).append("warehouse", Arrays.asList('D'));
        Bson update = Updates.push("instock", doc);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                                        .returnDocument(ReturnDocument.AFTER);
        Document result = collection.findOneAndUpdate(filter, update, options);
        System.out.println(result.toJson());
        // end pushElementsExample
    }

    private void setUpDocument(){
        collection.drop();
        collection.insertOne(
            Document.parse("{ _id: 1, color: 'green', instock: [ { qty: 8, warehouse: ['A', 'E'] }, { qty: 13, warehouse: ['B', 'C'] } , { qty: 15, warehouse: ['B', 'F']} ] }")
            );
    }
}
