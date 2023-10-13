package fundamentals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationAlternate;
import com.mongodb.client.model.CollationCaseFirst;
import com.mongodb.client.model.CollationMaxVariable;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndDeleteOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;

public class CollationCollectionExample {


    private static void aggregatesExample(MongoCollection<Document> collection) {
        // Creates instructions to calculate the frequency of "first_name" values
        // start aggregationExample
        Bson groupStage = Aggregates.group("$first_name", Accumulators.sum("nameCount", 1));
        Bson sortStage = Aggregates.sort(Sorts.ascending("_id"));

        // Constructs a collation object with the German locale that ignores accents
        AggregateIterable<Document> results = collection
                .aggregate(Arrays.asList(groupStage, sortStage))
                .collation(Collation.builder().locale("de").collationStrength(CollationStrength.PRIMARY).build());

        // Prints the JSON representation of the results        
        if (results != null) {
            results.forEach(doc -> System.out.println(doc.toJson()));
        }
        // end aggregationExample
    }

    private static void findAndSortExample(MongoCollection<Document> collection) {
        // start findAndSort
        List<Document> results = new ArrayList<>();

        // Runs a find operation and applies a collation to the sorted results
        collection.find()
                .collation(Collation.builder().locale("de@collation=phonebook").build())
                .sort(Sorts.ascending("first_name")).into(results);

        // Prints the JSON representation of the results          
        if (results != null) {
            results.forEach(doc -> System.out.println(doc.toJson()));
        }
        // end findAndSort
    }

    private static void findOneAndUpdateExample(MongoCollection<Document> collection) {
        // Runs an operation that updates the first matching document with a new "verified" field
        // start findOneAndUpdate
        Document result = collection.findOneAndUpdate(
                Filters.gt("first_name", "Gunter"),
                Updates.set("verified", true),
                new FindOneAndUpdateOptions()
                        .collation(Collation.builder().locale("de@collation=phonebook").build())
                        .sort(Sorts.ascending("first_name"))
                        .returnDocument(ReturnDocument.AFTER));

        // Prints the JSON representation of the results                
        if (result != null) {
            System.out.println("Updated document: " + result.toJson());
        }
        // end findOneAndUpdate
    }

    private static void findOneAndDeleteExample(MongoCollection<Document> collection) {
        List<Document> results = new ArrayList<>();

        // Runs a find operation and stores matching documents in a list
        collection.find(Filters.gt("a", "100")).into(results);

        // Prints the JSON representation of the result list
        results.forEach(r -> System.out.println(r.toJson()));

        // Runs a delete operation specifying a numerical string ordering collation
        // start findOneAndDelete
        Document result = collection.findOneAndDelete(
                Filters.gt("a", "100"),
                new FindOneAndDeleteOptions()
                        .collation(
                                Collation.builder()
                                        .locale("en")
                                        .numericOrdering(true)
                                        .build())
                        .sort(Sorts.ascending("a")));

        // Prints the JSON representation of the deleted document  
        if (result != null) {
            System.out.println("Deleted document: " + result.toJson());
        }
        // end findOneAndDelete
    }

    private static void insertPhonebookDocuments(MongoCollection collection) {
        List<Document> docs = new ArrayList<>();

        docs.add(new Document("first_name", "Klara"));
        docs.add(new Document("first_name", "Gunter"));
        docs.add(new Document("first_name", "Günter"));
        docs.add(new Document("first_name", "Jürgen"));
        docs.add(new Document("first_name", "Hannah"));

        // Inserts the sample documents into a collection
        collection.insertMany(docs);
    }

    private static void insertNumericalStrings(MongoCollection collection) {
        List<Document> docs = new ArrayList<>();

        docs.add(new Document("_id", 1).append("a", "16 apples"));
        docs.add(new Document("_id", 2).append("a", "84 oranges"));
        docs.add(new Document("_id", 3).append("a",  "179 bananas"));

        // Inserts the sample documents into a collection
        collection.insertMany(docs);
    }

    private static void collationBuilder() {
        // Creates collation instructions that specify all possible collation options
        // start collationBuilder
        Collation.builder()
        .caseLevel(true)
        .collationAlternate(CollationAlternate.SHIFTED)
        .collationCaseFirst(CollationCaseFirst.UPPER)
        .collationMaxVariable(CollationMaxVariable.SPACE)
        .collationStrength(CollationStrength.SECONDARY)
        .locale("en_US")
        .normalization(false)
        .numericOrdering(true)
        .build();
        // end collationBuilder
    }

    private static void createCollectionOptions(MongoDatabase database) {
        // Creates a collection and sets its default collation locale as "en_US"
        // start createCollectionOptions
        database.createCollection(
                "items",
                new CreateCollectionOptions().collation(
                        Collation.builder().locale("en_US").build()));
        // end createCollectionOptions
    }

    private static void listIndexes(MongoDatabase database) {
        // start listIndexes
        MongoCollection<Document> collection = database.getCollection("items");
        List<Document> indexes = new ArrayList<>();

        collection.listIndexes().into(indexes);

        // Prints a list of the collection's indexes to ensure successful collation creation
        indexes.forEach(idx -> System.out.println(idx.toJson()));
        // end listIndexes
    }

    private static void createIndex(MongoDatabase database) {
        // start createIndex
        MongoCollection<Document> collection = database.getCollection("items");
        IndexOptions idxOptions = new IndexOptions();
        idxOptions.collation(Collation.builder().locale("en_US").build());

        // Creates an index on the "name" field with a collation and ascending sort order
        collection.createIndex(Indexes.ascending("name"), idxOptions);
        // end createIndex
    }

    private static void createPhonebookIndex(MongoDatabase database) {
        MongoCollection<Document> collection = database.getCollection("phonebook");
        IndexOptions idxOptions = new IndexOptions();

        // Specifies collation options that set punctuation guidelines, comparison levels, and locale
        idxOptions.collation(Collation.builder().locale("de@collation=search").collationStrength(CollationStrength.PRIMARY).collationAlternate(CollationAlternate.SHIFTED).build());
        
        // Creates an index on the "first_name" field with the collation
        collection.createIndex(Indexes.ascending("first_name"), idxOptions);
    }

    private static void indexOperation(MongoCollection collection) {
        // Runs a find operation and applies a collation and ascending sort to the results
        // start indexOperation
        FindIterable<Document> cursor = collection.find()
                .collation(Collation.builder().locale("en_US").build())
                .sort(Sorts.ascending("name"));
        // end indexOperation
        cursor.forEach(doc -> System.out.println(doc.toJson()));
    }

    private static void customCollationOperation(MongoCollection collection) {
        // Runs a find operation and applies a collation and ascending sort to the results
        // start customCollationOperation
        FindIterable<Document> cursor = collection.find()
                .collation(Collation.builder().locale("is").build())
                .sort(Sorts.ascending("name"));
        // end customCollationOperation
    }

    private static void operationCollation(MongoCollection collection) {
        // Runs a find operation that meets the criteria for using the "name" field index
        // start operationCollation
        FindIterable<Document> cursor = collection.find(new Document("name", "cote"))
                .collation(Collation.builder().locale("en_US").build())
                .sort(Sorts.ascending("name"));
        // end operationCollation

        // Prints the JSON representation of the returned documents
        cursor.forEach(doc -> System.out.println(doc.toJson()));
    }

    public static void main(String[] args) {

        String uri = "<MongoDB connection URI>";
        try (MongoClient mongoClient = MongoClients.create(uri)) {

            MongoDatabase database = mongoClient.getDatabase("fundamentals_example");
//            MongoCollection<Document> collection = database.getCollection("phonebook");

        }
    }

}
