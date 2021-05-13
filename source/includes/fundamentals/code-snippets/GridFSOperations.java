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
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

public final class GridFSOperations {

    private static void uploadFromInputStream(GridFSBucket gridFSBucket) throws Exception {
        
        String filePath = "/Users/ccho/Downloads/download.csv";
        // start uploadFromInputStream
        try (InputStream streamToUploadFrom = new FileInputStream(filePath) ) {

            GridFSUploadOptions options = new GridFSUploadOptions()
                    .chunkSizeBytes(1024)
                    .metadata(new Document("type", "zip archive"));

            ObjectId fileId = gridFSBucket.uploadFromStream("A_Large_File", streamToUploadFrom, options);
            
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
    
    private static void findAllFiles(GridFSBucket gridFSBucket) throws Exception {
        
        // start findAllFiles
        gridFSBucket.find().forEach(new Consumer<GridFSFile>() {
            @Override
            public void accept(final GridFSFile gridFSFile) {
//                gridFSFile.getId()
//                gridFSFile.getLength()
//                gridFSFile.getUploadDate()
//                gridFSFile.toString()
                System.out.println(gridFSFile); //.getFilename());
            }
        });
        // end findAllFiles
    }
    private static void findMatchingFiles(GridFSBucket gridFSBucket) throws Exception {
        // start findMatchingFiles
        gridFSBucket.find().forEach(new Consumer<GridFSFile>() {
            @Override
            public void accept(final GridFSFile gridFSFile) {
                System.out.println(gridFSFile.getFilename());
            }
        });
        //end findMatchingFiles
    }
    
    private static void findFileRevision(GridFSBucket gridFSBucket) throws Exception {
        // start findFileRevision
        //TODO
        // end findFileRevision
    }
    
    private static void downloadToOutputStream(GridFSBucket gridFSBucket) throws Exception {
        ObjectId fileId = new ObjectId();
        
        // start downloadToStream
        FileOutputStream streamToDownloadTo = new FileOutputStream("/tmp/mongodb-tutorial.txt");
        gridFSBucket.downloadToStream(fileId, streamToDownloadTo);
        streamToDownloadTo.close();
        // end downloadToStream
        
        
        // start downloadRevisionToStream
//        streamToDownloadTo = new FileOutputStream("/tmp/mongodb-tutorial.txt");
//        GridFSDownloadOptions downloadOptions = new GridFSDownloadOptions().revision(0);
//        gridFSBucket.downloadToStream("mongodb-tutorial", streamToDownloadTo, downloadOptions);
//        streamToDownloadTo.close();
        // end downloadRevisionToStream
    }
    
    private static void downloadToMemory(GridFSBucket gridFSBucket) throws Exception {
        ObjectId fileId = new ObjectId();
        
        // start downloadToMemory
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(fileId);
        int fileLength = (int) downloadStream.getGridFSFile().getLength();
        byte[] bytesToWriteTo = new byte[fileLength];
        downloadStream.read(bytesToWriteTo);
        downloadStream.close();
        System.out.println(new String(bytesToWriteTo, StandardCharsets.UTF_8));
        // end downloadToMemory
        
//        downloadStream = gridFSBucket.openDownloadStream("sampleData");
//        fileLength = (int) downloadStream.getGridFSFile().getLength();
//        bytesToWriteTo = new byte[fileLength];
//        downloadStream.read(bytesToWriteTo);
//        downloadStream.close();
//
//        System.out.println(new String(bytesToWriteTo, StandardCharsets.UTF_8));

    }
    
    
    private static void renameFile(GridFSBucket gridFSBucket) throws Exception {
        ObjectId fileId = new ObjectId();
        
        // start renameFile
        gridFSBucket.rename(fileId, "mongodbTutorial");
        // end renameFile
    }
        
    private static void deleteFile(GridFSBucket gridFSBucket) throws Exception {
        ObjectId fileId = new ObjectId();
        
        // start deleteFile
        gridFSBucket.delete(fileId);
        // end deleteFile
    }
        
    
    public static void main(final String[] args) throws Exception {
        String uri = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(uri)) {

            // start createGridFSBucket
            MongoDatabase database = mongoClient.getDatabase("mydb");
            GridFSBucket gridFSBucket = GridFSBuckets.create(database);
            // end createGridFSBucket
            
            uploadFromInputStream(gridFSBucket);
            findAllFiles(gridFSBucket);
        }
    }

    private GridFSOperations() {
    }
}
