package com.aronskiy_anton.sdk.managers;

import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;

import com.aronskiy_anton.sdk.Manager;
import com.aronskiy_anton.sdk.P2PCore;
import com.aronskiy_anton.sdk.W1P2PException;
import com.aronskiy_anton.sdk.internal.Utility;
import com.aronskiy_anton.sdk.library.Base64;
import com.aronskiy_anton.sdk.models.RequestBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.logging.Logger;


/**
 * Created by aaronskiy on 16.08.2017.
 */

public class NetworkManager extends Manager {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    private static final String ISO_8601_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZ";

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

    public String makeSignatureForWeb(Map<String, String> parameters) {
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
            return digest.digest(base.getBytes("UTF-8"));
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

    public List<Response> request(String urlString, MethodType method, String parameters) {

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

    private List<Response> lowLevelRequest_test(RequestBuilder request) {
/*
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
                try {
                    serializeToUrlConnection(request, connection);
                } catch (Exception ex) {
                    List<Response> responses = Response.constructErrorResponses(requests.getRequests(), null, new FacebookException(ex));
                    runCallbacks(requests, responses);
                    return responses;
                }

                List<Response> responses = executeConnectionAndWait(connection, requests);

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
*/
        return executeBatchAndWait(request);
    }

    private List<Response> executeBatchAndWait(RequestBuilder requests) {

        HttpURLConnection connection = null;
        try {
            connection = toHttpConnection(requests);
        } catch (Exception ex) {
            List<Response> responses = Response.constructErrorResponses(requests, null, new W1P2PException(ex));
            //runCallbacks(requests, responses);
            return responses;
        }

        List<Response> responses = executeConnectionAndWait(connection, requests);
        return responses;
    }

    private List<Response> executeConnectionAndWait(HttpURLConnection connection, RequestBuilder requests) {
        List<Response> responses = Response.fromHttpConnection(connection, requests);

        Utility.disconnectQuietly(connection);
/*
        int numRequests = requests.size();
        if (numRequests != responses.size()) {
            throw new W1P2PException(String.format("Received %d responses while expecting %d", responses.size(),
                    numRequests));
        }

        runCallbacks(requests, responses);
*/
        // See if any of these sessions needs its token to be extended. We do this after issuing the request so as to
        // reduce network contention.
      /*  HashSet<Session> sessions = new HashSet<Session>();
        for (Request request : requests) {
            if (request.session != null) {
                sessions.add(request.session);
            }
        }
        for (Session session : sessions) {
            session.extendAccessTokenIfNeeded();
        }
*/
        return responses;
    }

    private HttpURLConnection toHttpConnection(RequestBuilder requests) {

        URL url = null;
        try {
            url = new URL(requests.getUrlString());
        } catch (MalformedURLException e) {
            throw new W1P2PException("could not construct URL for request", e);
        }

        HttpURLConnection connection;
        try {
            connection = createConnection(url);
            connection.setRequestMethod(requests.getMethodType().getMethodTypeId());

            connection.setRequestProperty("X-Wallet-PlatformId", core.getPlatformId());
            connection.setRequestProperty("X-Wallet-Timestamp", requests.getTimestamp());
            connection.setRequestProperty("X-Wallet-Signature", requests.getSignature());
            //serializeToUrlConnection(requests, connection);
        } catch (IOException e) {
            throw new W1P2PException("could not construct request body", e);
        } /* (JSONException e) {
            throw new W1P2PException("could not construct request body", e);
        }*/

        return connection;
    }

    static void serializeToUrlConnection(RequestBuilder request, HttpURLConnection connection)
    throws IOException, JSONException{
        //Logger logger = new Logger(LoggingBehavior.REQUESTS, "Request");

        int numRequests = 1;

        MethodType connectionHttpMethod = (numRequests == 1) ? request.getMethodType() : MethodType.POST;
        connection.setRequestMethod(connectionHttpMethod.name());

        URL url = connection.getURL();
        /*logger.append("Request:\n");
        logger.appendKeyValue("Id", requests.getId());
        logger.appendKeyValue("URL", url);
        logger.appendKeyValue("Method", connection.getRequestMethod());
        logger.appendKeyValue("User-Agent", connection.getRequestProperty("User-Agent"));
        logger.appendKeyValue("Content-Type", connection.getRequestProperty("Content-Type"));*/

        //connection.setConnectTimeout(requests.getTimeout());
        //connection.setReadTimeout(requests.getTimeout());

        // If we have a single non-POST request, don't try to serialize anything or HttpURLConnection will
        // turn it into a POST.
        /*boolean isPost = (connectionHttpMethod == HttpMethod.POST);
        if (!isPost) {
            logger.log();
            return;
        }*/

        connection.setDoOutput(true);

        BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
        /*try {
            Serializer serializer = new Serializer(outputStream, logger);

            if (numRequests == 1) {
                Request request = requests.get(0);

                //logger.append("  Parameters:\n");
                //serializeParameters(request.parameters, serializer);

                //logger.append("  Attachments:\n");
                //serializeAttachments(request.parameters, serializer);

                if (request.graphObject != null) {
                    processGraphObject(request.graphObject, url.getPath(), serializer);
                }
            } else {
                String batchAppID = getBatchAppId(requests);
                if (Utility.isNullOrEmpty(batchAppID)) {
                    throw new FacebookException("At least one request in a batch must have an open Session, or a "
                            + "default app ID must be specified.");
                }

                serializer.writeString(BATCH_APP_ID_PARAM, batchAppID);

                // We write out all the requests as JSON, remembering which file attachments they have, then
                // write out the attachments.
                Bundle attachments = new Bundle();
                serializeRequestsAsJSON(serializer, requests, attachments);

                logger.append("  Attachments:\n");
                serializeAttachments(attachments, serializer);
            }
        } finally {
            outputStream.close();
        }
*/
        outputStream.close();
        //logger.log();
    }

    private JSONObject getOutputStream(RequestBuilder request, HttpURLConnection connection) throws IOException {
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
                return jsonResponse;
            }
            else {
                System.out.print("HTTP Response Code: " + responseCode);
            }
        }
        catch (Exception e) {
            throw new IOException(e);
        }
        return new JSONObject();
    }

