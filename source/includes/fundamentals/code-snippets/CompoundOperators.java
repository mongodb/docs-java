package com.mycompany.app;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;


/**
 * Hello world!
 */




public class CompoundOperators {


    public static final String COLLECTION = "compound-test";
    public static final String DATABASE = "test";


    /**
     * This example demonstrates a race condition that compound operations can help
     * mitigate. Run the example a few times and see what happens in the safe example
     * and the unsafe example. Notice that in the unsafe example often someone is incorrectly
     * notified that they booked a room.
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n---------");
        System.out.println("Begin Unsafe Example:");
        runExample(false);
        System.out.println("Begin Safe Example:");
        runExample(true);
    }

    public static void runExample(boolean safe) throws InterruptedException  {
        CompoundOperators.resetExample();
        Thread thread1;
        Thread thread2;
        if (safe) {
            thread1 = new DemoClientSafe("Jan");
            thread2 = new DemoClientSafe("Pat");
        }
        else {
            thread1 = new DemoClientUnsafe("Jan");
            thread2 = new DemoClientUnsafe("Pat");
        }
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        CompoundOperators.whoGotTheRoom();
        CompoundOperators.resetExample();
        System.out.println("---------");
    }

    public static void whoGotTheRoom() {
        MongoCollection<Document> collection = CompoundOperators.getCollection();
        String guest = collection.find().first().get("guest", String.class);
            System.out.println("Only " + guest + " got the room");
    }

    public static void resetExample() {
        MongoCollection<Document> collection = CompoundOperators.getCollection();
        UpdateOptions options = new UpdateOptions().upsert(true);
        Bson update = Updates.combine(Updates.set("reserved", false), Updates.set("guest", null));
        collection.updateOne(new Document(), update, options);
    }

    public static MongoCollection<Document> getCollection(){
        String uri = System.getenv("DRIVER_URL");
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        return collection;
    }
}


abstract class DemoClient extends Thread {
    String guest;

    DemoClient(String guest) {
        this.guest = guest;
    }

    @Override
    public void run() {
        this.bookARoom();
    }

    public void bookARoom() {
        return;
    }
}

/**
 * An unsafe client. This class will tell users
 * that they have successfully reserved a room even if
 * they have not. This class incorrectly breaks up
 * a findOne and updateOne operation that should be atomic.
 */
class DemoClientUnsafe extends DemoClient {

    DemoClientUnsafe(String guest) {
        super(guest);
    }

    public void bookARoom() {
        MongoCollection<Document> collection = CompoundOperators.getCollection();
        Bson myRoom = collection.find(Filters.eq("reserved", false)).first();
        if (myRoom == null){
            System.out.println("Sorry, we are booked " + this.guest);
            return;
        }
        System.out.println("You got the room " + this.guest);
        Bson update = Updates.combine(Updates.set("reserved", true), Updates.set("guest", guest));
        collection.updateOne(Filters.eq("_id", myRoom.toBsonDocument().get("_id")), update);
    }

}

/**
 * A safe client. This class uses the atomic
 * findOneAndUpdate operator to correctly group
 * finding a free room and marking the room as reserved
 * into a single operation.
 */
class DemoClientSafe extends DemoClient {

    DemoClientSafe(String guest) {
        super(guest);
    }

    public void bookARoom(){
        MongoCollection<Document> collection = CompoundOperators.getCollection();
        Bson update = Updates.combine(Updates.set("reserved", true), Updates.set("guest", guest));
        Bson myRoom = collection.findOneAndUpdate(Filters.eq("reserved", false), update);
        if (myRoom == null){
            System.out.println("Sorry, we are booked " + this.guest);
            return;
        }
        System.out.println("You got the room " + this.guest);
    }

}