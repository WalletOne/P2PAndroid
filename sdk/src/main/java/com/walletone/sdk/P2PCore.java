package com.walletone.sdk;

import com.walletone.sdk.library.Environment;
import com.walletone.sdk.library.URLComposer;
import com.walletone.sdk.managers.BeneficiariesPaymentToolsManager;
import com.walletone.sdk.managers.DealsManager;
import com.walletone.sdk.managers.NetworkManager;
import com.walletone.sdk.managers.PayersPaymentToolsManager;
import com.walletone.sdk.managers.PayoutsManager;
import com.walletone.sdk.managers.RefundsManager;

public enum P2PCore {

    INSTANCE;

    private boolean initialised = false;

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

    public BeneficiariesPaymentToolsManager beneficiariesPaymentTools = new BeneficiariesPaymentToolsManager(this);

    public DealsManager dealsManager = new DealsManager(this);

    public PayersPaymentToolsManager payersPaymentToolsManager = new PayersPaymentToolsManager(this);

    public PayoutsManager payoutsManager = new PayoutsManager(this);

    public RefundsManager refundsManager = new RefundsManager(this);

    /// Manager for working with payers payment tools
    public PayersPaymentToolsManager payersPaymentTools = new PayersPaymentToolsManager(this);

    public boolean isInitialized() {
        return this.initialised;
    }

    public void setPlatform(String platformId, String signatureKey, Environment environment){
        if(!this.initialised) {
            URLComposer.getInstance().setEnvironment(environment);
            this.platformId = platformId;
            this.signatureKey = signatureKey;
            this.initialised = true;
        }
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

