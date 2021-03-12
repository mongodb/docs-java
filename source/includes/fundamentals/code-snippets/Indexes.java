package docs.builders;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Indexes.*;

public class Indexes {

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private MongoCollection<Document> collection;

    private Indexes(){
        // begin declaration
        final String uri = System.getenv("DRIVER_REF_URI");

        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("sample_mflix");
        collection = database.getCollection("movies");
        // end declaration
    }

    public static void main(String[] args){
        Indexes indexes = new Indexes();
        indexes.ascendingIndex();
        indexes.descendingIndex();
        indexes.compoundIndexExample();
        indexes.textIndex();
        indexes.hashedIndex();
        indexes.geo2dsphereIndex();
    }

    private void ascendingIndex(){
        // begin ascendingIndex
        Bson ascendingIndex = ascending("title");
        String resultCreateIndex = collection.createIndex(ascendingIndex);
        System.out.println(String.format("Index created: %s", resultCreateIndex));
        // end ascendingIndex
    }

    private void descendingIndex(){
        // begin descendingIndex
        Bson descendingIndex = descending("imdb.rating", "imdb.votes");
        String resultCreateIndex = collection.createIndex(descendingIndex);
        System.out.println(String.format("Index created: %s", resultCreateIndex));
        // end descendingIndex
    }

    private void compoundIndexExample(){
        // begin compoundIndexExample
        Bson compoundIndexExample = compoundIndex(descending("runtime", "year"), ascending("title"));
        String resultCreateIndex = collection.createIndex(compoundIndexExample);
        System.out.println(String.format("Index created: %s", resultCreateIndex));
        // end compoundIndexExample
    }

    private void textIndex(){
        // begin textIndex
        Bson textIndex = text("plot");
        String resultCreateIndex = collection.createIndex(textIndex);
        System.out.println(String.format("Index created: %s", resultCreateIndex));
        // end textIndex
    }

    private void hashedIndex(){
        // begin hashedIndex
        Bson hashedIndex = hashed("released");
        String resultCreateIndex = collection.createIndex(hashedIndex);
        System.out.println(String.format("Index created: %s", resultCreateIndex));
        // end hashedIndex
    }

    private void geo2dsphereIndex(){
        collection = database.getCollection("theaters");
        // begin geo2dsphereIndex
        Bson geo2dsphereIndex = geo2dsphere("location.geo");
        String resultCreateIndex = collection.createIndex(geo2dsphereIndex);
        System.out.println(String.format("Index created: %s", resultCreateIndex));
        // end geo2dsphereIndex
    }
}
