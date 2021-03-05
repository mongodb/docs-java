// package docs.builders;

// begin imports
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.mongodb.client.model.Updates.*;
// end imports

public class Updates {

    private final MongoCollection<Document> collection;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private Updates() {
        // begin declaration
        final String uri = System.getenv("DRIVER_REF_URI");

        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("builders");
        collection = database.getCollection("updates");
        // end declaration
    }

    public static void main(String[] args) {
        Updates updates = new Updates();
        updates.resetCollection(updates);

        updates.setUpdate();
        updates.resetCollection(updates);
        
        updates.unsetUpdate();
        updates.resetCollection(updates);

        updates.setOnInsertUpdate();
        updates.resetCollection(updates);

        updates.incUpdate();
        updates.resetCollection(updates);

        updates.mulUpdate();
        updates.resetCollection(updates);

        updates.renameUpdate();
        updates.resetCollection(updates);

        updates.minUpdate();
        updates.resetCollection(updates);

        updates.maxUpdate();
        updates.resetCollection(updates);

        updates.currentDateUpdate();
        updates.resetCollection(updates);

        updates.bitwiseOrUpdate();
        updates.resetCollection(updates);

        updates.addToSetUpdate();
        updates.resetCollection(updates);

        updates.popFirstUpdate();
        updates.resetCollection(updates);

        updates.pullAllUpdate();
        updates.resetCollection(updates);

        updates.pullUpdate();
        updates.resetCollection(updates);
        
        updates.pushUpdate();
        updates.resetCollection(updates);

        updates.combineUpdate();
        updates.resetCollection(updates);

    }

    private void setUpdate() {
        // begin setUpdate
        Bson filter = new Document();
        Bson setUpdate = set("qty", 11);
        collection.updateOne(filter, setUpdate);
        // end setUpdate
    }
    private void unsetUpdate() {
        // begin unsetUpdate
        Bson filter = new Document();
        Bson unsetUpdate = unset("qty");
        collection.updateOne(filter, unsetUpdate);
        // end unsetUpdate
    }

    private void setOnInsertUpdate() {
        // begin setOnInsertUpdate
        Bson filter = new Document();
        Bson setOnInsertUpdate = setOnInsert("qty", 7);
        collection.updateOne(filter, setOnInsertUpdate);
        // end setOnInsertUpdate
    }

    private void incUpdate() {
        // begin incUpdate
        Bson filter = new Document();
        Bson incUpdate = inc("qty", 3);
        collection.updateOne(filter, incUpdate);
        // end incUpdate
    }

    private void mulUpdate() {
        // begin mulUpdate
        Bson filter = new Document();
        Bson mulUpdate = mul("qty", 2);
        collection.updateOne(filter, mulUpdate);
        // end mulUpdate
    }

    private void renameUpdate() {
        // begin renameUpdate
        Bson filter = new Document();
        Bson renameUpdate = rename("qty", "quantity");
        collection.updateOne(filter, renameUpdate);
        // end renameUpdate
    }

    private void minUpdate() {
        // begin minUpdate
        Bson filter = new Document();
        Bson minUpdate = min("qty", 2);
        collection.updateOne(filter, minUpdate);
        // end minUpdate
    }

    private void maxUpdate() {
        // begin maxUpdate
        Bson filter = new Document();
        Bson maxUpdate = max("qty", 8);
        collection.updateOne(filter, maxUpdate);
        // end maxUpdate
    }

    private void currentDateUpdate() {
        // begin currentDateUpdate
        Bson filter = new Document();
        Bson currentDateUpdate = currentDate("lastModified");
        collection.updateOne(filter, currentDateUpdate);
        // end currentDateUpdate
    }

    private void bitwiseOrUpdate() {
        // begin bitwiseOrUpdate
        Bson filter = new Document();
        Bson bitwiseOrUpdate = bitwiseOr("qty", 10);
        collection.updateOne(filter, bitwiseOrUpdate);
        // end bitwiseOrUpdate
    }

    private void addToSetUpdate() {
            // begin addToSetUpdate
            Bson filter = new Document();
            Bson addToSetUpdate = addToSet("vendor", "C");
            collection.updateOne(filter, addToSetUpdate);
            // end addToSetUpdate
    }

    private void popFirstUpdate() {
        // begin popFirstUpdate
        Bson filter = new Document();
        Bson popFirstUpdate = popFirst("vendor");
        collection.updateOne(filter, popFirstUpdate);
        // end popFirstUpdate
    }

    private void pullAllUpdate() {
        // begin pullAllUpdate
        Bson filter = new Document();
        Bson pullAllUpdate = pullAll("vendor", Arrays.asList("A", "M"));
        collection.updateOne(filter, pullAllUpdate);
        // end pullAllUpdate
    }

    private void pullUpdate() {
        // begin pullUpdate
        Bson filter = new Document();
        Bson pullUpdate = pull("vendor", "D");
        collection.updateOne(filter, pullUpdate);
        // end pullUpdate
    }

    private void pushUpdate() {
        // begin pushUpdate
        Bson filter = new Document();
        Bson pushUpdate = push("vendor", "C");
        collection.updateOne(filter, pushUpdate);
        // end pushUpdate
    }

    private void combineUpdate() {
        // begin combineUpdate
        Bson filter = new Document();
        Bson combineUpdate = combine(set("color", "purple"), inc("qty", 6), push("vendor", "R"));
        collection.updateOne(filter, combineUpdate);
        // end combineUpdate
    }

    private void preview(){
        Bson filter = new Document();
        List<Document> res = new ArrayList();
        System.out.println(collection.find(filter).into(res));
    }

    public void resetCollection(Updates updates){
        updates.preview();
        collection.drop();
        updates.setupCollection();
    }

    private void setupCollection() {
        String [] vendors = {"A", "D", "M"};
        List<String> vendor_list = Arrays.asList(vendors);

        String today = "03/05/2021";
        Date lastModified = new SimpleDateFormat("MM/dd/yyyy").parse(today,  new ParsePosition(0));

        Document demoDocument = new Document("_id", 1)
                                .append("color", "red")
                                .append("qty", 5)
                                .append("vendor", vendor_list)
                                .append("lastModified", lastModified);

        collection.insertOne(demoDocument);

    }
}
