package com.aronskiy_anton.sdk.managers;

/**
 * Created by aaronskiy on 23.08.2017.
 */

import com.aronskiy_anton.sdk.W1P2PException;
import com.aronskiy_anton.sdk.internal.Utility;
import com.aronskiy_anton.sdk.models.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the response, successful or otherwise, of a call to the Facebook platform.
 */
public class Response {
    private final HttpURLConnection connection;

    private final boolean isFromCache;
    private final RequestBuilder request;

    /**
     * Property name of non-JSON results in the GraphObject. Certain calls to Facebook result in a non-JSON response
     * (e.g., the string literal "true" or "false"). To present a consistent way of accessing results, these are
     * represented as a GraphObject with a single string property with this name.
     */
    public static final String NON_JSON_RESPONSE_PROPERTY = "FACEBOOK_NON_JSON_RESULT";

    private static final int INVALID_SESSION_FACEBOOK_ERROR_CODE = 190;

    private static final String CODE_KEY = "code";
    private static final String BODY_KEY = "body";

    private static final String RESPONSE_LOG_TAG = "Response";

    private static final String RESPONSE_CACHE_TAG = "ResponseCache";


    Response(RequestBuilder request, HttpURLConnection connection) {
        this.request = request;
        this.connection = connection;
        this.isFromCache = false;
    }


    /**
     * Returns the HttpURLConnection that this response was generated from. If the response was retrieved
     * from the cache, this will be null.
     *
     * @return the connection, or null
     */
    public final HttpURLConnection getConnection() {
        return connection;
    }

    /**
     * Returns the request that this response is for.
     *
     * @return the request that this response is for
     */
    public RequestBuilder getRequest() {
        return request;
    }


    /**
     * Provides a debugging string for this response.
     */
    @Override
    public String toString() {
        String responseCode;
        try {
            responseCode = String.format("%d", (connection != null) ? connection.getResponseCode() : 200);
        } catch (IOException e) {
            responseCode = "unknown";
        }

        return new StringBuilder().append("{Response: ").append(" responseCode: ").append(responseCode)
                .append(", isFromCache:").append(isFromCache).append("}")
                .toString();
    }

    /**
     * Indicates whether the response was retrieved from a local cache or from the server.
     *
     * @return true if the response was cached locally, false if it was retrieved from the server
     */
    public final boolean getIsFromCache() {
        return isFromCache;
    }


    @SuppressWarnings("resource")
    static List<Response> fromHttpConnection(HttpURLConnection connection, RequestBuilder requests) {
        InputStream stream = null;
/*
        FileLruCache cache = null;
        String cacheKey = null;
        if (requests instanceof CacheableRequestBatch) {
            CacheableRequestBatch cacheableRequestBatch = (CacheableRequestBatch) requests;
            cache = getResponseCache();
            cacheKey = cacheableRequestBatch.getCacheKeyOverride();
            if (Utility.isNullOrEmpty(cacheKey)) {
                if (requests.size() == 1) {
                    // Default for single requests is to use the URL.
                    cacheKey = requests.get(0).getUrlForSingleRequest();
                } else {
                    Logger.log(LoggingBehavior.REQUESTS, RESPONSE_CACHE_TAG,
                            "Not using cache for cacheable request because no key was specified");
                }
            }

            // Try loading from cache.  If that fails, load from the network.
            if (!cacheableRequestBatch.getForceRoundTrip() && cache != null && !Utility.isNullOrEmpty(cacheKey)) {
                try {
                    stream = cache.get(cacheKey);
                    if (stream != null) {
                        return createResponsesFromStream(stream, null, requests, true);
                    }
                //} catch (W1P2PException exception) { // retry via roundtrip below
                } catch (JSONException exception) {
                } catch (IOException exception) {
                } finally {
                    Utility.closeQuietly(stream);
                }
            }
        }
*/
        // Load from the network, and cache the result if not an error.
        try {
            if (connection.getResponseCode() >= 400) {
                stream = connection.getErrorStream();
            } else {
                stream = connection.getInputStream();
                //if ((cache != null) && (cacheKey != null) && (stream != null)) {
                /*if (stream != null) {
                    InputStream interceptStream = cache.interceptAndPut(cacheKey, stream);
                    if (interceptStream != null) {
                        stream = interceptStream;
                    }
                }*/
            }

            return createResponsesFromStream(stream, connection, requests, false);
        /*} catch (W1P2PException W1P2PException) {
            Logger.log(LoggingBehavior.REQUESTS, RESPONSE_LOG_TAG, "Response <Error>: %s", W1P2PException);
            return constructErrorResponses(requests, connection, W1P2PException);*/
        } catch (JSONException exception) {
            //Logger.log(LoggingBehavior.REQUESTS, RESPONSE_LOG_TAG, "Response <Error>: %s", exception);
            return constructErrorResponses(requests, connection, new W1P2PException(exception));
        } catch (IOException exception) {
            //Logger.log(LoggingBehavior.REQUESTS, RESPONSE_LOG_TAG, "Response <Error>: %s", exception);
            return constructErrorResponses(requests, connection, new W1P2PException(exception));
        } finally {
            Utility.closeQuietly(stream);
        }
    }

