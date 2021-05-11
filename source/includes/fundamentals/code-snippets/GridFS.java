package fundamentals;

import static com.mongodb.client.model.Filters.eq;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

public final class GridFSDemo {

    private static void uploadFromInputStream(GridFSBucket gridFSBucket) throws Exception {

        // start uploadFromInputStream
        try (InputStream streamToUploadFrom = new FileInputStream("/path/to/Desktop_Wallpapers.zip") ) {

            GridFSUploadOptions options = new GridFSUploadOptions()
                    .chunkSizeBytes(1024)
                    .metadata(new Document("type", "zip archive"));

            ObjectId fileId = gridFSBucket.uploadFromStream("My_Desktop_Wallpapers", streamToUploadFrom, options);

            System.out.println("The file id of the uploaded file is: " + fileId.toHexString());
        }
        // end uploadFromInputStream
    }

    private static void uploadFromOutputStream(GridFSBucket gridFSBucket) throws Exception {

        // start uploadFromOutputStream

        // read largeFile.txt into memory
        Path filePath = Paths.get("/path/to/largeFile.txt");
        byte[] data = Files.readAllBytes(filePath);

        try (GridFSUploadStream uploadStream = gridFSBucket.openUploadStream("sampleData")) {
            uploadStream.write(data);
            uploadStream.close();
            System.out.println("The file id of the uploaded file is: " + uploadStream.getObjectId().toHexString());
        }
        // end uploadFromOutputStream
    }

    public static void main(final String[] args) throws Exception {
        String uri = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(uri)) {

            // get handle to "mydb" database
            MongoDatabase database = mongoClient.getDatabase("mydb");
            database.drop();

            GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        }
    }

    private GridFSDemo() {
    }
}
