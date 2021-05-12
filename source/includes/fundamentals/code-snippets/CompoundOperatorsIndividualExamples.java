package com.mycompany.app;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.concurrent.TimeUnit;

public class CompoundOperatorsIndividualExamples {

    public static final String COLLECTION = "compound-test-small-examples";
    public static final String DATABASE = "test";

    public static void main(String[] args) throws InterruptedException {
        CompoundOperatorsIndividualExamples.resetExample();
        CompoundOperatorsIndividualExamples examples = new CompoundOperatorsIndividualExamples();
        examples.findAndUpdateExample();
    }

    public static void resetExample() {
        MongoCollection<Document> collection = CompoundOperators.getCollection();
        UpdateOptions options = new UpdateOptions().upsert(true);
        Bson update = Updates.combine(Updates.set("food", "donut"), Updates.set("color", "green"));
        collection.updateOne(new Document(), update, options);
    }

    public static MongoCollection<Document> getCollection(){
        String uri = System.getenv("DRIVER_URL");
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        return collection;
    }

    private void findAndUpdateExample(){
        MongoCollection<Document> collection = getCollection();
        //start findOneAndUpdate-example
        // <MongoCollection set up code here>
        Bson projection = Projections.excludeId();
        Bson filter = Filters.eq("color","green");
        Bson update = Updates.set("food","pizza");
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().
                projection(projection).
                upsert(true).
                maxTime(5, TimeUnit.SECONDS);

        Document result = collection.findOneAndUpdate(filter, update, options);
        System.out.println(result.toJson());
        //end findOneAndUpdate-example
    }

}
