package com.aronskiy_anton.p2pui.bankcard;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class BankCardFragment extends android.support.v4.app.Fragment implements BankCardContract.View {

    private View noCardsView;

    private View cardsBlockView;

    private ViewGroup linkCardItem;

    private RecyclerView cardsRecyclerView;

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
        cardsAdapter = new BankCardAdapter(new ArrayList<BankCard>(0), itemListener, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bank_card_fragment_layout, container, false);

        cardsBlockView = root.findViewById(R.id.cards_block);

        // Set up recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        cardsRecyclerView = root.findViewById(R.id.cards_list);
        cardsRecyclerView.setLayoutManager(layoutManager);
        cardsRecyclerView.setAdapter(cardsAdapter);
        setupItemTouchHelper();

        // Set up no cards view
        noCardsView = root.findViewById(R.id.no_cards);

        linkCardItem = root.findViewById(R.id.link_card_item);
        linkCardItem.setOnClickListener(new View.OnClickListener() {
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

    private void setupItemTouchHelper() {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final BankCard card = ((BankCardAdapter.ItemViewHolder) viewHolder).getCard();

                if (direction == ItemTouchHelper.LEFT) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.delete_card_confirmation);

                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            cardsAdapter.notifyItemRangeChanged(position, cardsAdapter.getItemCount());
                        }
                    }).setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cardsAdapter.removeItem(position);
                            presenter.deleteCard(card);
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(cardsRecyclerView);
    }

    private void setLinkCardVisibility(boolean visibility) {
        linkCardItem.setVisibility(visibility ? View.VISIBLE : View.GONE);
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
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void showError(Throwable error) {
        Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
