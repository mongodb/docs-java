package docs.indexes;

// begin imports
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoCommandException;
import com.mongodb.client.*;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import org.apache.log4j.BasicConfigurator;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
// end imports

public class IndexPage {

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private MongoCollection<Document> collection;

    private IndexPage() {
        BasicConfigurator.configure();

        // begin declaration
        final String uri = "mongodb+srv://<atlas-uri>/<dbname>?retryWrites=true&w=majority";

        // Create a Client instance and access the "movies" collection in "sample_mflix"
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("sample_mflix");
        collection = database.getCollection("movies");
        // end declaration
    }

    public static void main(String[] args) {
        IndexPage page = new IndexPage();

        // Run a series of createIndex() operations
        page.singleIndex();
        page.compoundIndex();
        page.wildCardIndex();
        page.multiKeyIndex();
        page.textIndex();
        page.geoSpatialIndex();
        page.uniqueIndex();
    }

    private void singleIndex() {
        System.out.println("single index");

        // Create an index in ascending order on the "title" field
        // begin single index
        String resultCreateIndex = collection.createIndex(Indexes.ascending("title"));
        System.out.println(String.format("Index created: %s", resultCreateIndex));
        // end single index

        // Specify a query covered by the index that matches documents with a "title" of "Batman"
        // begin covered single query
        Bson filter = eq("title", "Batman");
        Bson sort = Sorts.ascending("title");
        Bson projection = fields(include("title"), excludeId());
        FindIterable<Document> cursor = collection.find(filter).sort(sort).projection(projection);
        // end covered single query
        
        // Iterate through the operation results and print each matched document
        cursor.forEach(doc -> System.out.println(doc));

    }

    private void compoundIndex() {
        System.out.println("compound index");

        // Create a compound index on the "type" and "rated" fields
        // begin compound index
        String resultCreateIndex = collection.createIndex(Indexes.ascending("type", "rated"));
        System.out.println(String.format("Index created: %s", resultCreateIndex));
        // end compound index

        // Specify a query covered by the index that matches movie documents with a "rated" value of "G"
        // begin covered compound query
        Bson filter = and(eq("type", "movie"), eq("rated", "G"));
        Bson sort = Sorts.ascending("type", "rated");
        Bson projection = fields(include("type", "rated"), excludeId());
        FindIterable<Document> cursor = collection.find(filter).sort(sort).projection(projection);
        // end covered compound query

        // Iterate through the operation results and print each matched document
        cursor.forEach(doc -> System.out.println(doc));
    }

    private void multiKeyIndex() {
        System.out.println("multikey index");
        // Create a compound, multikey index on the "rated", "genres", and "title" fields
        // begin multikey index
        String resultCreateIndex = collection.createIndex(Indexes.ascending("rated", "genres", "title"));
        System.out.println(String.format("Index created: %s", resultCreateIndex));
        // end multikey index

        // Specify a query covered by the index that matches documents with specified field values
        // begin covered multikey query
        Bson filter = and(eq("genres", "Animation"), eq("rated", "G"));
        Bson sort = Sorts.ascending("title");
        Bson projection = fields(include("title", "rated"), excludeId());
        FindIterable<Document> cursor = collection.find(filter).sort(sort).projection(projection);
        // end covered multikey query

        // Iterate through the operation results and print each matched document
        cursor.forEach(doc -> System.out.println(doc));
    }

    private void textIndex() {
        System.out.println("text index");
        // Create a text index on the "plot" field
        // begin text index
        try {
            String resultCreateIndex = collection.createIndex(Indexes.text("plot"));
            System.out.println(String.format("Index created: %s", resultCreateIndex));
        
        // Generate an error if a text index already exists with a different configuration 
        } catch (MongoCommandException e) {
            if (e.getErrorCodeName().equals("IndexOptionsConflict"))
                System.out.println("there's an existing text index with different options");
        }
        // end text index

        // Define a query covered by the index that matches documents with a specified "plot" value 
        // begin text query
        Bson filter = text("java coffee shop");
        Bson projection = fields(include("fullplot"), excludeId());
        FindIterable<Document> cursor = collection.find(filter).projection(projection);
        // end text query
        cursor.forEach(doc -> System.out.println(doc));
    }

    private void geoSpatialIndex() {
        System.out.println("geospatial index");

        // Access the "theaters" collection
        collection = database.getCollection("theaters");

        // Create a geospatial index on the "location.geo" field
        // begin geospatial index
        try {
            String resultCreateIndex = collection.createIndex(Indexes.geo2dsphere("location.geo"));
            System.out.println(String.format("Index created: %s", resultCreateIndex));
        
        // Generate an error if a geospatial index already exists with a different configuration 
        } catch (MongoCommandException e) {
            if (e.getErrorCodeName().equals("IndexOptionsConflict"))
                System.out.println("there's an existing text index with different options");
        }
        // end geospatial index

        // begin geospatial query
        // Store the coordinates of the NY MongoDB headquarters in a Point variable
        Point refPoint = new Point(new Position(-73.98456, 40.7612));

        // Perform a geospatial query covered by the "location.geo" index
        Bson filter = near("location.geo", refPoint, 1000.0, 0.0);
        FindIterable<Document> cursor = collection.find(filter);
        // end geospatial query

        // Iterate through the operation results and print each matched document
        cursor.forEach(doc -> System.out.println(doc));
    }

    private void uniqueIndex() {
        System.out.println("unique index");

        // Access the "theaters" collection
        collection = database.getCollection("theaters");

        // Create a unique index on the "theaterID" field
        // begin unique index
        try {
            IndexOptions indexOptions = new IndexOptions().unique(true);
            String resultCreateIndex = collection.createIndex(Indexes.descending("theaterId"), indexOptions);
            System.out.println(String.format("Index created: %s", resultCreateIndex));
        
        // Generate an error if any duplicate values exist on the "theaterID" field
        } catch (DuplicateKeyException e) {
            System.out.printf("duplicate field values encountered, couldn't create index: \t%s\n", e);
        }
        // end unique index
    }
    private void wildcardIndex() {
        System.out.println("wildcard index");
        
        // Access the "theaters" collection
        collection = database.getCollection("theaters");

        // Create an ascending wildcard index on all values of the "location" field
        // begin wildcard index
        String resultCreateIndex = collection.createIndex(Indexes.ascending("location.$**")); 
        System.out.println(String.format("Index created: %s", resultCreateIndex));
        // end wildcard index
    }
}
