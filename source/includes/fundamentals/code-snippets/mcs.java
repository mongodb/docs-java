package docs;

import static java.util.concurrent.TimeUnit.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

public class MCSettings {

    public static void main(String[] args) {

        System.out.println("ConnectionString:");
        createViaConnectionString();
        System.out.println();

        System.out.println("ClusterSettings:");
        createClusterSettings();
        System.out.println();

        System.out.println("ServerSettings:");
        createServerSettings();
        System.out.println();

        System.out.println("ConnectionPoolSettings:");
        createConnectionPoolSettings();
        System.out.println();

        System.out.println("SocketSettings:");
        createSocketSettings();
        System.out.println();

        System.out.println("SslSettings:");
        createSslSettings();
        System.out.println();
    }

    private static void createViaConnectionString() {
        try {
            //begin ConnectionString
            MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("<your connection uri>"))
                .build());
            //end ConnectionString
            mongoClient.listDatabaseNames().forEach(n -> System.out.println(n));
            mongoClient.close();
        } finally {
            System.out.println("---------------------------------------");
        }
    }

    private static void createClusterSettings() {
        try {
            //begin ClusterSettings
            MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("<your connection uri>"))
                .applyToClusterSettings(builder ->
                    builder.mode(ClusterConnectionMode.MULTIPLE)
                    .serverSelectionTimeout(5, SECONDS))
                .build());
            //end ClusterSettings
            mongoClient.listDatabaseNames().forEach(n -> System.out.println(n));
            mongoClient.close();
        } finally {
            System.out.println("---------------------------------------");
        }
    }

    private static void createServerSettings() {
        try {
            //begin ServerSettings
            MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyToServerSettings(builder ->
                    builder.minHeartbeatFrequency(10, MILLISECONDS)
                    .heartbeatFrequency(3, SECONDS))
                .build());
            //end ServerSettings
            mongoClient.listDatabaseNames().forEach(n -> System.out.println(n));
            mongoClient.close();
        } finally {
            System.out.print("---------------------------------------");
        }
    }

    private static void createSocketSettings() {
        try {
            //begin SocketSettings
            MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyToSocketSettings(builder ->
                    builder.connectTimeout(1000, MILLISECONDS)
                    .readTimeout(10, SECONDS))
                .build());
            //end SocketSettings
            mongoClient.listDatabaseNames().forEach(n -> System.out.println(n));
            mongoClient.close();
        } finally {
            System.out.print("---------------------------------------");
        }
    }

    private static void createConnectionPoolSettings() {
        try {
            //begin ConnectionPoolSettings
            MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyToConnectionPoolSettings(builder ->
                    builder.maxWaitTime(5, SECONDS)
                    .maxSize(1)
                    .maxConnectionLifeTime(100, MILLISECONDS))
                .build());
            //end ConnectionPoolSettings
            mongoClient.listDatabaseNames().forEach(n -> System.out.println(n));
            mongoClient.close();
        } finally {
            System.out.print("---------------------------------------");
        }
    }

    private static void createSslSettings() {
        try {
            ////begin SslSettings
            MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyToSslSettings(builder ->
                    builder.enabled(false)
                    .invalidHostNameAllowed(true))
                .build());
            //end SslSettings
            mongoClient.listDatabaseNames().forEach(n -> System.out.println(n));
            mongoClient.close();
        } finally {
            System.out.print("---------------------------------------");
        }
    }
}