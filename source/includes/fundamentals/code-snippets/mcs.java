package docs;

import static java.util.concurrent.TimeUnit.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;

import java.util.Collections;

public class MCSettings {

    public static void main(String [] args){

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

    private static void createViaConnectionString(){
        try {
            //begin connectionString
            MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString("<your connection uri>"))
                        .build());
            //end connectionString
            System.out.println(mongoClient.getClusterDescription());
            mongoClient.close();
        }
        finally {
            System.out.println("---------------------------------------");
        }
    } 
    
    private static void createClusterSettings(){
        try {
            //begin clusterSettings
            MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.serverSelectionTimeout(5, MILLISECONDS)
                                        .mode(ClusterConnectionMode.LOAD_BALANCED)
                                        .hosts(Collections.singletonList(new ServerAddress("host1", 27017))))
                        .build());
            //end clusterSettings
            System.out.println(mongoClient.getClusterDescription().getClusterSettings());
            mongoClient.close();
        }
        finally {
            System.out.println("---------------------------------------");
        }
    } 

    private static void createServerSettings(){
       try {
           //begin ServerSettings
            MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToServerSettings(builder -> 
                                builder.minHeartbeatFrequency(10, MILLISECONDS)
                                        .heartbeatFrequency(3, SECONDS))
                        .build());
            //end ServerSettings
            System.out.println(mongoClient.getClusterDescription().getServerSettings());
            mongoClient.close();
       }
       finally {
            System.out.print("---------------------------------------");
        }
    } 

    private static void createSocketSettings(){
        try {
            //begin SocketSettings
            MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToSocketSettings(builder -> 
                                builder.connectTimeout(1000, MILLISECONDS)
                                        .readTimeout(10, SECONDS))
                        .build());
            //end SocketSettings
            System.out.println(mongoClient.getClusterDescription());
            mongoClient.close();
        }
        finally {
            System.out.print("---------------------------------------");
        }
     } 

     private static void createConnectionPoolSettings(){
        try {
            //begin ConnectionPoolSettings
            MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToConnectionPoolSettings(builder -> 
                                builder.maxWaitTime(5, SECONDS)
                                        .maxSize(1)
                                        .maxConnectionLifeTime(20, MILLISECONDS))
                        .build());
            //end ConnectionPoolSettings
            System.out.println(mongoClient.getClusterDescription());
            mongoClient.close();
        }
        finally {
            System.out.print("---------------------------------------");
        }
     } 

     private static void createSslSettings(){
        
        try {
            ////begin SslSettings
            MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                     .applyToSslSettings(builder -> 
                            builder.enabled(true)
                                    .invalidHostNameAllowed(false))
                     .build());
            //end SslSettings
            System.out.println(mongoClient.getClusterDescription());
            mongoClient.close();
        }
        finally {
            System.out.print("---------------------------------------");
        }
     }
}
