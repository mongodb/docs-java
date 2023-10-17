package other;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;

import com.mongodb.AwsCredential;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDbAwsAuth {

    // Placeholder functions

    private static void encodeText() throws UnsupportedEncodingException {
        // Defines the encoding scheme used to translate a string
        // start urlEncode
        String encodedField = java.net.URLEncoder.encode("<fieldValue>".toString(), "ISO-8859-1");
        // end urlEncode
    }
    private static void connectionString() {
        // Instantiates a client and provides AWS credentials to enable the MONGODB-AWS authentication mechanism
        // start connectionString
        MongoClient mongoClient = MongoClients.create("mongodb://<awsKeyId>:<awsSecretKey>@<atlasUri>?authMechanism=MONGODB-AWS");
        // end connectionString
    }

    private static void mechOnlyConnectionString() {
        // Instantiates a client that MongoDB authenticates by using the MONGODB-AWS mechanism
        // start mechOnlyConnectionString
        MongoClient mongoClient = MongoClients.create("mongodb://<atlasUri>?authMechanism=MONGODB-AWS");
        // end mechOnlyConnectionString
    }

    private static void connectionStringSessionToken() {
        // Instantiates a client and provides AWS credentials and a session token to enable the MONGODB-AWS authentication mechanism
        // start connectionStringSessionToken
        MongoClient mongoClient = MongoClients.create("mongodb://<awsKeyId>:<awsSecretKey>@<atlasUri>?authMechanism=MONGODB-AWS&authMechanismProperties=AWS_SESSION_TOKEN:<awsSessionToken>");
        // end connectionStringSessionToken
    }

    private static void mongoCredential() {
        // Defines a MongoCredential instance to specify the authentication mechanism and credentials
        // start mongoCredential
        MongoCredential credential = MongoCredential.createAwsCredential("<awsKeyId>", "<awsSecretKey>".toCharArray());

        // Instantiates a client 
        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                builder.hosts(Arrays.asList(new ServerAddress("<hostname>"))))
                .credential(credential)
                .build());
        // end mongoCredential
    }

    private static void mechOnlyMongoCredential() {
        // start mechOnlyMongoCredential
        MongoCredential credential = MongoCredential.createAwsCredential(null, null);

        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                builder.hosts(Arrays.asList(new ServerAddress("<hostname>"))))
                .credential(credential)
                .build());
        // end mechOnlyMongoCredential
    }

    private static void mongoCredentialSessionTokenConnString() {
        // start mongoCredentialSessionTokenConnString
        MongoCredential credential = MongoCredential.createAwsCredential("<awsKeyId>", "<awsSecretKey>".toCharArray());
        ConnectionString connectionString = new ConnectionString("mongodb://<atlasUri>/?authMechanism=MONGODB-AWS&authMechanismProperties=AWS_SESSION_TOKEN:<awsSessionToken>");

        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .credential(credential)
                .build());
        // end mongoCredentialSessionTokenConnString
    }

    private static void mongoCredentialSessionTokenCredential() {
        // start mongoCredentialSessionTokenCredential
        MongoCredential credential = MongoCredential.createAwsCredential("<awsKeyId>", "<awsSecretKey>".toCharArray()).withMechanismProperty("AWS_SESSION_TOKEN",  "<awsSessionToken>");

        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                builder.hosts(Arrays.asList(new ServerAddress("<hostname>"))))
                .credential(credential)
                .build());
        // end mongoCredentialSessionTokenCredential
    }

    private static void mongoCredentialECSorEC2() {
        // start mongoCredentialECSorEC2
        MongoCredential credential = MongoCredential.createAwsCredential(null, null);
        // end mongoCredentialECSorEC2
    }

    private static void refreshCredentials() {

        // start refreshCredentials
        Supplier<AwsCredential> awsFreshCredentialSupplier = () -> {
            // Add your code here to fetch new credentials here

            // Return the new credentials
            return new AwsCredential("<awsKeyId>", "<awsSecretKey>", "<awsSessionToken>");
        };

        MongoCredential credential = MongoCredential.createAwsCredential(null, null)
                .withMechanismProperty(MongoCredential.AWS_CREDENTIAL_PROVIDER_KEY, awsFreshCredentialSupplier);
        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                    builder.hosts(Collections.singletonList(new ServerAddress("<hostname>", <port>))))
                .credential(credential)
                .build());
        // end refreshCredentials

    }
    public static void main(String[] args) { }
}
