package com.aronskiy_anton.sdk.library;

/**
 * Created by aaronskiy on 22.09.2017.
 */

public interface CompleteErrorOnlyHandler<T> {
    void completed(T error);
}
