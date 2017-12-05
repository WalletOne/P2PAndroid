package com.aronskiy_anton.p2pui.bankcard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.p2pui.library.CreditCardValidator.CreditCardType;
import com.aronskiy_anton.p2pui.library.CreditCardValidator.CreditCardValidator;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by aaronskiy on 21.09.2017.
 */

public class BankCardAdapter extends RecyclerView.Adapter<BankCardAdapter.ItemViewHolder> {

    private Context context;
    private CreditCardValidator validator;

    private List<BankCard> cards;
    private BankCardFragment.BankCardItemListener itemListener;

    public BankCardAdapter(@NonNull List<BankCard> cards, BankCardFragment.BankCardItemListener itemListener, Context context) {
        this.cards = cards;
        this.itemListener = itemListener;
        this.context = context;
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
    public BankCardAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bank_card_item, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(BankCardAdapter.ItemViewHolder holder, int position) {

        final BankCard card = cards.get(position);

        CreditCardType type = validator.type(card.getCardMask());
        if (type != CreditCardType.UNDEFINED) {
            Drawable drawable = ContextCompat.getDrawable(context, getCardTypeLogo(type.getTitle()));
            holder.image.setImageDrawable(drawable);
            holder.title.setText(type.getTitle());
        } else {
            holder.title.setText(R.string.unknown_card_type);
        }

        holder.number.setText(formatMask(card.getCardMask()));

        holder.setCard(card);
        holder.setOnBankCardItemClickListener(itemListener);


/*
        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(), "Toast", Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void removeItem(int position) {
        cards.remove(position);
        notifyItemRemoved(position);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView title;
        TextView number;
        BankCardFragment.BankCardItemListener itemListener;
        BankCard card;

        ItemViewHolder(View v) {
            super(v);
            this.image = v.findViewById(R.id.card_logo);
            this.title = v.findViewById(R.id.card_title);
            this.number = v.findViewById(R.id.card_number);
            v.setOnClickListener(this);
        }

        public void setCard(BankCard card) {
            this.card = card;
        }

        public BankCard getCard() {
            return card;
        }

        public void setOnBankCardItemClickListener(BankCardFragment.BankCardItemListener itemListener) {
            this.itemListener = itemListener;
        }

        @Override
        public void onClick(View view) {
            if(itemListener != null) {
                itemListener.onCardClick(card);
            }
        }
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
