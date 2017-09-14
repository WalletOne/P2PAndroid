package com.aronskiy_anton.p2pui.refunds;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.sdk.models.Refund;
import com.aronskiy_anton.sdk.models.Refund;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.support.v4.util.Preconditions.checkNotNull;
import static com.aronskiy_anton.sdk.models.Refund.REFUND_STATE_ID_ACCEPTED;
import static com.aronskiy_anton.sdk.models.Refund.REFUND_STATE_ID_PROCESSING;
import static com.aronskiy_anton.sdk.models.Refund.REFUND_STATE_ID_PROCESS_ERROR;

/**
 * Created by anton on 12.09.2017.
 */

public class RefundsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_PROGRESSBAR = 0;
    private boolean isFooterEnabled = true;

    private List<Refund> refunds;

    private OnScrolledToBottomListener onScrolledToBottomListener;

    public RefundsAdapter(List<Refund> refunds) {
        this.refunds = refunds;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PROGRESSBAR) {
            View view = createProgressView(parent);
            return new ProgressViewHolder(view);
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.refund_item, parent, false)) {
            };
        }
    }

    private View createProgressView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_item, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ProgressViewHolder) {
        } else if (refunds.size() > 0 && position < refunds.size()) {
            ((ViewHolder) holder).setData(refunds.get(position));
        }

        if(position > 0 && position >= refunds.size() && onScrolledToBottomListener != null) {
            onScrolledToBottomListener.onScrolledToBottom();
        }
    }

    @Override
    public int getItemCount() {
        return  (isFooterEnabled) ? refunds.size() + 1 : refunds.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (isFooterEnabled && position >= refunds.size()) ? VIEW_TYPE_PROGRESSBAR : VIEW_TYPE_ITEM;
    }

    public void replaceData(List<Refund> refunds) {
        setList(refunds);
        notifyDataSetChanged();
    }

    @SuppressLint("RestrictedApi")
    private void setList(List<Refund> refunds) {
        this.refunds = checkNotNull(refunds);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView state;
        private TextView date;
        private TextView amount;

        private Refund refund;

        public ViewHolder(View itemView) {
            super(itemView);

            state = itemView.findViewById(R.id.refund_state);
            date = itemView.findViewById(R.id.refund_date);
            amount = itemView.findViewById(R.id.refund_amount);
        }

        public void setData(Refund refund) {
            this.refund = refund;

            switch (refund.getRefundStateId()) {
                case REFUND_STATE_ID_ACCEPTED:
                    displayAcceptedState(itemView);
                    break;
                case REFUND_STATE_ID_PROCESSING:
                    displayProcessingState(itemView);
                    break;
                case REFUND_STATE_ID_PROCESS_ERROR:
                    displayErrorState(itemView);
                    break;
            }

            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
            date.setText(df.format(refund.getCreateDate()));

            DecimalFormat decFormat = new DecimalFormat(refund.getCurrencyId().getSymbol() + " ###.##");
            String number = decFormat.format(refund.getAmount());
            amount.setText(number);
        }

        private void displayErrorState(View view) {
            state.setText(R.string.refund_error_state);
            amount.setTextColor(Color.RED);
        }

        private void displayProcessingState(View view) {
            state.setText(R.string.refund_precessing_state);
            amount.setTextColor(ContextCompat.getColor(view.getContext(), R.color.processing_state_color));
        }

        private void displayAcceptedState(View view) {
            state.setText(R.string.refund_accepted_state);
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
