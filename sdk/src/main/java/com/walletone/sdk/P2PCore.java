package com.walletone.sdk;

import android.util.Log;

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

    private String benificaryTitle = "";

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

    public void setPlatform(String platformId, String signatureKey, Environment environment) {
        if (!this.initialised) {
            URLComposer.getInstance().setEnvironment(environment);
            this.platformId = platformId;
            this.signatureKey = signatureKey;
            this.initialised = true;
            printDebug("Platform initialized with:");
            printDebug("platformId: '" + platformId + "' signatureKey: '" + signatureKey + "' environment: '" + environment.name() + "'");
        }
    }

    public void setBeneficiary(String id, String title, String phoneNumber) {
        if (isInitialized()) {
            this.benificaryId = id;
            this.benificaryTitle = title;
            this.benificaryPhoneNumber = phoneNumber;
            printDebug("id: '" + id + "' title: '" + title + "' phone number: '" + phoneNumber + "'");
        } else {
            printDebug("Platform not initialized. Call P2PCore.INSTANCE.setPlatform() firstly");
        }
    }

    public void setPayer(String id, String title, String phoneNumber) {
        if (isInitialized()) {
            this.payerId = id;
            this.payerTitle = title;
            this.payerPhoneNumber = phoneNumber;
            printDebug("id: '" + id + "' title: '" + title + "' phone number: '" + phoneNumber + "'");
        } else {
            printDebug("Platform not initialized. Call P2PCore.INSTANCE.setPlatform() firstly");
        }
    }

    private boolean isPrintDebugEnabled = false;

    public void printDebug(String text) {
        if (this.isPrintDebugEnabled) {
            Log.d("P2P Log",  text);
        }
    }

    public void setPrintDebugEnabled(boolean printDebugEnabled) {
        isPrintDebugEnabled = printDebugEnabled;
    }
}


