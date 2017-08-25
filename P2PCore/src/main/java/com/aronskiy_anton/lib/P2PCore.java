package com.aronskiy_anton.lib;

import com.aronskiy_anton.lib.managers.NetworkManager;

public class P2PCore {

    private String platformId = "";

    private String signatureKey = "";

    public NetworkManager networkManager = new NetworkManager(this);

    public String getPlatformId() {
        return platformId;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public void setPlatform(String platformId, String signatureKey){
        this.platformId = platformId;
        this.signatureKey = signatureKey;
    }

}


