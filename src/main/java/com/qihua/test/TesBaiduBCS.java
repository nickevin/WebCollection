package com.qihua.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.model.DownloadObject;
import com.baidu.inf.iis.bcs.model.ObjectListing;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.model.ObjectSummary;
import com.baidu.inf.iis.bcs.request.GetObjectRequest;
import com.baidu.inf.iis.bcs.request.ListObjectRequest;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;

public class TesBaiduBCS {

    static String host = "bcs.duapp.com";
    static String accessKey = "sWUwI8vgZyAs4A4d52DANq2h";
    static String secretKey = "FLYuuRkbcYKDR6AjrNNB7AxWrt2C677t";
    static String bucket = "qihuacloud";

    static String object = "/2015-01-24_6d0a3ceca6eb40e6b5c56c8747cd9142.xlsx";
    static File destFile = new File("2015-01-24_6d0a3ceca6eb40e6b5c56c8747cd9142.xlsx");

    public static void main(String[] args) {
        BCSCredentials credentials = new BCSCredentials(accessKey, secretKey);
        BaiduBCS baiduBCS = new BaiduBCS(credentials, host);
        baiduBCS.setDefaultEncoding("UTF-8");

        // putObjectByFile(baiduBCS);

        // listObject(baiduBCS);

        getObjectWithDestFile(baiduBCS);
    }

    public static void putObjectByFile(BaiduBCS baiduBCS) {
        PutObjectRequest request = new PutObjectRequest(bucket, object, createSampleFile());
        ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setContentType("text/html");
        request.setMetadata(metadata);
        BaiduBCSResponse<ObjectMetadata> response = baiduBCS.putObject(request);
        ObjectMetadata objectMetadata = response.getResult();
        System.out.println("x-bs-request-id: " + response.getRequestId());
        System.out.println(objectMetadata);
    }

    private static File createSampleFile() {
        try {
            File file = File.createTempFile("java-sdk-", ".txt");
            file.deleteOnExit();

            Writer writer = new OutputStreamWriter(new FileOutputStream(file));
            writer.write("01234567890123456789\n");
            writer.write("01234567890123456789\n");
            writer.write("01234567890123456789\n");
            writer.write("01234567890123456789\n");
            writer.write("01234567890123456789\n");
            writer.close();

            return file;
        } catch (IOException e) {
            System.out.println("tmp file create failed.");

            return null;
        }
    }

    private static void listObject(BaiduBCS baiduBCS) {
        ListObjectRequest listObjectRequest = new ListObjectRequest(bucket);
        listObjectRequest.setStart(0);
        listObjectRequest.setLimit(20);
        // ------------------by dir
        {
            // prefix must start with '/' and end with '/'
            // listObjectRequest.setPrefix("/1/");
            // listObjectRequest.setListModel(2);
        }
        // ------------------only object
        {
            // prefix must start with '/'
            // listObjectRequest.setPrefix("/1/");
        }
        BaiduBCSResponse<ObjectListing> response = baiduBCS.listObject(listObjectRequest);
        System.out.println("we get [" + response.getResult().getObjectSummaries().size() + "] object record.");

        for (ObjectSummary os : response.getResult().getObjectSummaries()) {
            System.out.println(os.toString());
        }
    }

    private static InputStream getObjectWithDestFile(BaiduBCS baiduBCS) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, object);
        BaiduBCSResponse<DownloadObject> response = baiduBCS.getObject(getObjectRequest, destFile);

        System.out.println("TesBaiduBCS.getObjectWithDestFile()" + response.getResult().getObject().getBytes().length);

        return response.getResult().getContent();
    }

}
