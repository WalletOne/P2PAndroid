package com.aronskiy_anton.p2pui.bankcard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.p2pui.linkcard.LinkCardActivity;
import com.aronskiy_anton.sdk.models.BankCard;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.util.Preconditions.checkNotNull;
import static com.aronskiy_anton.p2pui.bankcard.BankCardActivity.ARG_CARD_ID;
import static com.aronskiy_anton.p2pui.bankcard.BankCardActivity.REQUEST_SELECT_CARD;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class BankCardFragment extends android.support.v4.app.Fragment implements BankCardContract.View {

    private View noCardsView;

    private View cardsBlockView;

    private ViewGroup link_card_item;

    private ListView cardsListView;

    private BankCardAdapter cardsAdapter;

    private BankCardContract.Presenter presenter;

    private ProgressBar progressBar;

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

        cardsBlockView = root.findViewById(R.id.cards_block);

        // Set up bank cards view
        cardsListView = root.findViewById(R.id.cards_list);
        cardsListView.setAdapter(cardsAdapter);

        // Set up no cards view
        noCardsView = root.findViewById(R.id.no_cards);

        link_card_item = root.findViewById(R.id.link_card_item);
        link_card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addNewCard();
            }
        });
        setLinkCardVisibility(presenter.isAddNewCardAvailable());

        progressBar = root.findViewById(R.id.progress_bar);

        setHasOptionsMenu(true);

        return root;
    }

    private void setLinkCardVisibility(boolean visibility) {
        link_card_item.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(presenter.isAddNewCardAvailable()) {
            inflater.inflate(R.menu.bank_cards_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Integer itemId = item.getItemId();
        if (itemId.equals(R.id.menu_link_card)) {
            presenter.addNewCard();
        }
        return true;
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
        cardsBlockView.setVisibility(View.VISIBLE);
        noCardsView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyList() {
        noCardsView.setVisibility(View.VISIBLE);
        cardsBlockView.setVisibility(View.GONE);
    }

    @Override
    public void showLinkCardActivity() {
        Intent intent = new Intent(getContext(), LinkCardActivity.class);
        startActivityForResult(intent, LinkCardActivity.REQUEST_LINK_CARD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case LinkCardActivity.REQUEST_LINK_CARD:
                if (resultCode == RESULT_OK) {
                    presenter.loadCards(true);
                } else if (resultCode == LinkCardActivity.RESULT_FAIL) {
                    Toast.makeText(getContext(), "Link card result error", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void setLoadingIndicator(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void closeBankCardAndShowPayDealActivity() {
        Intent intent = new Intent();
        //intent.putExtra(ARG_CARD_ID, clickedCard.getCardId());
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    /**
     * Listener for clicks on bank card in the ListView.
     */
    BankCardItemListener itemListener = new BankCardItemListener() {

        @Override
        public void onCardClick(BankCard clickedCard) {
            Intent intent = new Intent();
            intent.putExtra(ARG_CARD_ID, clickedCard.getCardId());
            getActivity().setResult(RESULT_OK, intent);
            getActivity().finish();
        }
    };

    public interface BankCardItemListener {

        void onCardClick(BankCard clickedCard);
    }
}
