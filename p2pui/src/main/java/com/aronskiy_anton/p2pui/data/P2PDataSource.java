package com.aronskiy_anton.p2pui.data;

import android.support.annotation.NonNull;

import com.aronskiy_anton.sdk.models.BankCard;

import java.util.List;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public interface P2PDataSource {

    interface LoadBankCardsCallback {

        void onBankCardsLoaded(List<BankCard> tasks);

        void onDataNotAvailable();
    }

    interface GetBankCardCallback {

        void onBankCardLoaded(BankCard task);

        void onDataNotAvailable();
    }

    void getBankCards(@NonNull LoadBankCardsCallback callback);

    void getBankCard(@NonNull String cardId, @NonNull GetBankCardCallback callback);


}
