package com.aronskiy_anton.sdk;

/**
 * Created by aaronskiy on 23.08.2017.
 */

public class W1P2PException extends RuntimeException {
    static final long serialVersionUID = 1;

    /**
     * Constructs a new W1P2PException. 
     */
    public W1P2PException() {
        super();
    }

    /**
     * Constructs a new W1P2PException. 
     *
     * @param message
     *            the detail message of this exception 
     */
    public W1P2PException(String message) {
        super(message);
    }

    /**
     * Constructs a new W1P2PException. 
     *
     * @param message
     *            the detail message of this exception 
     * @param throwable
     *            the cause of this exception 
     */
    public W1P2PException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Constructs a new W1P2PException. 
     *
     * @param throwable
     *            the cause of this exception 
     */
    public W1P2PException(Throwable throwable) {
        super(throwable);
    }
}
