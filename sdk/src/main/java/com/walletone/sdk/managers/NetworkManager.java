package com.walletone.sdk.managers;

import android.os.AsyncTask;

import com.walletone.sdk.Manager;
import com.walletone.sdk.P2PCore;
import com.walletone.sdk.W1P2PException;
import com.walletone.sdk.internal.Utility;
import com.walletone.sdk.library.Base64;
import com.walletone.sdk.library.CompleteErrorOnlyHandler;
import com.walletone.sdk.library.CompleteHandler;
import com.walletone.sdk.library.Mapper;
import com.walletone.sdk.library.ModelFactory;
import com.walletone.sdk.models.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        byte[] signatureValue = getSha256(sb.toString());

        try {
            base64 = Base64.encode(signatureValue);
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


    public static byte[] getSha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(base.getBytes("UTF-8"));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Mapper.Mappable> List<T> requestList(final String urlString, final MethodType method, final Map<String, Object> parameters, final Class<T> cls, final CompleteHandler<List<T>, Throwable> callback) {

        requestWithPrint(urlString, method, parameters, new CompleteHandler<Object, Throwable>() {
            @Override
            public void completed(Object json, Throwable error) {
                if (error == null) {
                    ArrayList<T> listT = new ArrayList<>();
                    JSONArray jArray = (JSONArray) json;
                    if (jArray != null) {
                        for (int i = 0; i < jArray.length(); i++) {
                            T t = (T) ModelFactory.newInstance(cls, (JSONObject) jArray.opt(i));
                            listT.add(t);
                        }
                    }
                    callback.completed(listT, null);
                } else {
                    callback.completed(null, error);
                }
            }
        });
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Mapper.Mappable> T request(final String urlString, final MethodType method, final Map<String, Object> parameters, final Class<?> cls, final CompleteHandler<T, Throwable> callback) {

        requestWithPrint(urlString, method, parameters, new CompleteHandler<Object, Throwable>() {
            @Override
            public void completed(Object json, Throwable error) {
                if (error == null) {
                    try {
                        T instance = (T) ModelFactory.newInstance(cls, (JSONObject) json);
                        callback.completed(instance, null);
                    } catch (ClassCastException ex) {
                        callback.completed(null, ex);
                    }
                } else {
                    callback.completed(null, error);
                }
            }
        });

        return null;
    }

    public void request(final String urlString, final MethodType method, final Map<String, Object> parameters, final CompleteErrorOnlyHandler<Throwable> callback) {
        requestWithPrint(urlString, method, parameters, new CompleteHandler<Object, Throwable>() {
            @Override
            public void completed(Object json, Throwable error) {
                callback.completed(error);
            }
        });
    }

    private JSONObject requestWithPrint(String urlString, MethodType method, Map<String, Object> parameters, final CompleteHandler<Object, Throwable> callback) {

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
                requestBuilder.setHttpBody(bodyAsString);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }

        final String signature = makeSignature(urlString, timestamp, bodyAsString);

        requestBuilder.setSignature(signature);

        LoadDataAsync task = new LoadDataAsync(callback);
        task.execute(requestBuilder.build());

        return null;
    }

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
            connection.setConnectTimeout(5000);
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

    class LoadDataAsync extends AsyncTask<RequestBuilder, Void, Object> {

        private final CompleteHandler<Object, Throwable> callback;

        private W1P2PException exception;

        private LoadDataAsync(CompleteHandler<Object, Throwable> callback) {
            this.callback = callback;
        }

        @Override
        protected Object doInBackground(RequestBuilder... request) {

            HttpURLConnection urlConnection = toHttpConnection(request[0]);

            printRequestDebugData(urlConnection, request[0]);

            InputStream stream = null;
            Object resultObject = null;

            try {

                // Add POST body
                if (request[0].getHttpBody() != null) {
                    OutputStream os = urlConnection.getOutputStream();
                    os.write(request[0].getHttpBody().getBytes("UTF-8"));
                    os.close();
                }

                if (urlConnection.getResponseCode() >= 400) {
                    stream = urlConnection.getErrorStream();
                    String responseString = Utility.readStreamToString(stream);
                    JSONTokener tokener = new JSONTokener(responseString);
                    resultObject = tokener.nextValue();

                    exception = new W1P2PException(((JSONObject) resultObject).getString("ErrorDescription"));
                    printErrorDebug(exception);
                } else {
                    stream = urlConnection.getInputStream();
                    String responseString = Utility.readStreamToString(stream);
                    JSONTokener tokener = new JSONTokener(responseString);
                    resultObject = tokener.nextValue();
                    /* При удалении сервер возвращает пустую строку c кавычками "", но это успешное выполнение */
                    if (!"DELETE".equals(urlConnection.getRequestMethod())) {
                        if (!(resultObject instanceof JSONObject) && !(resultObject instanceof JSONArray)) {
                            exception = new W1P2PException("Server returned non-json data");
                            printErrorDebug(exception);
                        }
                    }

                    printResponseDebugData(urlConnection, responseString);
                }
            } catch (JSONException e) {
                exception = new W1P2PException("could not parse json data", e);
                printErrorDebug(exception);
            } catch (IOException e) {
                exception = new W1P2PException("could not get data", e);
                printErrorDebug(exception);
            } finally {
                Utility.closeQuietly(stream);
            }
            return resultObject;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (callback != null) {
                if (exception == null) {
                    callback.completed(result, null);
                } else {
                    callback.completed(null, exception);
                }
            }
        }
    }

    private void printResponseDebugData(HttpURLConnection urlConnection, String responseString) throws IOException {
        P2PCore.INSTANCE.printDebug("=======");
        P2PCore.INSTANCE.printDebug("SERVER RESPONSE");
        P2PCore.INSTANCE.printDebug("Code:" + urlConnection.getResponseCode() + " Message: " + urlConnection.getResponseMessage());
        P2PCore.INSTANCE.printDebug("Response string:");
        P2PCore.INSTANCE.printDebug(responseString);
        P2PCore.INSTANCE.printDebug("=======");
    }

    private void printRequestDebugData(HttpURLConnection connection, RequestBuilder request) {
        P2PCore.INSTANCE.printDebug("=======");
        P2PCore.INSTANCE.printDebug("BEGIN NEW REQUEST");
        P2PCore.INSTANCE.printDebug(request.getMethodType().getMethodTypeId() + " \\ " + request.getUrlString());
        P2PCore.INSTANCE.printDebug("BODY:(" + request.getHttpBody() + ")");
        P2PCore.INSTANCE.printDebug("Headers:");

        for (Map.Entry<String, List<String>> entries : connection.getRequestProperties().entrySet()) {
            StringBuilder values = new StringBuilder();
            for (String value : entries.getValue()) {
                values.append(value);
            }
            P2PCore.INSTANCE.printDebug(entries.getKey() + " - " + values);
        }

        P2PCore.INSTANCE.printDebug("END NEW REQUEST");
        P2PCore.INSTANCE.printDebug("=======");
    }

    private void printErrorDebug(W1P2PException exception) {
        P2PCore.INSTANCE.printDebug("=======");
        P2PCore.INSTANCE.printDebug("ERROR:");
        P2PCore.INSTANCE.printDebug("Localized message: " + exception.getLocalizedMessage());
        P2PCore.INSTANCE.printDebug("Message: " + exception.getMessage());
        P2PCore.INSTANCE.printDebug("=======");
    }

    static class ISO8601TimeStamp {

        public static String getISO8601TimeStamp(Date date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat.format(date);
        }
    }

}