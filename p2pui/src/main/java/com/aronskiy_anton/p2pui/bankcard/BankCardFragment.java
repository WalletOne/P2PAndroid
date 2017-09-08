package com.aronskiy_anton.p2pui.bankcard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.sdk.models.BankCard;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class BankCardFragment extends android.support.v4.app.Fragment implements BankCardContract.View {

    private BankCardAdapter cardsAdapter;

    private BankCardContract.Presenter presenter;

    public BankCardFragment() {
    }

    public static BankCardFragment newInstance() {
        return new BankCardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardsAdapter = new BankCardAdapter(new ArrayList<BankCard>(0), itemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bank_card_fragment_layout, container, false);

        // Set up bank cards view
        ListView listView = root.findViewById(R.id.cards_list);
        listView.setAdapter(cardsAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(BankCardContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void showCards(List<BankCard> bankCards) {
        cardsAdapter.replaceData(bankCards);
    }

    /**
     * Listener for clicks on bank card in the ListView.
     */
    BankCardItemListener itemListener = new BankCardItemListener() {

        @Override
        public void onCardClick(BankCard clickedCard) {

        }

    };

    public interface BankCardItemListener {

        void onCardClick(BankCard clickedCard);
    }
}
