package com.aronskiy_anton.sdk;

import com.aronskiy_anton.sdk.managers.BeneficiariesCardsManager;
import com.aronskiy_anton.sdk.managers.NetworkManager;

public class P2PCore {

    private String platformId = "";

    private String signatureKey = "";

    private String payerId = "";

    private String payerTitle = "";

    private String payerPhoneNumber = "";

    private String benificaryId = "";

    private String benificaryTitle  = "";

    private String benificaryPhoneNumber = "";

    public String getPlatformId() {
        return platformId;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public String getPayerId() {
        return payerId;
    }

    public String getPayerTitle() {
        return payerTitle;
    }

    public String getPayerPhoneNumber() {
        return payerPhoneNumber;
    }

    public String getBenificaryId() {
        return benificaryId;
    }

    public String getBenificaryTitle() {
        return benificaryTitle;
    }

    public String getBenificaryPhoneNumber() {
        return benificaryPhoneNumber;
    }

    public void setPlatform(String platformId, String signatureKey){
        this.platformId = platformId;
        this.signatureKey = signatureKey;
    }

    public NetworkManager networkManager = new NetworkManager(this);

    public BeneficiariesCardsManager beneficiariesCards = new BeneficiariesCardsManager(this);

}


