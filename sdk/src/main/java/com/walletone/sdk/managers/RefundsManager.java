package com.walletone.sdk.managers;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.walletone.sdk.Manager;
import com.walletone.sdk.P2PCore;
import com.walletone.sdk.library.CompleteHandler;
import com.walletone.sdk.library.URLComposer;
import com.walletone.sdk.models.RefundsResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by aaronskiy on 30.08.2017.
 */

public class RefundsManager extends Manager {

    private Composer composer = new Composer();

    class Composer extends URLComposer{

        String payers(){
            return getInstance().relativeToApi("payers");
        }

        String payers(String id){
            return getInstance().relative(payers(), id);
        }

        String payersRefunds(@NonNull String id, int pageNumber, int itemsPerPage, String dealId){

            List<String> items = new ArrayList<>();

            items.add(String.format(Locale.US, "pageNumber=%d", pageNumber));
            items.add(String.format(Locale.US, "itemsPerPage=%d", itemsPerPage));

            if(dealId != null){
                items.add(String.format(Locale.US, "dealId=%s", dealId));
            }

            return getInstance().relative(payers(id), "refunds?" + TextUtils.join("&", items));
        }
    }

    public RefundsManager(P2PCore core) {
        super(core);
    }

    /**
     * Get all refunds by payer id
     * @return all refunds of payer
     */
    public RefundsResult refunds(int pageNumber, int itemsPerPage, String dealId, CompleteHandler<RefundsResult, Throwable> callback){
        String url = composer.payersRefunds(core.getPayerId(), pageNumber, itemsPerPage, dealId);
        return core.networkManager.request(url, NetworkManager.MethodType.GET, null, RefundsResult.class, callback);
    }
}
