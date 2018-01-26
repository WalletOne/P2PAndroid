package com.walletone.sdk.library;

/**
 * Created by aaronskiy on 29.08.2017.
 */

public interface CompleteHandler<V, A> {
    void completed(V json, A error);
}