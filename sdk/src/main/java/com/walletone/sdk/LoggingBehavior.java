package com.walletone.sdk;

/**
 * Created by aaronskiy on 23.08.2017.
 */

/**
 * Specifies different categories of logging messages that can be generated.
 *
 * @see Settings#addLoggingBehavior(LoggingBehavior)
 */
public enum LoggingBehavior {
    /**
     * Indicates that HTTP requests and a summary of responses should be logged.
     */
    REQUESTS,
    /**
     * Indicates that access tokens should be logged as part of the request logging; normally they are not.
     */
    INCLUDE_ACCESS_TOKENS,
    /**
     * Indicates that the entire raw HTTP response for each request should be logged.
     */
    INCLUDE_RAW_RESPONSES,
    /**
     * Indicates that cache operations should be logged.
     */
    CACHE
}
