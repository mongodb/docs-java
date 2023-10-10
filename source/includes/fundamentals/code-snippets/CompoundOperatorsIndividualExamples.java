package com.mycompany.app;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CompoundOperatorsIndividualExamples {

    private static final String COLLECTION = "compound-ops";
    private static final String DATABASE = "test";

    public static void main(String[] args) throws InterruptedException {
        CompoundOperatorsIndividualExamples.resetExample();
        CompoundOperatorsIndividualExamples examples = new CompoundOperatorsIndividualExamples();

        // Run each compound operation example
        examples.findOneAndUpdateExample();
        examples.findOneAndReplaceExample();
        examples.findOneAndDeleteExample();
    }

    public static void resetExample() {
        // Retrieve the "compound-ops" collection and delete its documents
        MongoCollection<Document> collection = getCollection();
        collection.deleteMany(new Document());

        // Insert documents describing food into the collection
        Document insert_pizza = new Document("_id", 1).append("food", "donut").append("color", "green");
        Document insert_pear = new Document("_id", 2).append("food", "pear").append("color", "yellow");
        ArrayList<Document> docs = new ArrayList<Document>();
        docs.add(insert_pizza);
        docs.add(insert_pear);
        collection.insertMany(docs);
    }

    public static MongoCollection<Document> getCollection() {
        // Access the "compound-test" collection in the "test" database
        String uri = System.getenv("DRIVER_URL");
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        return collection;
    }

    private void findOneAndUpdateExample() {
        System.out.println("Starting find one and update example...");
        MongoCollection<Document> collection = getCollection();
        //start findOneAndUpdate-example
        // <MongoCollection set up code here>
        // Define a projection to exclude the "_id" field from the result
        Bson projection = Projections.excludeId();

        // Define a filter to find documents with "color" field value of "green"
        Bson filter = Filters.eq("color", "green");

        // Define an update operation to set the "food" field value to "pizza"
        Bson update = Updates.set("food", "pizza");

        // Define options for the findOneAndUpdate operation
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().
                projection(projection).
                upsert(true).
                maxTime(5, TimeUnit.SECONDS);
        // Run the operation and store your document in its state before the update operation
        Document result = collection.findOneAndUpdate(filter, update, options);
        System.out.println(result.toJson());
        //end findOneAndUpdate-example
    }

    private void findOneAndDeleteExample() {
        System.out.println("Starting find one and delete example...");
        MongoCollection<Document> collection = getCollection();
        //start findOneAndDelete-example
        // <MongoCollection set up code here>

        // Specify a descending sort on the "_id" field.
        Bson sort = Sorts.descending("_id");

        // Define an empty filter to match all documents in the collection
        Bson filter = Filters.empty();

        // Define options to apply the descending sort to the findOneAndDelete operation
        FindOneAndDeleteOptions options = new FindOneAndDeleteOptions().
                sort(sort);

        // Run the operation and print the deleted document
        Document result = collection.findOneAndDelete(filter, options);
        System.out.println(result.toJson());
        //end findOneAndDelete-example
    }


    private void findOneAndReplaceExample() {
        System.out.println("Starting find one and replace example...");
        MongoCollection<Document> collection = getCollection();
        //start findOneAndReplace-example
        // <MongoCollection set up code here>
        // Define a filter to match documents with a "color" field value of "green"
        Bson filter = Filters.eq("color", "green");

        // Create a document that will replace an existing document in the collection
        Document replace = new Document("music", "classical").append("color", "green");

        // Instruct the driver to return the document in its post-replace operation state
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().
                returnDocument(ReturnDocument.AFTER);

        // Run the operation and print the replacement document
        Document result = collection.findOneAndReplace(filter, replace, options);
        System.out.println(result.toJson());
        //end findOneAndReplace-example
    }
}
