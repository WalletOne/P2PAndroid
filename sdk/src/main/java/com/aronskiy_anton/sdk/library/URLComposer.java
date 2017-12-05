package com.aronskiy_anton.sdk.library;

/**
 * Created by aaronskiy on 25.08.2017.
 */

public class URLComposer {

    private enum Mode {
        SANDBOX,
        PRODUCT
    }

    private Mode mode = Mode.SANDBOX;

    public static final String SANDBOX_URL = "https://api.dev.walletone.com/p2p/";
    public static final String PRODUCT_URL = "https://api.dev.walletone.com/p2p";
    public static final String API_PATH = "api/v3";

    public String getProtocol() {
        switch (mode) {
            case SANDBOX:
                return "https";
            case PRODUCT:
                return "https";
            default:
                return "https";
        }
    }

    public String getBaseURL() {
        switch (mode) {
            case SANDBOX:
                return SANDBOX_URL;
            case PRODUCT:
                return PRODUCT_URL;
            default:
                return SANDBOX_URL;
        }
    }

    public String getApiURL() {
        return getBaseURL() + API_PATH;
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
}
