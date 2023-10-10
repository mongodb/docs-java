package com.mycompany.app;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;


public class CompoundOperators {


    private static final String COLLECTION = "compound-test";
    private static final String DATABASE = "test";


    /**
     * This example demonstrates a race condition that compound operations can help
     * fix. Run the example a few times and see what happens in the safe example
     * and the unsafe example. Notice that in the unsafe example often someone is incorrectly
     * notified that they booked a room.
     */
    public static void main(String[] args) throws InterruptedException {
        // Run the compound operation examples and print the results
        System.out.println("\n---------");
        System.out.println("Begin Unsafe Example:");
        runExample(false);
        System.out.println("Begin Safe Example:");
        runExample(true);
    }

    public static void runExample(boolean safe) throws InterruptedException  {
        CompoundOperators.resetExample();

        // Create two threads representing guests
        Thread thread1;
        Thread thread2;
        String guest1 = "Jan";
        String guest2 = "Pat";
        if (safe) {
            thread1 = new DemoClientSafe(guest1);
            thread2 = new DemoClientSafe(guest2);
        }
        else {
            thread1 = new DemoClientUnsafe(guest1);
            thread2 = new DemoClientUnsafe(guest2);
        }

        // Start the execution of the two threads
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        // Determine who successfully booked the room
        CompoundOperators.whoGotTheRoom();
        CompoundOperators.resetExample();
        System.out.println("---------");
    }

    public static void whoGotTheRoom() {
        // Access the "compound-test" collection
        MongoCollection<Document> collection = CompoundOperators.getCollection();

        // Retrieve and print the guest who successfully booked the room
        String guest = collection.find().first().get("guest", String.class);
            System.out.println("Only " + guest + " got the room");
    }

    public static void resetExample() {
        // Access the "compound-test" collection
        MongoCollection<Document> collection = getCollection();

        // Clear any documents from the collection, then insert a document representing the "Blue Room"
        collection.deleteMany(new Document());
        Document insert_room = new Document("_id", 1).append("reserved", false).append("guest", null).append("room", "Blue Room");
        collection.insertOne(insert_room);
    }

    public static MongoCollection<Document> getCollection(){
        // Access the "compound-test" collection in the "test" database
        String uri = System.getenv("DRIVER_URL");
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        return collection;
    }
}


abstract class DemoClient extends Thread {
    String guest;
    MongoCollection<Document> collection;

    // Constructor for initializing a client with a guest name
    DemoClient(String guest) {
        this.guest = guest;

        // Retrieve the collection used for booking
        this.collection = CompoundOperators.getCollection();
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

    // Constructor for initializing an unsafe client with a guest name
    DemoClientUnsafe(String guest) {
        super(guest);
    }

    // start the-unsafe-book-a-room
    public void bookARoom() {
         // Define a filter to find an available room
        Bson filter = Filters.eq("reserved", false);

        // Attempt to find the first available room
        Document myRoom = this.collection.find(filter).first();

        // If no available rooms were found, print a message informing the guest
        if (myRoom == null){
            System.out.println("Sorry, we are booked " + this.guest);
            return;
        }

        // Get the name of the available room
        String myRoomName = myRoom.getString("room");

        // Inform the guest that they successfully booked the room
        System.out.println("You got the " + myRoomName + " " + this.guest);

        // Prepare an update to mark the room as reserved with the guest's name
        Bson update = Updates.combine(Updates.set("reserved", true), Updates.set("guest", guest));
        
        // Define a filter to update the room that will be reserved
        Bson roomFilter = Filters.eq("_id", myRoom.get("_id", Integer.class));

        // Perform the update operation to mark the room as reserved
        this.collection.updateOne(roomFilter, update);
    }
    // end the-unsafe-book-a-room

}

/**
 * A safe client. This class uses the atomic
 * findOneAndUpdate operator to correctly group
 * finding a free room and marking the room as reserved
 * into a single operation.
 */
class DemoClientSafe extends DemoClient {

    // Constructor for initializing a safe client with a guest name
    DemoClientSafe(String guest) {
        super(guest);
    }

    // start the-safe-book-a-room
    public void bookARoom(){
        // Prepare an update operation that will mark an available room as reserved
        Bson update = Updates.combine(Updates.set("reserved", true), Updates.set("guest", guest));
        
        // Define a filter to find an available room
        Bson filter = Filters.eq("reserved", false);

        // Atomically find an available room and mark it as reserved
        Document myRoom = this.collection.findOneAndUpdate(filter, update);
       
        // If no available rooms were found, print a message informing the guest
        if (myRoom == null){
            System.out.println("Sorry, we are booked " + this.guest);
            return;
        }

        // Get the name of the available room and inform the guest they've reserved the room
        String myRoomName = myRoom.getString("room");
        System.out.println("You got the " + myRoomName + " " + this.guest);
    }
    // end the-safe-book-a-room

}