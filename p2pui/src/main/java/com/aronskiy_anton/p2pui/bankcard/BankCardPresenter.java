package com.aronskiy_anton.p2pui.bankcard;

import android.support.annotation.NonNull;

import com.aronskiy_anton.sdk.P2PCore;
import com.aronskiy_anton.sdk.library.CompleteHandler;
import com.aronskiy_anton.sdk.models.BankCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class BankCardPresenter implements BankCardContract.Presenter {

    public enum Owner {
        BENEFICIARY,
        PAYER
    }

    private Owner owner;

    private BankCardContract.View bankCardView;

    private boolean firstLoad = true;

    private boolean isLoading = false;

    private boolean isAddCardAvailable = false;

    private List<BankCard> cards;

    public BankCardPresenter(@NonNull Owner owner, @NonNull BankCardContract.View view) {
        this.owner = owner;
        this.bankCardView = view;
        this.bankCardView.setPresenter(this);
    }

    @Override
    public void start() {
        loadCards(false);
    }

    @Override
    public void loadCards(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadCards(forceUpdate || firstLoad, true);
        firstLoad = false;
    }

    private void loadCards(boolean forceUpdate, final boolean showLoadingUI) {

        bankCardView.setLoadingIndicator(true);

        CompleteHandler<List<BankCard>, Throwable> handler = new CompleteHandler<List<BankCard>, Throwable>() {
            @Override
            public void completed(List<BankCard> list, Throwable var2) {
                bankCardView.setLoadingIndicator(false);
                isLoading = false;
                cards = list != null ? list : new ArrayList<BankCard>();
                if (cards.size() > 0) {
                    bankCardView.showCards(cards);
                } else {
                    bankCardView.showEmptyList();
                }

            }
        };

        isLoading = true;

        cards = new ArrayList<>();

        switch (owner) {
            case BENEFICIARY:
                P2PCore.INSTANCE.beneficiariesCards.cards(handler);
                break;
            case PAYER:
                P2PCore.INSTANCE.payersCards.cards(handler);
                break;
        }
    }

    @Override
    public void addNewCard() {
        switch (owner){
            case BENEFICIARY:
                bankCardView.showLinkCardActivity();
                break;
            case PAYER:
                bankCardView.closeBankCardAndShowPayDealActivity();
                break;

        }
    }

    @Override
    public boolean isAddNewCardAvailable() {
        return isAddCardAvailable || owner == Owner.BENEFICIARY;
    }

    @Override
    public void setAddCardAvailable(boolean isAvailable) {
        this.isAddCardAvailable = isAvailable;
    }


}
