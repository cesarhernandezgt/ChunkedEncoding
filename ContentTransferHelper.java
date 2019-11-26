package com.test.content.transfer.chunked;

import java.nio.file.CopyOption;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.apache.commons.httpclient.HttpMethod;
import java.io.File;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpClientParams;

public class ContentTransferHelper
{
    public void getContent(final String[] args) {
        final HttpClientParams params = new HttpClientParams();
        params.setSoTimeout(0);
        params.setHttpElementCharset(System.getProperty("file.encoding"));
        final HttpClient httpClient = new HttpClient(params);
        try {
            final GetMethod getMethod = new GetMethod(args[0]);
            final File localFile = new File(String.valueOf(System.currentTimeMillis()) + ".jpg");
            System.out.println("path to new file:" + localFile.getAbsoluteFile());
            InputStream in = null;
            try {
                final int statusCode = httpClient.executeMethod((HttpMethod)getMethod);
                in = getMethod.getResponseBodyAsStream();
                final CopyOption[] copyOptions = { StandardCopyOption.REPLACE_EXISTING };
                Files.copy(in, localFile.toPath(), copyOptions);
                this.handleGetResponse(getMethod, statusCode, localFile);
            }
            finally {
                IOUtils.closeQuietly(in);
                getMethod.releaseConnection();
            }
            IOUtils.closeQuietly(in);
            getMethod.releaseConnection();
        }
        catch (Exception e) {
            System.out.println("HTTP transfer failed: " + e);
        }
    }
    
    private void handleGetResponse(final GetMethod getMethod, final int statusCode, final File newFile) {
        if (statusCode == 200) {
            System.out.println("Transfer complete.");
        }
        else if (statusCode == 404) {
            System.out.println("Received HTTP content transfer error code [" + statusCode + "]. SC_NOT_FOUND.");
        }
        else {
            System.out.println("Received HTTP content transfer error code [" + statusCode + "].");
        }
    }
    
    public static void main(final String[] args) {
        final ContentTransferHelper helper = new ContentTransferHelper();
        helper.getContent(args);
    }
}