package com.aronskiy_anton.sdk.managers;

import android.os.AsyncTask;

import com.aronskiy_anton.sdk.Manager;
import com.aronskiy_anton.sdk.P2PCore;
import com.aronskiy_anton.sdk.W1P2PException;
import com.aronskiy_anton.sdk.internal.Utility;
import com.aronskiy_anton.sdk.library.Base64;
import com.aronskiy_anton.sdk.library.CompleteHandler;
import com.aronskiy_anton.sdk.library.Mapper;
import com.aronskiy_anton.sdk.library.ModelFactory;
import com.aronskiy_anton.sdk.models.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
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
        String base64;

        byte[] signatureValue2 = getSha256(sb.toString());

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

        byte[] signatureValue = getSha256(sb.toString());

        String base64;
        try {
            base64 = Base64.encode(signatureValue);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return base64;
    }


    private byte[] getSha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(base.getBytes("UTF-8"));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Mapper.Mappable> List<T> requestList(final String urlString, final MethodType method, final Map<String, Object> parameters, final Class<T> cls, final CompleteHandler<List<T>, Throwable> callback) {

        AsyncTask.execute((new Runnable() {
            @Override
            public void run() {
                requestWithPrint(urlString, method, parameters, new CompleteHandler<Object, Throwable>() {
                    @Override
                    public void completed(Object json, Throwable var2) {
                        ArrayList<T> listT = new ArrayList<>();
                        JSONArray jArray = (JSONArray) json;
                        if (jArray != null) {
                            for (int i = 0; i < jArray.length(); i++) {
                                T t = (T) ModelFactory.newInstance(cls, (JSONObject) jArray.opt(i));
                                listT.add(t);
                            }
                        }
                        callback.completed(listT, var2);
                    }
                });
            }
        }));

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Mapper.Mappable> T request(final String urlString, final MethodType method, final Map<String, Object> parameters, final Class<?> cls, final CompleteHandler<T, Throwable> callback) {

        AsyncTask.execute((new Runnable() {
            @Override
            public void run() {
                requestWithPrint(urlString, method, parameters, new CompleteHandler<Object, Throwable>() {
                    @Override
                    public void completed(Object json, Throwable var2) {
                        T instance = (T) ModelFactory.newInstance(cls, (JSONObject) json);
                        callback.completed(instance, var2);
                    }
                });
            }
        }));

        return null;
    }

    private JSONObject requestWithPrint(String urlString, MethodType method, Map<String, Object> parameters, final CompleteHandler<Object, Throwable> callback) {
        HttpURLConnection urlConnection;
        String timestamp = ISO8601TimeStamp.getISO8601TimeStamp(new Date());
        String bodyAsString = "";

        RequestBuilder.Builder requestBuilder = RequestBuilder.newBuilder();
        requestBuilder
                .setMethodType(method)
                .setTimestamp(timestamp)
                .setUrlString(urlString);

        try {
            if (parameters != null) {
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
                bodyAsString = jsonObject.toString();

                String base64 = Base64.encode(getSha256(bodyAsString));
                requestBuilder.setHttpBody(base64);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex){
            ex.printStackTrace();
        }

        final String signature = makeSignature(urlString, timestamp, bodyAsString);

        requestBuilder.setSignature(signature);

        //urlConnection = toHttpConnection(requestBuilder.build());

        LoadDataAsync task = new LoadDataAsync(callback);
        task.execute(requestBuilder.build());

        //return lowLevelRequestJson(urlConnection, callback);
        return null;
    }
/*
    private JSONObject lowLevelRequestJson(HttpURLConnection connection, CompleteHandler<Object, Throwable> callback) {

        InputStream stream = null;

        try {
            if (connection.getResponseCode() >= 400) {
                stream = connection.getErrorStream();
            } else {

                stream = connection.getInputStream();
                String responseString = Utility.readStreamToString(stream);
                JSONTokener tokener = new JSONTokener(responseString);
                Object resultObject = tokener.nextValue();
                callback.completed(resultObject, null);
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            Utility.closeQuietly(stream);
        }
        return null;
    }
*/
    private HttpURLConnection toHttpConnection(RequestBuilder requests) {

        URL url;
        try {
            url = new URL(requests.getUrlString());
        } catch (MalformedURLException e) {
            throw new W1P2PException("could not construct URL for request", e);
        }

        HttpURLConnection connection;
        try {
            connection = createConnection(url);
            connection.setConnectTimeout(2000);
            connection.setRequestMethod(requests.getMethodType().getMethodTypeId());

            connection.setRequestProperty("X-Wallet-PlatformId", core.getPlatformId());
            connection.setRequestProperty("X-Wallet-Timestamp", requests.getTimestamp());
            connection.setRequestProperty("X-Wallet-Signature", requests.getSignature());
        } catch (IOException e) {
            throw new W1P2PException("could not construct request body", e);
        }

        return connection;
    }

    private HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty(CONTENT_TYPE_HEADER, "application/json; charset=utf-8");
        return connection;
    }

    static class ISO8601TimeStamp {

        public static String getISO8601TimeStamp(Date date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat.format(date);
        }
    }

    class LoadDataAsync extends AsyncTask<RequestBuilder, Void, Object> {

        private final CompleteHandler<Object, Throwable> callback;

        private LoadDataAsync(CompleteHandler<Object, Throwable> callback) {
            this.callback = callback;
        }

        @Override
        protected Object doInBackground(RequestBuilder... request) {

            HttpURLConnection urlConnection = toHttpConnection(request[0]);

            InputStream stream = null;
            Object resultObject = null;

            try {
                if (urlConnection.getResponseCode() >= 400) {
                    stream = urlConnection.getErrorStream();
                } else {

                    stream = urlConnection.getInputStream();
                    String responseString = Utility.readStreamToString(stream);
                    JSONTokener tokener = new JSONTokener(responseString);
                    resultObject = tokener.nextValue();
                }
            } catch (JSONException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                Utility.closeQuietly(stream);
            }
            return resultObject;
        }

        @Override
        protected void onPostExecute(Object result) {
            if(callback != null) {
                callback.completed(result, null);
            }
        }
    }

}
