package com.walletone.p2p;

import android.app.Application;

import com.walletone.sdk.P2PCore;

/**
 * Created by aaronskiy on 05.09.2017.
 */

public class P2PApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        P2PCore.INSTANCE.setPlatform("testplatform", "TestPlatformSignatureKey");
    }
}
