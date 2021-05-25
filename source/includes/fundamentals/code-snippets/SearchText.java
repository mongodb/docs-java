package docs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.TextSearchOptions;

public class SearchText {
    
    private final MongoCollection<Document> collection;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private SearchText() {
        final String uri = System.getenv("DRIVER_REF_URI");

        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("crudOps");
        collection = database.getCollection("searchText");
    }

    public static void main(String [] args){
        SearchText searchText = new SearchText();

        searchText.setupCoursesCollection();
        System.out.println("Matches for 'Science' (Case Sensitive)");
        searchText.caseSensitiveExample();

        System.out.println("Matches for 'Science' without 'Computer'");
        searchText.negateExample();

        System.out.println("Matches for 'Computer Science'");
        searchText.phraseExample();

        System.out.println("Scores for 'Computer Science'");
        searchText.scoreExample();
    }

    private void caseSensitiveExample(){
        // begin caseSensitiveExample
        TextSearchOptions options = new TextSearchOptions().caseSensitive(true);
        Bson filter = Filters.text("Science", options);
        collection.find(filter).forEach(doc -> System.out.println(doc.toJson()));
        // end caseSensitiveExample
    }
    
    private void negateExample(){
        // begin negateExample
        Bson filter = Filters.text("-Computer Science");
        collection.find(filter).forEach(doc -> System.out.println(doc.toJson()));
        // end negateExample
    }

    private void phraseExample(){
        // begin phraseExample
        Bson filter = Filters.text("\"Computer Science\"");
        collection.find(filter).forEach(doc -> System.out.println(doc.toJson()));
        // end phraseExample
    }

    private void scoreExample(){
        // begin scoreExample
        Bson filter = Filters.text("\"Computer Science\"");
        collection.find(filter).projection(Projections.metaTextScore("score"))
                                .sort(Sorts.metaTextScore("score"))
                                .forEach(doc -> System.out.println(doc.toJson()));
        // end scoreExample
    }

    private void setupCoursesCollection() {
        collection.drop();
        collection.insertMany(Arrays.asList(
            new Document("_id", 1).append("name", "Forensic science").append("major", Arrays.asList("Forensic Science", "Criminal Justice")), 
            new Document("_id", 2).append("name", "Discrete Mathematical Structures for Computer Science").append("major", Arrays.asList("Computer Science", "Mathematics")), 
            new Document("_id", 3).append("name", "Environmental Science").append("major", Arrays.asList("Environmental Science", "Sustainable Management")), 
            new Document("_id", 4).append("name", "Image Processing in Computer science").append("major", Arrays.asList("Artificial Intelligence", "Computer Science"))
        ));
        // begin textIndex
        collection.createIndex(Indexes.text("name"));
        // end textIndex
    }
}
