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

        // searchText.setupCoursesCollection();
        System.out.println("Matches for 'Computer'");
        searchText.wordExample();

        System.out.println("Matches for 'Computer or Science'");
        searchText.multiWordExample();

        System.out.println("Matches for 'Computer' without 'Science'");
        searchText.negateExample();

        System.out.println("Matches for 'Computer Science'");
        searchText.phraseExample();

        System.out.println("Scores for 'Computer Science'");
        searchText.scoreExample();
    }

    private void wordExample(){
        // begin wordExample
        Bson filter = Filters.text("Computer");
        collection.find(filter).forEach(doc -> System.out.println(doc.toJson()));
        // end wordExample
    }

    private void multiWordExample(){
        // begin multiWordExample
        Bson filter = Filters.text("Computer Science");
        collection.find(filter).forEach(doc -> System.out.println(doc.toJson()));
        // end multiWordExample
    }
    
    private void negateExample(){
        // begin negateExample
        Bson filter = Filters.text("Computer -Science");
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
            new Document("_id", 1).append("course", "Artificial Intelligence"), 
            new Document("_id", 2).append("course", "Discrete Mathematical Structures for Computer Science"), 
            new Document("_id", 3).append("course", "Web Database Applications"), 
            new Document("_id", 4).append("course", "Computer Science"),
            new Document("_id", 5).append("course", "Computer Hacking Revealed"),
            new Document("_id", 6).append("course", "Advanced Computer Game Programming"),
            new Document("_id", 7).append("course", "Image Processing in Computer Science")
        ));
        // begin textIndex
        collection.createIndex(Indexes.text("course"));
        // end textIndex
    }
}
