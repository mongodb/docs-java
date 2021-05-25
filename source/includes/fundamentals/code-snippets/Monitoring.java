package com.mycompany.app;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
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
        examples.monitorCommandEvent();

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
        collection.find().first().toJson();
        collection.countDocuments();
        collection.distinct("music", Filters.empty(), String.class).forEach(i->{});
        // end monitor-command-example
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
        long duration = currentTimeUnit.convert((this.endTime - this.startTime), TimeUnit.NANOSECONDS);
        System.out.println(String.format("Successfully executed command '%s' in %d %s",
                event.getCommandName(),
                duration,
                currentTimeUnit.toString().toLowerCase()));
    }

    @Override
    public void commandFailed(final CommandFailedEvent event) {
        System.out.println(String.format("Failed execution of command '%s' with id %s "
                        + "on connection '%s' to server '%s' with exception %s.",
                event.getCommandName(),
                event.getRequestId(),
                event.getConnectionDescription()
                        .getConnectionId(),
                event.getConnectionDescription().getServerAddress(),
                event.getThrowable()));
    }
}
// end command-listener-impl