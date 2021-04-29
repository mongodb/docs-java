package com.mycompany.app;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import org.bson.Document;
import org.bson.conversions.Bson;

// begin importsFindExample
import static com.mongodb.client.model.Filters.near;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Projections.excludeId;
//end importsFindExample'

public class Geo {

    private final MongoClient mongoClient;

    public Geo() {
        final String uri = System.getenv("DRIVER_REF_URI");
        mongoClient = MongoClients.create(uri);

    }

    public void go(){
        Geo geo = new Geo();
        System.out.println("Near Example: ");
        geo.nearExample();

    }

    private void nearExample(){
        // begin findExample

        // code to set up your mongo client ...
        MongoDatabase database = mongoClient.getDatabase("sample_mflix");
        MongoCollection<Document> collection = database.getCollection("theaters");
        final Point centralPark = new Point(new Position(-73.9667, 40.78));
        final Bson query = near("location.geo", centralPark, 10000.0, 5000.0);
        final Bson projection = fields(include("location.address.city"), excludeId());
        collection.find(query)
                .projection(projection)
                .forEach(doc -> System.out.println(doc.toJson()));
        // end findExample
    }

}