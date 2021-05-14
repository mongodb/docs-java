package docs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateManyModel;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.DeleteOneModel;
import com.mongodb.client.model.DeleteManyModel;

import com.mongodb.MongoBulkWriteException;

import org.bson.Document;

import java.util.*;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.WriteModel;

public class BulkWrite {
    
    private final MongoCollection<Document> collection;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private BulkWrite() {
        final String uri = System.getenv("DRIVER_REF_URI");

        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("crudOps");
        collection = database.getCollection("bulkWrite");
    }

    public static void main(String[] args) {
        BulkWrite bulkWrite = new BulkWrite();
        System.out.println("Ordered BulkWrite");
        bulkWrite.setUpCollection();
        bulkWrite.bulkWriteExample();
        bulkWrite.preview();

        System.out.println("Unordered BulkWrite");
        bulkWrite.setUpCollection();
        bulkWrite.bulkWriteNotOrderedExample();
        bulkWrite.preview();

        System.out.println("Unordered BulkWriteException");
        bulkWrite.setUpCollection();
        bulkWrite.bulkWriteOrderedExceptionExample();
        bulkWrite.preview();

        System.out.println("Insert");
        bulkWrite.insertDocumentsExample();
        bulkWrite.preview();

        System.out.println("Replace");
        bulkWrite.replaceDocumentsExample();
        bulkWrite.preview();

        System.out.println("Update");
        bulkWrite.updateDocumentsExample();
        bulkWrite.preview();

        System.out.println("Delete");
        bulkWrite.deleteDocumentsExample();
        bulkWrite.preview();
    }

    
    private void bulkWriteOrderedExceptionExample() {
        // begin bulkWriteOrderedExceptionExample
        try {
            collection.bulkWrite(
            Arrays.asList(new InsertOneModel<>(new Document("_id", 1)),
                            new ReplaceOneModel<>(Filters.eq("_id", 1),
                                                new Document("_id", 1).append("x", 2)),
                            new UpdateOneModel<>(Filters.eq("_id", 3), Updates.set("x", 2)), 
                            new DeleteManyModel<>(Filters.eq("x", 2)) ),
                            new BulkWriteOptions().ordered(false));
        } catch (MongoBulkWriteException e){
            collection.find().forEach(doc -> System.out.println(doc.toJson()));
            
            System.out.println("A MongoBulkWriteException occured: " + e.getWriteErrors());
        }
        //end bulkWriteOrderedExceptionExample
    }

    private void bulkWriteNotOrderedExample() {
        // begin bulkWriteNotOrderedExample
        collection.bulkWrite(
            Arrays.asList(new InsertOneModel<>(new Document("_id", 3)),
                            new ReplaceOneModel<>(Filters.eq("_id", 1),
                                                new Document("_id", 1).append("x", 2)),
                            new UpdateOneModel<>(Filters.eq("_id", 3), Updates.set("x", 2)), 
                            new DeleteManyModel<>(Filters.eq("x", 2)) ),
                            new BulkWriteOptions().ordered(false));
        //end bulkWriteNotOrderedExample
    }

    private void bulkWriteExample() {
        // begin bulkWriteExample
        collection.bulkWrite(
            Arrays.asList(new InsertOneModel<>(new Document("_id", 3)),
                            new ReplaceOneModel<>(Filters.eq("_id", 1),
                                                new Document("_id", 1).append("x", 2)),
                            new UpdateOneModel<>(Filters.eq("_id", 3), Updates.set("x", 2)), 
                            new DeleteManyModel<>(Filters.eq("x", 2)) ));                  
        //end bulkWriteExample
    }

    private void setUpCollection(){
            collection.drop();
            collection.bulkWrite(
                Arrays.asList(new InsertOneModel<>(new Document("_id", 1)),
                                new InsertOneModel<>(new Document("_id", 2)) ));
        }

    private void preview(){
        collection.find().forEach(doc -> System.out.println(doc.toJson()));
    }

    private void insertDocumentsExample(){
        collection.drop();
        // begin insertDocumentsExample
        collection.bulkWrite(
            Arrays.asList(new InsertOneModel<>(new Document("_id", 1)),
                            new InsertOneModel<>(new Document("_id", 2))));
        //end insertDocumentsExample
    }
    
    private void replaceDocumentsExample(){
        collection.drop();
        // begin replaceDocumentsExample
        collection.bulkWrite(
            Arrays.asList(new InsertOneModel<>(new Document("_id", 1)),
                            new InsertOneModel<>(new Document("_id", 2)),
                            new ReplaceOneModel<>(Filters.eq("_id", 1), new Document("_id", 1).append("x", 4))));
        //end replaceDocumentsExample
    }

    private void updateDocumentsExample(){
        collection.drop();
        // begin updateDocumentsExample
        collection.bulkWrite(
            Arrays.asList(new InsertOneModel<>(new Document("_id", 1)),
                            new InsertOneModel<>(new Document("_id", 2)),
                            new UpdateOneModel<>(Filters.eq("_id", 2), Updates.set("x", 8))));
        //end updateDocumentsExample
    }

    private void deleteDocumentsExample(){
       collection.drop();
       // begin deleteDocumentsExample
       collection.bulkWrite(
            Arrays.asList(new InsertOneModel<>(new Document("_id", 1)),
                            new InsertOneModel<>(new Document("_id", 2)),
                            new DeleteOneModel<>(Filters.eq("_id", 1))));
        //end deleteDocumentsExample
    }
}
