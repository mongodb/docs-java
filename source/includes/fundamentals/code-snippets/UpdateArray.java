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

        System.out.println("$[] example:");
        updateArray.setUpDocument();
        updateArray.updateValueOptionsExample();
        updateArray.preview();

        System.out.println("$<> example:");
        updateArray.setUpDocument();
        updateArray.updateAllElementsExample();
        updateArray.preview();

        System.out.println("Push example:");
        updateArray.setUpDocument();
        updateArray.pushElementsExample();
        updateArray.preview();

        System.out.println("Add To Set example:");
        updateArray.setUpDocument();
        updateArray.addToSetElementsExample();
        updateArray.preview();

        // System.out.println("Pop First example:");
        // updateArray.setUpDocument();
        // updateArray.popFirstElementsExample();
        // updateArray.preview();

        // System.out.println("Pop Last example:");
        // updateArray.setUpDocument();
        // updateArray.popLastElementsExample();
        // updateArray.preview();

        System.out.println("Pull example:");
        updateArray.setUpDocument();
        updateArray.pullElementsExample();
        updateArray.preview();

        // System.out.println("Pull All example:");
        // updateArray.setUpDocument();
        // updateArray.pullAllElementsExample();
        // updateArray.preview();
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
        Bson filter = Filters.empty();
        UpdateOptions options = new UpdateOptions().arrayFilters(Arrays.asList(Filters.eq("instock.warehouse", 'B')));
        Bson update = Updates.inc("instock.$[instock].qty", -3);
        collection.updateOne(filter, update, options);
        // end updateValueOptionsExample
    }

    private void updateAllElementsExample(){
        // begin updateAllElementsExample
        Bson filter = Filters.eq("instock.warehouse", 'B');
        Bson update = Updates.inc("instock.$[].qty", 5);
        collection.updateOne(filter, update);
        // end updateAllElementsExample
    }

    private void pushElementsExample(){
        // begin pushElementsExample
        Bson filter = Filters.empty();
        Document doc = new Document().append("qty", "11").append("warehouse", Arrays.asList('D'));
        Bson update = Updates.push("instock", doc);
        collection.updateOne(filter, update);
        // end pushElementsExample
    }

    private void addToSetElementsExample(){
        // begin addToSetElementsExample
        Bson filter = Filters.empty();
        Document doc = new Document().append("qty", 8).append("warehouse", Arrays.asList('A', 'E'));
        Bson update = Updates.addToSet("instock", doc);
        collection.updateOne(filter, update);
        // end addToSetElementsExample
    }

    // private void popFirstElementsExample(){
    //     // begin popFirstElementsExample
    //     Bson filter = Filters.empty();
    //     Bson update = Updates.popFirst("instock");
    //     collection.updateOne(filter, update);
    //     // end popFirstElementsExample
    // }

    // private void popLastElementsExample(){
    //     // begin popLastElementsExample
    //     Bson filter = Filters.empty();
    //     Bson update = Updates.popLast("instock");
    //     collection.updateOne(filter, update);
    //     // end popLastElementsExample
    // }

    private void pullElementsExample(){
        // begin pullElementsExample
        Bson filter = Filters.empty();
        Document doc = new Document().append("warehouse", 'B');
        Bson update = Updates.pull("instock", doc);
        collection.updateOne(filter, update);
        // end pullElementsExample
    }

    // private void pullAllElementsExample(){
    //     // begin pullAllElementsExample
    //     Bson filter = Filters.empty();
    //     Bson update = Updates.pullAll("instock", Arrays.asList('A', 'E'));
    //     collection.updateOne(filter, update);
    //     // end pullAllElementsExample
    // }

    private void preview(){
        collection.find().forEach(doc -> System.out.println(doc.toJson()));
    }

    private void setUpDocument(){
        collection.drop();
        collection.insertOne(
            Document.parse("{ _id: 1, color: 'green', instock: [ { qty: 8, warehouse: ['A', 'E'] }, { qty: 13, warehouse: ['B', 'C'] } , { qty: 15, warehouse: ['B', 'E'] } ] }")
            );
    }
}
public class UpdateArray {
    
}
