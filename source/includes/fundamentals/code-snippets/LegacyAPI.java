package com.mycompany.app;

import com.mongodb.*;

public class LegacyAPI {

    private static final String COLLECTION = "test";
    private static final String DATABASE = "test";
    private static final ConnectionString URI = new ConnectionString(System.getenv("DRIVER_URL"));

    public static void main(String[] args) {
        // start legacy-api-example
        MongoClient client = new MongoClient(URI);
        DB db = client.getDB(DATABASE);
        DBCollection col = db.getCollection(COLLECTION);
        DBObject doc = col.find().one();
        System.out.println(doc.toString());
        // end legacy-api-example
    }

}
