package sample.batch.scb;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.*;
import java.util.List;

public class Sampleclass {


    private static String efsPath = "/efs_of_container/";
    //    private static String efsPath = "/etc/";
    private static String sourceFileLocation = "sample_data.csv";


    public static void main(String[] args) throws IOException {
        System.out.println("AWS Batch docker job is executing");

        if (args.length > 0) {
            System.out.println("Your input parameter is: " + args[0]);
        }

        uploadFileToS3();

        System.out.println("End program");
    }


    private static void uploadFileToS3() {

        System.out.println("Uploading file to S3 function is executing");


        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
//                .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();


        String bucketName = "poc-sandbox-scb-batch-bucket";
        String objectKey = "report_engine_file/sample_data2.csv";
        File file = new File(sourceFileLocation);

        System.out.println("Deleting an existing file");
        s3client.deleteObject(new DeleteObjectRequest(bucketName, objectKey));

        System.out.println("Uploading file in progress..");
        long startTime = System.currentTimeMillis();

        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, file);
        s3client.putObject(putObjectRequest);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        System.out.println("AWS S3 Java uploaded is completed with: " + totalTime + "ms");



    }


    private static void createNewFileToEFS() {
        System.out.println("create file in efs disk is executing");
        String fileName = efsPath + "default_file.txt";
        File myObj = new File(fileName);

        try {
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void writeFileToEFS() throws IOException {
        System.out.println("Deleting an existing file");
        File f = new File(efsPath + "sample_data2.csv");           //file to be delete
        if (f.delete())                      //returns Boolean value
        {
            System.out.println(f.getName() + " deleted");   //getting and printing the file name
        }

        System.out.println("Copying test file is executing ");
//
//
        File sourceFile = new File(sourceFileLocation);
        File desFile = new File(efsPath + sourceFile.getName());

        long startTime = System.currentTimeMillis();

        copyFileUsingStream(sourceFile, desFile);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        System.out.println("Copying file is completed with processing time of " + totalTime + "ms");

    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
