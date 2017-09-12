package com.aronskiy_anton.p2pui.payouts;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.p2pui.library.CreditCardValidator.CreditCardType;
import com.aronskiy_anton.sdk.models.BankCard;
import com.aronskiy_anton.sdk.models.Payout;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.support.v4.util.Preconditions.checkNotNull;
import static com.aronskiy_anton.sdk.models.Payout.PAYOUT_STATE_ID_ACCEPTED;
import static com.aronskiy_anton.sdk.models.Payout.PAYOUT_STATE_ID_PROCESSING;
import static com.aronskiy_anton.sdk.models.Payout.PAYOUT_STATE_ID_PROCESS_ERROR;

/**
 * Created by anton on 12.09.2017.
 */

public class PayoutsAdapter extends BaseAdapter {

    private TextView state;
    private TextView date;
    private TextView amount;

    private List<Payout> payouts;

    private OnScrolledToBottomListener onScrolledToBottomListener;

    public PayoutsAdapter(List<Payout> payouts) {
        this.payouts = payouts;
    }

    public void setOnScrolledToBottomListener(OnScrolledToBottomListener onScrolledToBottomListener) {
        this.onScrolledToBottomListener = onScrolledToBottomListener;
    }

    public void replaceData(List<Payout> payouts) {
        setList(payouts);
        notifyDataSetChanged();
    }

    @SuppressLint("RestrictedApi")
    private void setList(List<Payout> payouts) {
        this.payouts = checkNotNull(payouts);
    }

    @Override
    public int getCount() {
        return payouts.size();
    }

    @Override
    public Payout getItem(int i) {
        return payouts.get(i);
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
            rowView = inflater.inflate(R.layout.payout_item, viewGroup, false);
        }

        final Payout payout = getItem(i);

        state = rowView.findViewById(R.id.payout_state);
        date = rowView.findViewById(R.id.payout_date);
        amount = rowView.findViewById(R.id.payout_amount);

        state.setText(payout.getPayoutStateId());

        switch (payout.getPayoutStateId()){
            case PAYOUT_STATE_ID_ACCEPTED:
                displayAcceptedState(rowView);
                break;
            case PAYOUT_STATE_ID_PROCESSING:
                displayProcessingState(rowView);
                break;
            case PAYOUT_STATE_ID_PROCESS_ERROR:
                displayErrorState(rowView);
                break;
        }

        SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yy HH:mm", Locale.getDefault());
        date.setText(df.format(payout.getCreateDate()));

        DecimalFormat decFormat = new DecimalFormat(payout.getCurrencyId().getSymbol() + " ###.##");
        String number = decFormat.format(payout.getAmount());
        amount.setText(number);

        /*
        if(i >= getCount() - 1 && onScrolledToBottomListener != null) {
            onScrolledToBottomListener.onScrolledToBottom();
        }
*/
        return rowView;
    }

    private void displayErrorState(View view) {
        state.setText(R.string.payout_error_state);
        amount.setTextColor(Color.RED);
    }

    private void displayProcessingState(View view) {
        state.setText(R.string.payout_precessing_state);
        amount.setTextColor(ContextCompat.getColor(view.getContext(), R.color.processing_state_color));
    }

    private void displayAcceptedState(View view) {
        state.setText(R.string.payout_accepted_state);
        amount.setTextColor(ContextCompat.getColor(view.getContext(), R.color.accepted_state_color));
    }

    public interface OnScrolledToBottomListener {
        void onScrolledToBottom();
    }


}
