package com.walletone.sdk.library;

/**
 * Created by aaronskiy on 29.08.2017.
 */

public class Container<T> {

    private Class<T> type;

    public Container(Class<T> type) { this.type = type; }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public <T> T newInstance(Class<T> cls) {
        T myObject = null;
        try {
            myObject = cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return myObject;
    }

    public T createContents() throws Exception {
        return type.newInstance();
    }

}
