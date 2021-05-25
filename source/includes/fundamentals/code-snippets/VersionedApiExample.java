package fundamentals;

import java.util.Arrays;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class VersionedAPIExample {

    private static void setApiVersionOptions(String uri) {
        // start apiVersionOptions
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .strict(true)
                .deprecationErrors(true)
                .build();
        // end apiVersionOptions
    }

    public static void main(String[] args) {
        // start serverAPIVersion
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        ServerAddress serverAddress = new ServerAddress("localhost", 27017);

        MongoClientSettings settings = MongoClientSettings.builder()
                .serverApi(serverApi)
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(serverAddress)))
                .build();

        MongoClient client = MongoClients.create(settings);
        // end serverAPIVersion
    }
}
