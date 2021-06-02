package com.mycompany.app;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.event.*;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

public class Monitoring {

    private static final String COLLECTION = "compound-ops";
    private static final String DATABASE = "test";
    private static final ConnectionString URI = new ConnectionString(System.getenv("DRIVER_URL"));

    public static void main(String[] args) throws InterruptedException {
        Monitoring examples = new Monitoring();
        System.out.println("\n---Command Event---\n");
        examples.monitorCommandEvent();
        System.out.println("\n---Cluster Event---\n");
        examples.monitorClusterEvent();
        System.out.println("\n---Connection Pool Event---\n");
        examples.monitorConnectionPoolEvent();
    }

    private void monitorCommandEvent() {
        // start monitor-command-example
        MongoClientSettings settings =
                MongoClientSettings.builder()
                        .applyConnectionString(URI)
                        .addCommandListener(new CommandTimer())
                        .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        // Run some commands to test the timer
        collection.find().first();
        collection.countDocuments();
        mongoClient.close();
        // end monitor-command-example
    }

    private void monitorClusterEvent() {
        // start monitor-cluster-example
        IsReadAndWrite clusterListener = new IsReadAndWrite();
        MongoClientSettings settings =
                MongoClientSettings.builder()
                        .applyConnectionString(URI)
                        .applyToClusterSettings(builder ->
                                builder.addClusterListener(clusterListener))
                        .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        // Run a command to trigger a ClusterDescriptionChangedEvent event
        collection.find().first();
        // end monitor-cluster-example
        mongoClient.close();
    }

    private void monitorConnectionPoolEvent() {
        // start monitor-cp-example
        ConnectionPoolLibrarian cpListener = new ConnectionPoolLibrarian();
        MongoClientSettings settings =
                MongoClientSettings.builder()
                        .applyConnectionString(URI)
                        .applyToConnectionPoolSettings(builder ->
                                builder.addConnectionPoolListener(cpListener))
                        .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        // Run a command to trigger connection pool events
        collection.find().first();
        // end monitor-cp-example
        mongoClient.close();
    }

}

// start command-listener-impl
class CommandTimer implements CommandListener {

    private long startTime;
    private long endTime;

    @Override
    public void commandStarted(final CommandStartedEvent event) {
        this.startTime = System.nanoTime();
    }

    @Override
    public void commandSucceeded(final CommandSucceededEvent event) {
        TimeUnit currentTimeUnit = TimeUnit.MILLISECONDS;
        this.endTime = System.nanoTime();
        long duration = currentTimeUnit.convert(
                (this.endTime - this.startTime),
                TimeUnit.NANOSECONDS);
        System.out.println(String.format("Command '%s' took %d %s",
                event.getCommandName(),
                duration,
                currentTimeUnit.toString().toLowerCase()));
    }

    @Override
    public void commandFailed(final CommandFailedEvent event) {
        System.out.println(String.format("Failed execution of command '%s' with id %s",
                event.getCommandName(),
                event.getRequestId()));
    }
}
// end command-listener-impl

// start cluster-listener-impl
class IsReadAndWrite implements ClusterListener {

    private boolean isWritable;
    private boolean isReadable;
    
    @Override
    public void clusterDescriptionChanged(final ClusterDescriptionChangedEvent event) {
        if (!isWritable) {
            if (event.getNewDescription().hasWritableServer()) {
                isWritable = true;
                System.out.println("Able to write to server");
            }
        } else {
            if (!event.getNewDescription().hasWritableServer()) {
                isWritable = false;
                System.out.println("Unable to write to server");
            }
        }

        if (!isReadable) {
            if (event.getNewDescription().hasReadableServer(ReadPreference.primary())) {
                isReadable = true;
                System.out.println("Able to read from server");
            }
        } else {
            if (!event.getNewDescription().hasReadableServer(ReadPreference.primary())) {
                isReadable = false;
                System.out.println("Unable to read from server");
            }
        }
    }
}
// end cluster-listener-impl

// start cp-listener-impl
class ConnectionPoolLibrarian implements ConnectionPoolListener {

    @Override
    public void connectionCheckedOut(final ConnectionCheckedOutEvent event) {
        System.out.println(String.format("Let me get you the connection with id %s...",
                event.getConnectionId().getLocalValue()));
    }

    @Override
    public void connectionCheckOutFailed(final ConnectionCheckOutFailedEvent event) {
        System.out.println("Something went wrong! Failed to checkout connection.");
    }

}
// end cp-listener-impl
