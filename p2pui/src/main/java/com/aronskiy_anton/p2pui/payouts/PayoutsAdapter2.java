package com.aronskiy_anton.p2pui.payouts;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.sdk.models.Payout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.support.v4.util.Preconditions.checkNotNull;
import static com.aronskiy_anton.sdk.models.Payout.PAYOUT_STATE_ID_ACCEPTED;
import static com.aronskiy_anton.sdk.models.Payout.PAYOUT_STATE_ID_PROCESSING;
import static com.aronskiy_anton.sdk.models.Payout.PAYOUT_STATE_ID_PROCESS_ERROR;

/**
 * Created by anton on 12.09.2017.
 */

public class PayoutsAdapter2 extends RecyclerView.Adapter<PayoutsAdapter2.ViewHolder> {

    private List<Payout> payouts;

    private OnScrolledToBottomListener onScrolledToBottomListener;

    public PayoutsAdapter2(List<Payout> payouts) {
        this.payouts = payouts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payout_item, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Payout payout = payouts.get(position);
        holder.setData(payout);

        if(position >= getItemCount() - 1 && onScrolledToBottomListener != null) {
            onScrolledToBottomListener.onScrolledToBottom();
        }
    }

    @Override
    public int getItemCount() {
        return payouts.size();
    }


    public void replaceData(List<Payout> payouts) {
        setList(payouts);
        notifyDataSetChanged();
    }

    @SuppressLint("RestrictedApi")
    private void setList(List<Payout> payouts) {
        this.payouts = checkNotNull(payouts);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView state;
        private TextView date;
        private TextView amount;

        private Payout payout;

        public ViewHolder(View itemView) {
            super(itemView);

            state = itemView.findViewById(R.id.payout_state);
            date = itemView.findViewById(R.id.payout_date);
            amount = itemView.findViewById(R.id.payout_amount);
        }

        public void setData(Payout payout){
            this.payout = payout;

            switch (payout.getPayoutStateId()){
                case PAYOUT_STATE_ID_ACCEPTED:
                    displayAcceptedState(itemView);
                    break;
                case PAYOUT_STATE_ID_PROCESSING:
                    displayProcessingState(itemView);
                    break;
                case PAYOUT_STATE_ID_PROCESS_ERROR:
                    displayErrorState(itemView);
                    break;
            }

            SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yy HH:mm", Locale.getDefault());
            date.setText(df.format(payout.getCreateDate()));

            DecimalFormat decFormat = new DecimalFormat(payout.getCurrencyId().getSymbol() + " ###.##");
            String number = decFormat.format(payout.getAmount());
            amount.setText(number);
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
    }

    public void setOnScrolledToBottomListener(OnScrolledToBottomListener onScrolledToBottomListener) {
        this.onScrolledToBottomListener = onScrolledToBottomListener;
    }

    public interface OnScrolledToBottomListener {
        void onScrolledToBottom();
    }

}
