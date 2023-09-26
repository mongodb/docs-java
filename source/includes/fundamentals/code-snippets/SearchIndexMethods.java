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
    public static void main( String[] args ) {

        String uri = "mongodb://localhost:59651/?directConnection=true&serverSelectionTimeoutMS=2000";


        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            database.createCollection("test_movies");
            MongoCollection<Document> collection = database.getCollection("test_movies");

            collection.insertOne(new Document("name", "abc"));

            collection.dropSearchIndex("myIndex");

            // start create-search-index
            BsonDocument indexDefinition = new BsonDocument("mappings",
                    new BsonDocument("dynamic", BsonBoolean.TRUE));
            collection.createSearchIndex("myIndex", indexDefinition);
            // end create-search-index

            collection.dropSearchIndex("myIndex1");
            collection.dropSearchIndex("myIndex2");

            // start create-search-indexes
            SearchIndexModel indexModelOne = new SearchIndexModel("myIndex1",
                    new BsonDocument("analyzer", new BsonString("lucene.standard")).append(
                        "mappings", new BsonDocument("dynamic", BsonBoolean.TRUE)));
            SearchIndexModel indexModelTwo = new SearchIndexModel("myIndex2",
                    new BsonDocument("analyzer", new BsonString("lucene.simple")).append(
                        "mappings", new BsonDocument("dynamic", BsonBoolean.TRUE)));

            collection.createSearchIndexes(Arrays.asList(indexModelOne, indexModelTwo));
            // end create-search-indexes

            // start update-search-index
            collection.updateSearchIndex("myIndex",
                    new BsonDocument("analyzer", new BsonString("lucene.simple")).append(
                            "mappings", new BsonDocument("dynamic", BsonBoolean.FALSE)));
            // end update-search-index

            // start drop-search-index
            collection.dropSearchIndex("myIndex");
            // end drop-search-index

            // start list-search-indexes
            try (MongoCursor<Document> resultsCursor = collection.listSearchIndexes().iterator()) {
                while (resultsCursor.hasNext()) {
                    System.out.println(resultsCursor.next());
                }
            }
            // end list-search-indexes
        }
    }
}
