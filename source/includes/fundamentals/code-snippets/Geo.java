package com.mycompany.app;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

// begin exampleImports
import java.util.Arrays;
import org.bson.conversions.Bson;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.Position;
import static com.mongodb.client.model.Filters.near;
import static com.mongodb.client.model.Filters.geoWithin;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Projections.excludeId;
// end exampleImports

public class Geo {

    private final MongoClient mongoClient;

    public Geo() {
        final String uri = System.getenv("DRIVER_REF_URI");
        mongoClient = MongoClients.create(uri);

    }

    public static void main(String[] args) {
        Geo geo = new Geo();
        geo.go();
    }

    public void go() {
        // Create a Geo instance and run geospatial queries on your collection
        Geo geo = new Geo();
        System.out.println("Near Example: ");
        geo.nearExample();
        System.out.println("Range Example: ");
        geo.rangeExample();

    }

    private void nearExample() {
        // begin findExample

        // code to set up your mongo client ...
        // Access the "theaters" collection in the "sample_mflix" database
        MongoDatabase database = mongoClient.getDatabase("sample_mflix");
        MongoCollection<Document> collection = database.getCollection("theaters");

        // Store the coordinates of Central Park's Great Lawn in a Point instance
        Point centralPark = new Point(new Position(-73.9667, 40.78));
        
        // Query for theaters betweeen 5,000 and 10,000 meters from the specified Point
        Bson query = near("location.geo", centralPark, 10000.0, 5000.0);

        // Specify a projection to project the documents' "location.address.city" field
        Bson projection = fields(include("location.address.city"), excludeId());
        
        // Run the find operation with the specified query filter and projection
        collection.find(query)
                .projection(projection)
                .forEach(doc -> System.out.println(doc.toJson()));
        // end findExample
    }

    private void rangeExample() {
        // Access the "theaters" collection in the "sample_mflix" database
        MongoDatabase database = mongoClient.getDatabase("sample_mflix");
        MongoCollection<Document> collection = database.getCollection("theaters");
        // begin rangeExample

        // code to set up your mongo collection ...
        // Create a Polygon instance to filter for locations within a section of Long Island
        Polygon longIslandTriangle = new Polygon(Arrays.asList(new Position(-72, 40),
                new Position(-74, 41),
                new Position(-72, 39),
                new Position(-72, 40)));

        // Specify a projection to project the documents' "location.address.city" field
        Bson projection = fields(include("location.address.city"), excludeId());

        // Construct a query with the $geoWithin query operator
        Bson geoWithinComparison = geoWithin("location.geo", longIslandTriangle);
        
        // Run the find operation with the specified query filter and projection
        collection.find(geoWithinComparison)
                .projection(projection)
                .forEach(doc -> System.out.println(doc.toJson()));
        // end rangeExample
    }
}