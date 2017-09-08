package com.aronskiy_anton.p2pui.bankcard;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.sdk.models.BankCard;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class BankCardAdapter extends BaseAdapter {

    private List<BankCard> cards;
    private BankCardFragment.BankCardItemListener itemListener;

    public BankCardAdapter(List<BankCard> cards, BankCardFragment.BankCardItemListener itemListener) {
        this.cards = cards;
        this.itemListener = itemListener;
    }

    public void replaceData(List<BankCard> cards) {
        setList(cards);
        notifyDataSetChanged();
    }

    @SuppressLint("RestrictedApi")
    private void setList(List<BankCard> cards) {
        this.cards = checkNotNull(cards);
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public BankCard getItem(int i) {
        return cards.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.bank_card_item, viewGroup, false);
        }

        final BankCard card = getItem(i);

        TextView titleTV = (TextView) rowView.findViewById(R.id.title);
        titleTV.setText(card.getCardMask());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onCardClick(card);
            }
        });

        return rowView;
    }
}
