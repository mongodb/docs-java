package fundamentals;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class EnumFieldPojo {


    public static void main(String[] args) {
        String uri = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // start createPenguin
            CodecProvider pojoCodecProvider = PojoCodecProvider.builder().register(Penguin.class).build();
            CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

            MongoDatabase database = mongoClient.getDatabase("myDb").withCodecRegistry(pojoCodecRegistry);
            MongoCollection<Penguin> collection = database.getCollection("penguins", Penguin.class);
            
            Penguin penguin = new Penguin("Robin", 51, Family.ADELIE);
            
            collection.insertOne(penguin);
            // end createPenguin
            
            FindIterable<Penguin> results = collection.find();
            results.forEach(result -> System.out.println(result)); 
            
        }    
    }
}
