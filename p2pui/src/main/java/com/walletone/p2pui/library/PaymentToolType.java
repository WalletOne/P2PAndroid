package com.walletone.p2pui.library;

import android.support.annotation.StringRes;

import com.walletone.p2pui.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aaronskiy on 06.12.2017.
 */

public enum PaymentToolType {

    ALFA_CLICK_RUB(R.string.payment_tool_id_alfaclick_rub, "AlfaclickRUB"),

    BEELINE_RUB(R.string.payment_tool_id_beeline_rub, "BeelineRUB"),

    CREDIT_CARD_RUB(R.string.payment_tool_id_credit_card_rub, "CreditCardRUB"),

    PSB_RETAIL_RUB(R.string.payment_tool_id_psb_retail_rub, "PsbRetailRUB"),

    QIWI_WALLET_RUB(R.string.payment_tool_id_qiwi_wallet_rub, "QiwiWalletRUB"),

    YANDEX_MONEY_RUB(R.string.payment_tool_id_yandex_money_rub, "YandexMoneyRUB"),

    UNDEFINED(R.string.payment_tool_id_undefined, null);    // Undefined

    private static Map<String, PaymentToolType> map = new HashMap<>();

    static {
        for (PaymentToolType type : values()) {
            map.put(type.remoteName, type);
        }
    }

    @StringRes
    final int localizedName;

    final String remoteName;

    PaymentToolType(int stringResId, String remoteName) {
        localizedName = stringResId;
        this.remoteName = remoteName;
    }

    @StringRes
    public int getLocalizedName() {
        return localizedName;
    }

    public String getRemoteName() {
        return remoteName;
    }

    public static PaymentToolType getPaymentToolNameByPaymentTypeId(String name) {
        PaymentToolType result = map.get(name);
        return result == null ? UNDEFINED : result;
    }

}
