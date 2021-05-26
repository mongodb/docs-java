package com.mycompany.app;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.event.*;
import com.mongodb.management.JMXConnectionPoolListener;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.util.ArrayList;
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
        // Run some commands to test the timer
        MongoDatabase database = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> collection = database.getCollection(COLLECTION);
        collection.find().first().toJson();
        collection.countDocuments();
        collection.distinct("music", Filters.empty(), String.class).forEach(i->{});
        // end monitor-command-example
        mongoClient.close();
    }

    private void monitorClusterEvent() {
        // start monitor-cluster-example
        TestClusterListener clusterListener = new TestClusterListener(ReadPreference.primary());
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
        collection.find().first().toJson();
        mongoClient.close();
        // end monitor-cluster-example
    }

    // start monitor-cp-example
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
        // Run a command to trigger a connection pool events event
        collection.find().first();
        mongoClient.close();
        // end monitor-cp-example
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
class TestClusterListener implements ClusterListener {
    private final ReadPreference readPreference;
    private boolean isWritable;
    private boolean isReadable;

    public TestClusterListener(final ReadPreference readPreference) {
        this.readPreference = readPreference;
    }

    @Override
    public void clusterOpening(final ClusterOpeningEvent clusterOpeningEvent) {
        System.out.println(String.format("Cluster with unique client identifier %s opening",
                clusterOpeningEvent.getClusterId().getValue()));
    }

    @Override
    public void clusterClosed(final ClusterClosedEvent clusterClosedEvent) {
        System.out.println(String.format("Cluster with unique client identifier %s closed",
                clusterClosedEvent.getClusterId().getValue()));
    }

    @Override
    public void clusterDescriptionChanged(final ClusterDescriptionChangedEvent event) {


        if (!isWritable) {
            if (event.getNewDescription().hasWritableServer()) {
                isWritable = true;
                System.out.println("Writable server available!");
                event.getNewDescription()
                        .getServerDescriptions().forEach(s -> System.out.println(s.getHosts()));
            }
        } else {
            if (!event.getNewDescription().hasWritableServer()) {
                isWritable = false;
                System.out.println("No writable server available!");
            }
        }

        if (!isReadable) {
            if (event.getNewDescription().hasReadableServer(readPreference)) {
                isReadable = true;
                System.out.println("Readable server available!");
            }
        } else {
            if (!event.getNewDescription().hasReadableServer(readPreference)) {
                isReadable = false;
                System.out.println("No readable server available!");
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
        System.out.println("Something went wrong! Failed to checkout.");
    }

}
// end cp-listener-impl
