package com.walletone.sdk.library;

/**
 * Created by aaronskiy on 25.08.2017.
 */

public class URLComposer {

    private String SANDBOX_URL = "https://api.dev.walletone.com/p2p/";
    private String PRODUCT_URL = "https://api.w1.ru/p2p/";
    private int CURRENT_API_VERSION = 3;
    private String API_PATH = "api/v%s";

    private static URLComposer instance;

    public static URLComposer getInstance() {
        if (instance == null) instance = new URLComposer();
        return instance;
    }

    private Environment environment;

    private String getProtocol() {
        switch (environment) {
            case SANDBOX:
                return "https";
            case PRODUCT:
                return "https";
            default:
                return "https";
        }
    }

    private String getBaseURL() {
        switch (environment) {
            case SANDBOX:
                return SANDBOX_URL;
            case PRODUCT:
                return PRODUCT_URL;
            default:
                return SANDBOX_URL;
        }
    }

    private String getApiURL() {
        return getBaseURL() + String.format(API_PATH, CURRENT_API_VERSION);
    }

    public String relativeToBase(String to){
        return relative(getBaseURL(), to);
    }

    public String relativeToApi(String to){
        return relative(getApiURL(), to);
    }

    public String relative(String base, String to) {
        if (base.endsWith("/")) {
            return base + to;
        } else {
            return base + "/" + to;
        }
    }

    public void setEnvironment(Environment environment) {
        if(environment != null) {
            this.environment = environment;
        }
    }
}
