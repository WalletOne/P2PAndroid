package com.walletone.sdk.managers;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.walletone.sdk.Manager;
import com.walletone.sdk.P2PCore;
import com.walletone.sdk.library.CompleteHandler;
import com.walletone.sdk.library.URLComposer;
import com.walletone.sdk.models.PayoutResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by aaronskiy on 30.08.2017.
 */

public class PayoutsManager extends Manager {

    private Composer composer = new Composer();

    class Composer extends URLComposer {

        String beneficiaries() {
            return getInstance().relativeToApi("beneficiaries");
        }

        String beneficiaries(String id) {
            return getInstance().relative(beneficiaries(), id);
        }

        String beneficiariesPayouts(@NonNull String id, int pageNumber, int itemsPerPage, String dealId) {

            List<String> items = new ArrayList<>();

            items.add(String.format(Locale.US, "pageNumber=%d", pageNumber));
            items.add(String.format(Locale.US, "itemsPerPage=%d", itemsPerPage));

            if (dealId != null) {
                items.add(String.format(Locale.US, "dealId=%s", dealId));
            }

            return getInstance().relative(beneficiaries(id), "payouts?" + TextUtils.join("&", items));
        }
    }

    public PayoutsManager(P2PCore core) {
        super(core);
    }

    /**
     * Get all payouts by beneficiary id
     *
     * @param pageNumber   Number of page in pagination
     * @param itemsPerPage count of payouts in one page
     * @param dealId       platform deal id
     * @param callback     callback from server
     * @return all payouts of beneficiary
     */
    public PayoutResult payouts(int pageNumber, int itemsPerPage, String dealId, CompleteHandler<PayoutResult, Throwable> callback) {
        String url = composer.beneficiariesPayouts(core.getBenificaryId(), pageNumber, itemsPerPage, dealId);
        return core.networkManager.request(url, NetworkManager.MethodType.GET, null, PayoutResult.class, callback);
    }
}
