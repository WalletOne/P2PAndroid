package com.aronskiy_anton.p2pui.bankcard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.p2pui.library.CreditCardValidator.CreditCardType;
import com.aronskiy_anton.p2pui.library.CreditCardValidator.CreditCardValidator;
import com.aronskiy_anton.sdk.models.BankCard;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class BankCardAdapter extends BaseAdapter {

    private CreditCardValidator validator;

    private List<BankCard> cards;
    private BankCardFragment.BankCardItemListener itemListener;

    public BankCardAdapter(List<BankCard> cards, BankCardFragment.BankCardItemListener itemListener) {
        this.cards = cards;
        this.itemListener = itemListener;

        this.validator = new CreditCardValidator();
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

        ImageView image = rowView.findViewById(R.id.card_logo);
        TextView title = rowView.findViewById(R.id.card_title);
        TextView number = rowView.findViewById(R.id.card_number);

        CreditCardType type = validator.type(card.getCardMask());
        if (type != CreditCardType.UNDEFINED) {
            Drawable drawable = ContextCompat.getDrawable(rowView.getContext(), getCardTypeLogo(type.getTitle()));
            image.setImageDrawable(drawable);
            title.setText(type.getTitle());
        } else {
            //image.setImageDrawable(R.drawable.);
            title.setText(R.string.unknown_card_type);
        }

        number.setText(formatMask(card.getCardMask()));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onCardClick(card);
            }
        });

        return rowView;
    }

    private int getCardTypeLogo(String name){
        if("MasterCard".equals(name)){
            return R.drawable.ic_mastercard_mini;
        } else if("Visa".equals(name)){
            return R.drawable.ic_visa_mini;
        } else if ("Mir".equals(name)){
            return R.drawable.ic_mir_mini;
        } else return -1;
    }

    private String formatMask(String number) {
        if (number.length() <= 4) {
            return number;
        }

        String last4Digits = number.substring(number.length() - 4);
        last4Digits = last4Digits.replace('*', '•');
        return String.format("•••• %s", last4Digits);
    }

}
