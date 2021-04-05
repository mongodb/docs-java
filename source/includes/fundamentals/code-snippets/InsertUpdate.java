package docs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

public class InsertUpdate {

    private final MongoCollection<Document> collection;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private InsertUpdate() {
        final String uri = System.getenv("DRIVER_REF_URI");

        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("crudOps");
        collection = database.getCollection("upsert");
    }

    public static void main(String [] args){
        InsertUpdate insertUpdate = new InsertUpdate();
        insertUpdate.preview(true);
        insertUpdate.setupPaintCollection();

        System.out.println("Update One Attempt:");
        insertUpdate.updateOneAttemptExample();
        insertUpdate.preview(false);

        System.out.println("Update One:");
        insertUpdate.updateOneExample();
        insertUpdate.preview(false);
    }

    private void updateOneAttemptExample(){
        // begin updateOneAttemptExample
        Bson filter = Filters.eq("color", "orange");
        Bson update = Updates.inc("qty", 10);
        System.out.println(collection.updateOne(filter, update));
        // end updateOneAttemptExample
    }

    private void updateOneExample(){
        // begin updateOneExample
        Bson filter = Filters.eq("color", "orange");
        Bson update = Updates.inc("qty", 10);
        UpdateOptions options = new UpdateOptions().upsert(true);
        System.out.println(collection.updateOne(filter, update, options));
        // end updateOneExample
    }

    private void preview(boolean drop){
        Bson filter = Filters.empty();
        collection.find(filter).forEach(doc -> System.out.println(doc.toJson()));
        if (drop){
          collection.drop();  
        }
    }

    private void setupPaintCollection() {

        List<Document> deletedata = new ArrayList<>();

        Document p1 = new Document("color", "red").append("qty", 5);
        Document p2 = new Document("color", "purple").append("qty", 8);
        Document p3 = new Document("color", "blue").append("qty", 0);
        Document p4 = new Document("color", "white").append("qty", 0);
        Document p5 = new Document("color", "yellow").append("qty", 6);
        Document p6 = new Document("color", "pink").append("qty", 0);
        Document p7 = new Document("color", "green").append("qty", 0);
        Document p8 = new Document("color", "black").append("qty", 8);
        
        deletedata.add(p1);
        deletedata.add(p2);
        deletedata.add(p3);
        deletedata.add(p4);
        deletedata.add(p5);
        deletedata.add(p6);
        deletedata.add(p7);
        deletedata.add(p8);

        collection.insertMany(deletedata);
    }
}
