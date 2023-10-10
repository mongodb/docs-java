package docs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateOneModel;
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

        // Create a client and access the "bulkWrite" collection in the "crudOps" database
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("crudOps");
        collection = database.getCollection("bulkWrite");
    }

    public static void main(String[] args) {
        // For each operation, set up the collection, run the operation, and print the results
        BulkWrite bulkWrite = new BulkWrite();
        System.out.println("Ordered BulkWrite");
        bulkWrite.setUpCollection();
        bulkWrite.bulkWriteExample();
        bulkWrite.preview();

        System.out.println("Unordered BulkWrite");
        bulkWrite.setUpCollection();
        bulkWrite.bulkWriteNotOrderedExample();
        bulkWrite.preview();

        System.out.println("Insert BulkWriteException");
        bulkWrite.setUpCollection();
        bulkWrite.insertExceptionExample();

        System.out.println("Insert");
        bulkWrite.setUpCollection();
        bulkWrite.insertDocumentsExample();
        bulkWrite.preview();

        System.out.println("Replace");
        bulkWrite.setUpCollection();
        bulkWrite.replaceDocumentsExample();
        bulkWrite.preview();

        System.out.println("Update");
        bulkWrite.setUpCollection();
        bulkWrite.updateDocumentsExample();
        bulkWrite.preview();

        System.out.println("Delete");
        bulkWrite.setUpCollection();
        bulkWrite.deleteDocumentsExample();
        bulkWrite.preview();
    }

    
    private void insertExceptionExample() {
        // begin insertExceptionExample
        try {
            // Create a List that will store the bulk operations
            List<WriteModel<Document>> bulkOperations = new ArrayList<>();
            
            // Create an InsertOneModel for two documents with ID values of 1 and 3
            InsertOneModel<Document> doc1 = new InsertOneModel<>(new Document("_id", 1));
            InsertOneModel<Document> doc3 = new InsertOneModel<>(new Document("_id", 3));
            
            // Add the InsertOneModel instances to the bulkOperations list
            bulkOperations.add(doc1);
            bulkOperations.add(doc3);
            
            // Run the bulk operations on your collection
            collection.bulkWrite(bulkOperations); 
        
        // Handle any exceptions that occur during the operations
        } catch (MongoBulkWriteException e){
            System.out.println("A MongoBulkWriteException occured with the following message: " + e.getMessage());
        }
        //end insertExceptionExample
    }

    private void bulkWriteNotOrderedExample() {
        // Create a List that will store the bulk operations
        List<WriteModel<Document>> bulkOperations = new ArrayList<>();

        // Create an InsertOneModel for a document with a "name" value of "Zaynab Omar"
        InsertOneModel<Document> insertDoc = new InsertOneModel<>(new Document("_id", 6)
                                                                .append("name", "Zaynab Omar")
                                                                .append("age", 37));

        // Create a ReplaceOneModel for a document with a "name" value of "Sandy Kane"                                                       
        ReplaceOneModel<Document> replaceDoc = new ReplaceOneModel<>(Filters.eq("_id", 1), 
                                                new Document("name", "Sandy Kane")
                                                    .append("location", "Helena, MT")); 
        
        // Create an UpdateOneModel to change the "name" value of a document                                                                    
        UpdateOneModel<Document> updateDoc = new UpdateOneModel<>(Filters.eq("name", "Zaynab Omar"), 
                                                Updates.set("name", "Zaynab Hassan"));
        
        // Create a DeleteManyModel that matches documents with an "age" value greater than 50                                       
        DeleteManyModel<Document> deleteDoc = new DeleteManyModel<>(Filters.gt("age", 50));
   
        // Add each model instance to the bulkOperations list
        bulkOperations.add(insertDoc);
        bulkOperations.add(replaceDoc);
        bulkOperations.add(updateDoc);
        bulkOperations.add(deleteDoc);

        // Instruct the driver to execute the bulk operations in any order
        // begin bulkWriteNotOrderedExample
        BulkWriteOptions options = new BulkWriteOptions().ordered(false);
        
        // Run the bulk operations on your collection with an options parameter
        collection.bulkWrite(bulkOperations, options); 
        //end bulkWriteNotOrderedExample
    }

    private void bulkWriteExample() {
        // Create a List that will store the bulk operations
        // begin bulkWriteExample
        List<WriteModel<Document>> bulkOperations = new ArrayList<>();


        // Create an InsertOneModel for a document with a "name" value of "Zaynab Omar"
        InsertOneModel<Document> insertDoc = new InsertOneModel<>(new Document("_id", 6)
                                                                .append("name", "Zaynab Omar")
                                                                .append("age", 37));

        // Create a ReplaceOneModel for a document with a "name" value of "Sandy Kane"                                                       
        ReplaceOneModel<Document> replaceDoc = new ReplaceOneModel<>(Filters.eq("_id", 1), 
                                                new Document("name", "Sandy Kane")
                                                    .append("location", "Helena, MT")); 
        
        // Create an UpdateOneModel to change the "name" value of a document                                                                    
        UpdateOneModel<Document> updateDoc = new UpdateOneModel<>(Filters.eq("name", "Zaynab Omar"), 
                                                Updates.set("name", "Zaynab Hassan"));
        
        // Create a DeleteManyModel that matches documents with an "age" value greater than 50                                       
        DeleteManyModel<Document> deleteDoc = new DeleteManyModel<>(Filters.gt("age", 50));
   
        // Add each model instance to the bulkOperations list
        bulkOperations.add(insertDoc);
        bulkOperations.add(replaceDoc);
        bulkOperations.add(updateDoc);
        bulkOperations.add(deleteDoc);

        // Run the bulk operations on your collection
        collection.bulkWrite(bulkOperations);               
        //end bulkWriteExample
    }

    private void insertDocumentsExample(){
        // Create a List that will store the bulk operations
        List<WriteModel<Document>> bulkOperations = new ArrayList<>();

        // begin insertDocumentsExample
        // Create an InsertOneModel for a document with a "name" value of "June Carrie"
        InsertOneModel<Document> juneDoc = new InsertOneModel<>(new Document("name", "June Carrie")
                                                                    .append("age", 17));

        // Create an InsertOneModel for a document with a "name" value of "Kevin Moss"                                                            
        InsertOneModel<Document> kevinDoc = new InsertOneModel<>(new Document("name", "Kevin Moss")
                                                                    .append("age", 22));
        //end insertDocumentsExample
        
        // Add each model instance to the bulkOperations list
        bulkOperations.add(juneDoc);
        bulkOperations.add(kevinDoc);

        // Run the bulk operations on your collection
        collection.bulkWrite(bulkOperations);
    }
    
    private void replaceDocumentsExample(){
        // Create a List that will store the bulk operations
        List<WriteModel<Document>> bulkOperations = new ArrayList<>();

        // Create a ReplaceOneModel to replace a document with a "_id" value of 1
        // begin replaceDocumentsExample
        ReplaceOneModel<Document> celineDoc = new ReplaceOneModel<>(
                                            Filters.eq("_id", 1), 
                                            new Document("name", "Celine Stork")
                                                .append("location", "San Diego, CA"));
        //end replaceDocumentsExample

        // Add the ReplaceOneModel instance to the bulkOperations list
        bulkOperations.add(celineDoc);

        // Run the replace operation on your collection
        collection.bulkWrite(bulkOperations);
    }

    private void updateDocumentsExample(){
        // Create a List that will store the bulk operations
        List<WriteModel<Document>> bulkOperations = new ArrayList<>();

        // Create an UpdateOneModel to modify a document with a "_id" value of 2
        // begin updateDocumentsExample
        UpdateOneModel<Document> updateDoc = new UpdateOneModel<>(
                                            Filters.eq("_id", 2), 
                                            Updates.set("age", 31));
        //end updateDocumentsExample

        // Add the UpdateOneModel instance to the bulkOperations list
        bulkOperations.add(updateDoc);

        // Run the update operation on your collection
        collection.bulkWrite(bulkOperations);
    }

    private void deleteDocumentsExample(){
        // Create a List that will store the bulk operations
        List<WriteModel<Document>> bulkOperations = new ArrayList<>();

        // Create a DeleteOneModel to delete a document with a "_id" value of 1
        // begin deleteDocumentsExample
        DeleteOneModel<Document> deleteDoc = new DeleteOneModel<>(Filters.eq("_id", 1));
        //end deleteDocumentsExample

        // Add the DeleteOneModel instance to the bulkOperations list
        bulkOperations.add(deleteDoc);

        // Run the delete operation on your collection
        collection.bulkWrite(bulkOperations);
    }

    private void preview(){
        // Print the JSON representation of the returned documents
        collection.find().forEach(doc -> System.out.println(doc.toJson()));
    }

    private void setUpCollection(){
        // Delete the collection so each operation starts with an empty collection
        collection.drop();

        // Create a List that will store the bulk operations
        //begin bulkOpsList
        List<WriteModel<Document>> bulkOperations = new ArrayList<>();
        //end bulkOpsList

        // Create an InsertOneModel for a document with a "name" value of "Karen Sandoval"
        InsertOneModel<Document> karen = new InsertOneModel<>(new Document("_id", 1)
                                                                .append("name", "Karen Sandoval")
                                                                .append("age", 31));

        // Create an InsertOneModel for a document with a "name" value of "William Chin"                                                        
        InsertOneModel<Document> william = new InsertOneModel<>(new Document("_id", 2)
                                                                    .append("name", "William Chin")
                                                                    .append("age", 54));

        // Create an InsertOneModel for a document with a "name" value of "Shayla Ray"                                                            
        InsertOneModel<Document> shayla = new InsertOneModel<>(new Document("_id", 8)
                                                                    .append("name", "Shayla Ray")
                                                                    .append("age", 20));

        // Add the model instances to the bulkOperations list
        bulkOperations.add(karen);
        bulkOperations.add(william);
        bulkOperations.add(shayla);

        // Run the bulk operations on your collection
        collection.bulkWrite(bulkOperations);
    }
}
