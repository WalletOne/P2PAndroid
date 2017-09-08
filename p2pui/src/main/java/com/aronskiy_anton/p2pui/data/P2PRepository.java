package com.aronskiy_anton.p2pui.data;

import android.support.annotation.NonNull;

import com.aronskiy_anton.sdk.models.BankCard;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class P2PRepository implements P2PDataSource {

    private static P2PRepository INSTANCE = null;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, BankCard> cachedBankCards;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean cacheIsDirty = false;

    @Override
    public void getBankCards(@NonNull LoadBankCardsCallback callback) {

        // Respond immediately with cache if available and not dirty
        if (cachedBankCards != null && !cacheIsDirty) {
            callback.onBankCardsLoaded(new ArrayList<>(cachedBankCards.values()));
            return;
        }

        if (cacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getBankCardsFromRemoteDataSource(callback);
        } else {
           /* // Query the local storage if available. If not, query the network.
            mBankCardsLocalDataSource.getBankCards(new LoadBankCardsCallback() {
                @Override
                public void onBankCardsLoaded(List<BankCard> BankCards) {
                    refreshCache(BankCards);
                    callback.onBankCardsLoaded(new ArrayList<>(cachedBankCards.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getBankCardsFromRemoteDataSource(callback);
                }
            });*/
        }
    }

    private void getBankCardsFromRemoteDataSource(LoadBankCardsCallback callback) {
       /* mBankCardsRemoteDataSource.getBankCards(new LoadBankCardsCallback() {
            @Override
            public void onBankCardsLoaded(List<BankCard> bankCards) {
                //refreshCache(bankCards);
                //refreshLocalDataSource(BankCards);
                callback.onBankCardsLoaded(new ArrayList<>(cachedBankCards.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });*/
    }

    @Override
    public void getBankCard(@NonNull String cardId, @NonNull GetBankCardCallback callback) {

    }
}