    static List<Response> createResponsesFromStream(InputStream stream, HttpURLConnection connection,
                                                    RequestBuilder requests, boolean isFromCache) throws JSONException, IOException {

        String responseString = Utility.readStreamToString(stream);
        /*Logger.log(LoggingBehavior.INCLUDE_RAW_RESPONSES, RESPONSE_LOG_TAG,
                "Response (raw)\n  Size: %d\n  Response:\n%s\n", responseString.length(),
                responseString);*/

        JSONTokener tokener = new JSONTokener(responseString);
        Object resultObject = tokener.nextValue();

        List<Response> responses = createResponsesFromObject(connection, requests, resultObject, isFromCache);
        /*Logger.log(LoggingBehavior.REQUESTS, RESPONSE_LOG_TAG, "Response\n  Id: %s\n  Size: %d\n  Responses:\n%s\n",
                requests.getId(), responseString.length(), responses);*/
        //List<Response> responses = new ArrayList<>();
        return responses;
    }

    private static List<Response> createResponsesFromObject(HttpURLConnection connection, RequestBuilder requests,
                                                            Object object, boolean isFromCache) throws W1P2PException, JSONException {

        List<Response> responses = new ArrayList<>();
        Object originalResult = object;

        try {
            // Single request case -- the entire response is the result, wrap it as "body" so we can handle it
            // the same as we do in the batched case. We get the response code from the actual HTTP response,
            // as opposed to the batched case where it is returned as a "code" element.
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(BODY_KEY, object);
            int responseCode = (connection != null) ? connection.getResponseCode() : 200;
            jsonObject.put(CODE_KEY, responseCode);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);

            // Pretend we got an array of 1 back.
            object = jsonArray;

        } catch (JSONException e) {
            responses.add(new Response(requests, connection));
        } catch (IOException e) {
            responses.add(new Response(requests, connection));
        }

/*
        if (!(object instanceof JSONArray) || ((JSONArray) object).length() != numRequests) {
            FacebookException exception = new FacebookException("Unexpected number of results");
            throw exception;
        }
*/


        JSONArray jsonArray = (JSONArray) object;

        for (int i = 0; i < jsonArray.length(); ++i) {
            RequestBuilder request = requests;
            try {
                Object obj = jsonArray.get(i);
                responses.add(createResponseFromObject(request, connection, obj, isFromCache, originalResult));
            } catch (JSONException e) {
                responses.add(new Response(request, connection));
            } catch (W1P2PException e) {
                responses.add(new Response(request, connection));
            }
        }

        return responses;
    }

    private static Response createResponseFromObject(RequestBuilder request, HttpURLConnection connection, Object object,
                                                     boolean isFromCache, Object originalResult) throws JSONException {
    /*    if (object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;

            FacebookRequestError error =
                    FacebookRequestError.checkResponseAndCreateError(jsonObject, originalResult, connection);
            if (error != null) {
                if (error.getErrorCode() == INVALID_SESSION_FACEBOOK_ERROR_CODE) {
                    Session session = request.getSession();
                    if (session != null) {
                        session.closeAndClearTokenInformation();
                    }
                }
                return new Response(request, connection, error);
            }

            Object body = Utility.getStringPropertyAsJSON(jsonObject, BODY_KEY, NON_JSON_RESPONSE_PROPERTY);

            if (body instanceof JSONObject) {
                GraphObject graphObject = GraphObject.Factory.create((JSONObject) body);
                return new Response(request, connection, graphObject, isFromCache);
            } else if (body instanceof JSONArray) {
                GraphObjectList<GraphObject> graphObjectList = GraphObject.Factory.createList(
                        (JSONArray) body, GraphObject.class);
                return new Response(request, connection, graphObjectList, isFromCache);
            }
            // We didn't get a body we understand how to handle, so pretend we got nothing.
            object = JSONObject.NULL;
        }
*/
        if (object == JSONObject.NULL) {
            return new Response(request, connection);
        } else {
            throw new W1P2PException("Got unexpected object type in response, class: "
                    + object.getClass().getSimpleName());
        }
    }

    static List<Response> constructErrorResponses(RequestBuilder requests, HttpURLConnection connection, W1P2PException error) {

        List<Response> responses = new ArrayList<>();

        Response response = new Response(requests, connection);
        responses.add(response);

        return responses;
    }

}