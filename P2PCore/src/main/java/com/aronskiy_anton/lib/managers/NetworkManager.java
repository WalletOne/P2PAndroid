package com.aronskiy_anton.lib.managers;

import com.aronskiy_anton.lib.Manager;
import com.aronskiy_anton.lib.P2PCore;
import com.aronskiy_anton.lib.library.Base64;
import com.aronskiy_anton.lib.models.RequestBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import sun.rmi.runtime.Log;


/**
 * Created by aaronskiy on 16.08.2017.
 */

public class NetworkManager extends Manager {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    public final String P2P_RESPONSE_STRING_KEY = "P2PResponseStringKey";
    public final String P2P_RESPONSE_ERROR_CODE_KEY = "P2PResponseErrorCodeKey";

    private InputStream dataStream;
    private List<InputStream> dataStreamList = new ArrayList<>();

    public NetworkManager(P2PCore core) {
        super(core);
    }

    public enum MethodType {

        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE"),
        UNKNOWN("");

        private String id;

        MethodType(String id) {
            this.id = id;
        }

        public String getMethodTypeId() {
            return id;
        }

        public static MethodType fromId(String id) {
            for (MethodType type : values()) {
                if (type.getMethodTypeId().equals(id)) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

    private String makeSignature(String urlString, String timestamp, String requestBody) {
        StringBuilder sb = new StringBuilder();
        sb.append(urlString)
                .append(timestamp)
                .append(requestBody)
                .append(core.getSignatureKey());
        String signatureValue = getSha256(sb.toString());
        String base64;

        byte[] signatureValue2 = getSha256_test(sb.toString());

        try {
            base64 = Base64.encode(signatureValue2);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return base64;
    }

    private String makeSignatureForWeb(Map<String, String> parameters) {
        Map<String, String> treeMap = new TreeMap<>(parameters);
        StringBuilder sb = new StringBuilder();
        for (String value : treeMap.values()) {
            sb.append(value);
        }
        sb.append(core.getSignatureKey());

        byte[] signatureValue = getSha256_test(sb.toString());

        String base64;
        try {
            base64 = Base64.encode(signatureValue);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return base64;
    }


    private byte[] getSha256_test(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(base.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getSha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            //for(byte )
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public InputStream request(String urlString, MethodType method, String parameters) {

        URL url;
        HttpURLConnection urlConnection;
        String timestamp = ISO8601TimeStamp.getISO8601TimeStamp(new Date());
        String bodyAsString = "";


        // timestamp = "2017-08-14T12:09:11";
        bodyAsString = "";

        final String signature = makeSignature(urlString, timestamp, bodyAsString);

        RequestBuilder requestBuilder = RequestBuilder.newBuilder()
                .setMethodType(method)
                .setSignature(signature)
                .setTimestamp(timestamp)
                .setUrlString(urlString)
                .build();
/*
        try {

            url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method.getMethodTypeId());

            urlConnection.setRequestProperty("X-Wallet-PlatformId", core.getPlatformId());
            urlConnection.setRequestProperty("X-Wallet-Timestamp", timestamp);
            urlConnection.setRequestProperty("X-Wallet-Signature", signature);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            urlConnection.connect();

            System.out.println("=======");
            System.out.println("BEGIN NEW REQUEST");
            System.out.println(method.getMethodTypeId() + " : " + urlString);
            System.out.println("BODY: " + bodyAsString);
            System.out.println("Headers:");
            System.out.println("PlatformId: " + urlConnection.getHeaderField("X-Wallet-PlatformId"));
            System.out.println("Timestamp: " + urlConnection.getHeaderField("X-Wallet-Timestamp"));
            System.out.println("Signature: " + urlConnection.getHeaderField("X-Wallet-Signature"));
            System.out.println("Content-Type: " + urlConnection.getHeaderField("Content-Type"));
            System.out.println("END NEW REQUEST\n");
            System.out.println("=======");

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }*/

        return lowLevelRequest_test(requestBuilder);
    }

    private InputStream lowLevelRequest_test(RequestBuilder request) {

        URL url;
        HttpURLConnection connection;
        String bodyAsString = "";

        try {
            url = new URL(request.getUrlString());
            connection = createConnection(url);

            connection.setRequestMethod(request.getMethodType().getMethodTypeId());

            connection.setRequestProperty("X-Wallet-PlatformId", core.getPlatformId());
            connection.setRequestProperty("X-Wallet-Timestamp", request.getTimestamp());
            connection.setRequestProperty("X-Wallet-Signature", request.getSignature());

            System.out.println("=======");
            System.out.println("BEGIN NEW REQUEST");
            System.out.println(request.getMethodType().getMethodTypeId() + " : " + request.getUrlString());
            System.out.println("BODY: " + bodyAsString);
            System.out.println("Headers:");
            System.out.println("PlatformId: " + connection.getHeaderField("X-Wallet-PlatformId"));
            System.out.println("Timestamp: " + connection.getHeaderField("X-Wallet-Timestamp"));
            System.out.println("Signature: " + connection.getHeaderField("X-Wallet-Signature"));
            System.out.println("Content-Type: " + connection.getHeaderField("Content-Type"));
            System.out.println("END NEW REQUEST");
            System.out.println("=======");

            int responseCode = connection.getResponseCode();

            if (responseCode >= 200 && responseCode <= 226) {

            } else {
                throw new IOException("Bad response: " + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            connection.setDoOutput(true);
            return getOutputStream(request, connection);
        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }


    }

    private InputStream getOutputStream(RequestBuilder request, HttpURLConnection connection) throws IOException {
        try {

            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Reader reader = new InputStreamReader(inputStream);

                int contentLength = connection.getContentLength();
                char[] charArray = new char[contentLength];
                reader.read(charArray);

                JSONObject jsonResponse = new JSONObject(charArray.toString());
            }
            else {
                Log.i("", "HTTP Response Code: " + responseCode);
            }
        }
        catch (Exception e) {
        }
    }

    private HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty(CONTENT_TYPE_HEADER, "application/json; charset=utf-8");
        return connection;
    }

    /*
        private InputStream lowLevelRequest(HttpURLConnection connection) {
            try {
                dataStream = connection.getInputStream();
            } catch (IOException e){
                e.printStackTrace();
            }

            //HttpClient client = new HttpClient();

            dataStreamList.add(dataStream);
            //return
        }
    */
    static class ISO8601TimeStamp {

        public static String getISO8601TimeStamp(Date date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat.format(date);
        }
    }

}
