public package docs;

import org.bson.BsonInt64;
import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.TimeSeriesOptions;

import java.util.Arrays;
import java.util.List;

public class TimeSeriesCollection {
    
    public static void main(String[] args) {
        
        final String uri = System.getenv("DRIVER_REF_URI");

        try(MongoClient mongoClient = MongoClients.create(uri)){

            // begin time series
            MongoDatabase database = mongoClient.getDatabase("fall_weather");
            TimeSeriesOptions tOptions = new TimeSeriesOptions("temperature");
            CreateCollectionOptions coptions = new CreateCollectionOptions().timeSeriesOptions(tOptions);
            
            database.createCollection("september2021", coptions);
            // end time series
            try {
                // begin check collection type
                Document commandResult = database.runCommand(new Document("listCollections", new BsonInt64(1)));
                
                List<String> keys = Arrays.asList("cursor");
                System.out.println("listCollections: " + commandResult.getEmbedded(keys, Document.class));
                // end check collection type
            } catch (MongoException me) {
                System.err.println("An error occurred: " + me);
            }  
        }     
    }
}
class TimeSeries {
    
}
