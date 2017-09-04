package com.aronskiy_anton.sdk;

import com.aronskiy_anton.sdk.managers.BeneficiariesCardsManager;
import com.aronskiy_anton.sdk.managers.DealsManager;
import com.aronskiy_anton.sdk.managers.NetworkManager;
import com.aronskiy_anton.sdk.managers.PayersCardsManager;
import com.aronskiy_anton.sdk.managers.PayoutsManager;
import com.aronskiy_anton.sdk.managers.RefundsManager;

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

    public NetworkManager networkManager = new NetworkManager(this);

    public BeneficiariesCardsManager beneficiariesCards = new BeneficiariesCardsManager(this);

    public DealsManager dealsManager = new DealsManager(this);

    public PayersCardsManager payersCardsManager = new PayersCardsManager(this);

    public PayoutsManager payoutsManager = new PayoutsManager(this);

    public RefundsManager refundsManager = new RefundsManager(this);

    /// Manager for working with payers cards
    public PayersCardsManager payersCards = new PayersCardsManager(this);

    public void setPlatform(String platformId, String signatureKey){
        this.platformId = platformId;
        this.signatureKey = signatureKey;
    }

    public void setBeneficiary(String id, String title, String phoneNumber) {
        this.benificaryId = id;
        this.benificaryTitle = title;
        this.benificaryPhoneNumber = phoneNumber;
    }

    public void setPayer(String id, String title, String phoneNumber) {
        this.payerId = id;
        this.payerTitle = title;
        this.payerPhoneNumber = phoneNumber;
    }
}


