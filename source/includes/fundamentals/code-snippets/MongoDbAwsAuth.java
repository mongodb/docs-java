package other;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDbAwsAuth {

    // Placeholder functions

    private static void encodeText() throws UnsupportedEncodingException {
        // start uriEncode
        String encodedField = java.net.URLEncoder.encode("<fieldValue>".toString(), "ISO-8859-1");
        // start uriEncode
    }
    private static void connectionString() {
        // start connectionString
        MongoClient mongoClient = MongoClients.create("mongodb://<awsKeyId>:<awsSecretKey>@<atlasUri>?authMechanism=MONGODB-AWS");
        // end connectionString
    }

    private static void connectionStringSessionToken() {
        // start connectionStringSessionToken
        MongoClient mongoClient = MongoClients.create("mongodb://<awsKeyId>:<awsSecretKey>@<atlasUri>?authMechanism=MONGODB-AWS&authMechanismProperties=AWS_SESSION_TOKEN:<awsSessionToken>");
        // end connectionStringSessionToken
    }

    private static void mongoCredential() {
        // start mongoCredential
        MongoCredential credential = MongoCredential.createAwsCredential("<awsKeyId>", "<awsSecretKey>".toCharArray());

        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                builder.hosts(Arrays.asList(new ServerAddress("<atlasUri>"))))
                .credential(credential)
                .build());
        // end mongoCredential
    }

    private static void mongoCredentialSessionTokenConnString() {
        // start mongoCredentialSessionTokenConnString
        MongoCredential credential = MongoCredential.createAwsCredential("<awsKeyId>", "<awsSecretKey>".toCharArray());
        ConnectionString connectionString = new ConnectionString("mongodb://<hostname>:<port>/?authMechanism=MONGODB-AWS&authMechanismProperties=AWS_SESSION_TOKEN:<awsSessionToken>");

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
        ConnectionString connectionString = new ConnectionString("mongodb://<hostname>:<port>/?authMechanism=MONGODB-AWS");

        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .credential(credential)
                .build());
        // end mongoCredentialSessionTokenCredential
    }
    
    
    public static void main(String[] args) { }
}
