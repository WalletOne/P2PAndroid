package com.aronskiy_anton.sdk.models;

import com.aronskiy_anton.sdk.managers.NetworkManager;

/**
 * Created by aaronskiy on 21.08.2017.
 */

public class RequestBuilder {

    private NetworkManager.MethodType methodType;
    private String urlString;
    private String signature;
    private String timestamp;
    private String httpBody;

    public NetworkManager.MethodType getMethodType() {
        return methodType;
    }

    public String getUrlString() {
        return urlString;
    }

    public String getSignature() {
        return signature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getHttpbody() {
        return httpBody;
    }

    public static Builder newBuilder(){
        return new RequestBuilder().new Builder();
    }

    public class Builder{
        private Builder(){}

        public Builder setUrlString(String urlString){
            RequestBuilder.this.urlString = urlString;
            return this;
        }

        public Builder setMethodType(NetworkManager.MethodType type){
            RequestBuilder.this.methodType = type;
            return this;
        }

        public Builder setSignature(String signature){
            RequestBuilder.this.signature = signature;
            return this;
        }

        public Builder setTimestamp(String timestamp){
            RequestBuilder.this.timestamp = timestamp;
            return this;
        }

        public Builder setHttpBody(String httpBody){
            RequestBuilder.this.httpBody = httpBody;
            return this;
        }

        public RequestBuilder build(){
            return RequestBuilder.this;
        }
    }
}
