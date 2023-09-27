package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.SearchIndexModel;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

public class SearchIndexMethods {

    private static String URI = "mongodb://localhost:59651/?directConnection=true&serverSelectionTimeoutMS=2000";
    private static String DB_NAME = "test_db";
    private static String COLL_NAME = "test_coll";
    private static void setup(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        MongoCollection<Document> collection = database.getCollection(COLL_NAME);
        collection.dropIndexes();
        database.createCollection(COLL_NAME);
    }
    public static void main( String[] args ) {

        try (MongoClient mongoClient = MongoClients.create(URI)) {
            setup(mongoClient);
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection(COLL_NAME);

            // start create-search-index
            BsonDocument index = new BsonDocument("mappings",
                    new BsonDocument("dynamic", BsonBoolean.TRUE));
            collection.createSearchIndex("myIndex", index);
            // end create-search-index

            // start create-search-indexes
            SearchIndexModel indexOne = new SearchIndexModel("myIndex1",
                    new BsonDocument("analyzer", new BsonString("lucene.standard")).append(
                            "mappings", new BsonDocument("dynamic", BsonBoolean.TRUE)));

            SearchIndexModel indexTwo = new SearchIndexModel("myIndex2",
                    new BsonDocument("analyzer", new BsonString("lucene.simple")).append(
                            "mappings", new BsonDocument("dynamic", BsonBoolean.TRUE)));

            collection.createSearchIndexes(Arrays.asList(indexOne, indexTwo));
            // end create-search-indexes

            // start update-search-index
            collection.updateSearchIndex("myIndex",
                    new BsonDocument("analyzer", new BsonString("lucene.simple")).append(
                            "mappings",
                            new BsonDocument("dynamic", BsonBoolean.FALSE)
                                    .append("fields",
                                            new BsonDocument("title",
                                                    new BsonDocument("type", new BsonString("string"))))
                    )
            );
            // end update-search-index

            // start list-search-indexes
            try (MongoCursor<Document> resultsCursor = collection.listSearchIndexes().iterator()) {
                while (resultsCursor.hasNext()) {
                    System.out.println(resultsCursor.next());
                }
            }
            // end list-search-indexes

            // start drop-search-index
            collection.dropSearchIndex("myIndex");
            // end drop-search-index
        }
    }
}