    static HttpURLConnection createConnection(URL url) throws IOException {
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

    private static boolean isSupportedParameterType(Object value) {
        return value instanceof String || value instanceof Boolean || value instanceof Number ||
                value instanceof Date;
    }

    private static String parameterToString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Boolean || value instanceof Number) {
            return value.toString();
        } else if (value instanceof Date) {
            final SimpleDateFormat iso8601DateFormat = new SimpleDateFormat(ISO_8601_FORMAT_STRING, Locale.US);
            return iso8601DateFormat.format(value);
        }
        throw new IllegalArgumentException("Unsupported parameter type.");
    }

    private interface KeyValueSerializer {
        void writeString(String key, String value) throws IOException;
    }

    private static class Serializer implements KeyValueSerializer {
        private final BufferedOutputStream outputStream;
        private final Logger logger;
        private boolean firstWrite = true;

        public Serializer(BufferedOutputStream outputStream, Logger logger) {
            this.outputStream = outputStream;
            this.logger = logger;
        }

        public void writeObject(String key, Object value) throws IOException {
            if (isSupportedParameterType(value)) {
                writeString(key, parameterToString(value));
            } else if (value instanceof Bitmap) {
                writeBitmap(key, (Bitmap) value);
            } else if (value instanceof byte[]) {
                writeBytes(key, (byte[]) value);
            } else if (value instanceof ParcelFileDescriptor) {
                writeFile(key, (ParcelFileDescriptor) value);
            } else {
                throw new IllegalArgumentException("value is not a supported type: String, Bitmap, byte[]");
            }
        }

        public void writeString(String key, String value) throws IOException {
            writeContentDisposition(key, null, null);
            writeLine("%s", value);
            writeRecordBoundary();
            if (logger != null) {
                //logger.appendKeyValue("    " + key, value);
            }
        }

        public void writeBitmap(String key, Bitmap bitmap) throws IOException {
            writeContentDisposition(key, key, "image/png");
            // Note: quality parameter is ignored for PNG
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            writeLine("");
            writeRecordBoundary();
            //logger.appendKeyValue("    " + key, "<Image>");
        }

        public void writeBytes(String key, byte[] bytes) throws IOException {
            writeContentDisposition(key, key, "content/unknown");
            this.outputStream.write(bytes);
            writeLine("");
            writeRecordBoundary();
            //logger.appendKeyValue("    " + key, String.format("<Data: %d>", bytes.length));
        }

        public void writeFile(String key, ParcelFileDescriptor descriptor) throws IOException {
            writeContentDisposition(key, key, "content/unknown");

            ParcelFileDescriptor.AutoCloseInputStream inputStream = null;
            BufferedInputStream bufferedInputStream = null;
            int totalBytes = 0;
            try {
                inputStream = new ParcelFileDescriptor.AutoCloseInputStream(descriptor);
                bufferedInputStream = new BufferedInputStream(inputStream);

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    this.outputStream.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                }
            } finally {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            writeLine("");
            writeRecordBoundary();
            //logger.appendKeyValue("    " + key, String.format("<Data: %d>", totalBytes));
        }

        public void writeRecordBoundary() throws IOException {
            //writeLine("--%s", MIME_BOUNDARY);
        }

        public void writeContentDisposition(String name, String filename, String contentType) throws IOException {
            write("Content-Disposition: form-data; name=\"%s\"", name);
            if (filename != null) {
                write("; filename=\"%s\"", filename);
            }
            writeLine(""); // newline after Content-Disposition
            if (contentType != null) {
                writeLine("%s: %s", CONTENT_TYPE_HEADER, contentType);
            }
            writeLine(""); // blank line before content
        }

        public void write(String format, Object... args) throws IOException {
            if (firstWrite) {
                // Prepend all of our output with a boundary string.
                this.outputStream.write("--".getBytes());
                //this.outputStream.write(MIME_BOUNDARY.getBytes());
                this.outputStream.write("\r\n".getBytes());
                firstWrite = false;
            }
            this.outputStream.write(String.format(format, args).getBytes());
        }

        public void writeLine(String format, Object... args) throws IOException {
            write(format, args);
            write("\r\n");
        }

    }

}
