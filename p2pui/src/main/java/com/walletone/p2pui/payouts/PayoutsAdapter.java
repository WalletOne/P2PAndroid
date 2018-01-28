package com.walletone.p2pui.payouts;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.walletone.p2pui.R;
import com.walletone.sdk.models.Payout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.support.v4.util.Preconditions.checkNotNull;
import static com.walletone.sdk.models.Payout.PAYOUT_STATE_ID_ACCEPTED;
import static com.walletone.sdk.models.Payout.PAYOUT_STATE_ID_PROCESSING;
import static com.walletone.sdk.models.Payout.PAYOUT_STATE_ID_PROCESS_ERROR;

/**
 * Created by Aronskiy Anton on 12.09.2017.
 */

public class PayoutsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_PROGRESSBAR = 0;
    private boolean isFooterEnabled = true;

    private List<Payout> payouts;

    private OnScrolledToBottomListener onScrolledToBottomListener;

    public PayoutsAdapter(List<Payout> payouts) {
        this.payouts = payouts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PROGRESSBAR) {
            View view = createProgressView(parent);
            return new ProgressViewHolder(view);
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.payout_item, parent, false)) {
            };
        }
    }

    private View createProgressView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_item, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (payouts.size() > 0 && position < payouts.size()) {
            ((ViewHolder) holder).setData(payouts.get(position));
        }

        if(position > 0 && position >= payouts.size() && onScrolledToBottomListener != null) {
            onScrolledToBottomListener.onScrolledToBottom();
        }
    }

    @Override
    public int getItemCount() {
        return  (isFooterEnabled) ? payouts.size() + 1 : payouts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (isFooterEnabled && position >= payouts.size()) ? VIEW_TYPE_PROGRESSBAR : VIEW_TYPE_ITEM;
    }

    public void replaceData(List<Payout> payouts) {
        setList(payouts);
        notifyDataSetChanged();
    }

    @SuppressLint("RestrictedApi")
    private void setList(List<Payout> payouts) {
        this.payouts = checkNotNull(payouts);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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

        public void setData(Payout payout) {
            this.payout = payout;

            switch (payout.getPayoutStateId()) {
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

            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
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

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.load_more_progress_bar);
            progressBar.setIndeterminate(true);
        }
    }

    public void disableLoadMore() {
        isFooterEnabled = false;
    }

    public void setOnScrolledToBottomListener(OnScrolledToBottomListener onScrolledToBottomListener) {
        this.onScrolledToBottomListener = onScrolledToBottomListener;
    }

    public interface OnScrolledToBottomListener {
        void onScrolledToBottom();
    }

}
