package fundamentals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.MongoGridFSException;
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
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

public final class GridFSOperations {

    private static void createCustomBucket(MongoClient mongoClient) throws Exception {
        // Access the "mydb" database
        MongoDatabase database = mongoClient.getDatabase("mydb");
        // Create a custom GridFS bucket named "myCustomBucket"
        // start createCustomGridFSBucket
        GridFSBucket gridFSBucket = GridFSBuckets.create(database, "myCustomBucket");
        // end createCustomGridFSBucket
    }

    private static void uploadOptions() {
        // Configure the chunk size and metadata upload options
        // start uploadOptions
        GridFSUploadOptions options = new GridFSUploadOptions()
                .chunkSizeBytes(1048576) // 1MB chunk size
                .metadata(new Document("myField", "myValue"));
        // end uploadOptions
    }

    private static void uploadFromInputStream(GridFSBucket gridFSBucket) throws Exception {
        // start uploadFromInputStream
        String filePath = "/path/to/project.zip";
        try (InputStream streamToUploadFrom = new FileInputStream(filePath) ) {
            // Configure the chunk size and metadata upload options
            GridFSUploadOptions options = new GridFSUploadOptions()
                    .chunkSizeBytes(1048576)
                    .metadata(new Document("type", "zip archive"));

            // Upload your file to the GridFS bucket
            ObjectId fileId = gridFSBucket.uploadFromStream("myProject.zip", streamToUploadFrom, options);

            // Print the ObjectId of the uploaded file
            System.out.println("The file id of the uploaded file is: " + fileId.toHexString());
        }
        // end uploadFromInputStream
    }

    private static void uploadFromOutputStream(GridFSBucket gridFSBucket) throws Exception {
        // Read the file data from the specified path into memory
        // start uploadFromOutputStream
        Path filePath = Paths.get("/path/to/project.zip");
        byte[] data = Files.readAllBytes(filePath);

        // Configure the chunk size and metadata upload options
        GridFSUploadOptions options = new GridFSUploadOptions()
                .chunkSizeBytes(1048576)
                .metadata(new Document("type", "zip archive"));

        try (GridFSUploadStream uploadStream = gridFSBucket.openUploadStream("myProject.zip", options)) {
            // Write the file data to the output stream
            uploadStream.write(data);
            uploadStream.flush();

            // Print the ObjectId of the uploaded file
            System.out.println("The file id of the uploaded file is: " + uploadStream.getObjectId().toHexString());
        } catch (Exception e) {
            System.err.println("The file upload failed: " + e);
        }
        // end uploadFromOutputStream
    }

    private static void findAllFiles(GridFSBucket gridFSBucket) throws Exception {
        // Iterate through the files in the GridFS bucket
        // start findAllFiles
        gridFSBucket.find().forEach(new Consumer<GridFSFile>() {
            @Override
            public void accept(final GridFSFile gridFSFile) {
                // Print details of each file in the GridFS bucket
                System.out.println(gridFSFile);
            }
        });
        // end findAllFiles
    }
    private static void findMatchingFiles(GridFSBucket gridFSBucket) throws Exception {
        // Filter for documents with a "metadata.type" value of "zip archive" and specify an ascending sort
        // start findMatchingFiles
        Bson query = Filters.eq("metadata.type", "zip archive");
        Bson sort = Sorts.ascending("filename");
        
        // Perform a find operation to retrieve documents matching the query and print the results
        gridFSBucket.find(query)
                .sort(sort)
                .limit(5)
                .forEach(new Consumer<GridFSFile>() {
                    @Override
                    public void accept(final GridFSFile gridFSFile) {
                        System.out.println(gridFSFile);
                    }
                });
        //end findMatchingFiles
    }

    private static void downloadToOutputStream(GridFSBucket gridFSBucket) throws Exception {
        // Instruct GridFS to select the original version of the specified file
        // start downloadToStream
        GridFSDownloadOptions downloadOptions = new GridFSDownloadOptions().revision(0);

        // Download a file to an output stream
        try (FileOutputStream streamToDownloadTo = new FileOutputStream("/tmp/myProject.zip")) {
            gridFSBucket.downloadToStream("myProject.zip", streamToDownloadTo, downloadOptions);
            streamToDownloadTo.flush();
        }
        // end downloadToStream
    }

    private static void downloadToMemory(GridFSBucket gridFSBucket) throws Exception {
        // Retrieve a file to download by specifying its ObjectID
        // start downloadToMemory
        ObjectId fileId = new ObjectId("60345d38ebfcf47030e81cc9");

        // Open an input stream to read the specified file and download the file into memory
        try (GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(fileId)) {
            int fileLength = (int) downloadStream.getGridFSFile().getLength();
            byte[] bytesToWriteTo = new byte[fileLength];
            downloadStream.read(bytesToWriteTo);
            System.out.println(new String(bytesToWriteTo, StandardCharsets.UTF_8));
        }
        // end downloadToMemory
    }


    private static void renameFile(GridFSBucket gridFSBucket) throws Exception {
        // Retrieve a file to rename by specifying its ObjectID
        // start renameFile
        ObjectId fileId = new ObjectId("60345d38ebfcf47030e81cc9");
        
        // Rename the specified file to "mongodbTutorial.zip"
        gridFSBucket.rename(fileId, "mongodbTutorial.zip");
        // end renameFile
    }

    private static void deleteFile(GridFSBucket gridFSBucket) throws Exception {
        // Retrieve a file to delete by specifying its ObjectID
        // start deleteFile
        ObjectId fileId = new ObjectId("60345d38ebfcf47030e81cc9");

        // Delete the specified file
        gridFSBucket.delete(fileId);
        // end deleteFile
    }

    private static void dropBucket(MongoClient mongoClient) throws Exception {
        // Access the "mydb" database and delete its default GridFS bucket
        // start dropBucket
        MongoDatabase database = mongoClient.getDatabase("mydb");
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        gridFSBucket.drop();
        // end dropBucket
    }

    public static void main(final String[] args) throws Exception {
        String uri = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Access the "mydb" database and create a GridFS bucket
            // start createGridFSBucket
            MongoDatabase database = mongoClient.getDatabase("mydb");
            GridFSBucket gridFSBucket = GridFSBuckets.create(database);
            // end createGridFSBucket
        }
    }

    private GridFSOperations() {
    }
}

