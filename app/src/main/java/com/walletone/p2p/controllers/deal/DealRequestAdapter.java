package com.walletone.p2p.controllers.deal;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.walletone.p2p.R;
import com.walletone.p2p.models.DealRequest;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by anton on 15.09.2017.
 */

public class DealRequestAdapter extends BaseAdapter {

    private List<DealRequest> requests;

    private DealFragment.RequestItemListener itemListener;

    public DealRequestAdapter(List<DealRequest> requests, DealFragment.RequestItemListener itemListener) {
        this.requests = requests;
        this.itemListener = itemListener;
    }

    public void replaceData(List<DealRequest> requests) {
        setList(requests);
        notifyDataSetChanged();
    }

    private void setList(List<DealRequest> requests) {
        this.requests = requests;
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Override
    public DealRequest getItem(int i) {
        return requests.get(i);
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
            rowView = inflater.inflate(R.layout.deal_request_item, viewGroup, false);
        }

        final DealRequest request = getItem(i);

        TextView titleTV = (TextView) rowView.findViewById(R.id.request_title);
        titleTV.setText(request.getFreelancer().getTitle());

        TextView amountTV = (TextView) rowView.findViewById(R.id.request_amount);
        DecimalFormat decFormat = new DecimalFormat(" ###.##");
        String number = decFormat.format(request.getAmount());
        formatStatus(amountTV, request, number);
        //amountTV.setText(formatWithStatus(rowView, request, number));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.onRequestClick(request);
            }
        });

        return rowView;
    }

    private void formatStatus(TextView textView, DealRequest request, String number) {
        String text = "";
        //int textColor;
        switch (request.getStateId()) {
            case created:
                text = String.format("%s \u20BD", number);
                break;
            case paymentProcessing:
                text = String.format(textView.getResources().getString(R.string.payment_precessing_status), number);
                break;
            case paymentHold:
                text = String.format(textView.getResources().getString(R.string.payment_hold_status), number);
                break;
            case paid:
                text = String.format(textView.getResources().getString(R.string.paid_status), number);
                break;
            case canceling:
                text = textView.getResources().getString(R.string.canceling_status);
                break;
            case canceled:
                text = String.format(textView.getResources().getString(R.string.canceled_status), number);
                textView.setTextColor(ContextCompat.getColor(textView.getContext(),  R.color.completed_state_color));
                break;
            case paymentError:
                text = String.format(textView.getResources().getString(R.string.payment_error_status), number);
                break;
            case completed:
                text = textView.getResources().getString(R.string.completed_status);
                break;
            case confirming:
                text = textView.getResources().getString(R.string.confirming_status);
                break;
            case payoutProcessing:
                text = textView.getResources().getString(R.string.payout_processing_status);
                textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.processing_state_color));
                break;
            case payoutProcessingError:
                text = textView.getResources().getString(R.string.payout_processing_error_status);
                textView.setTextColor(ContextCompat.getColor(textView.getContext(),  R.color.error_state_color));
                break;
            case done:
                text = textView.getResources().getString(R.string.paid_to_freelancer_status);
                textView.setTextColor(ContextCompat.getColor(textView.getContext(),  R.color.accepted_state_color));
                break;
        }
        textView.setText(text);
    }

    private String formatWithStatus(View view, DealRequest request, String number) {
        switch (request.getStateId()) {
            case created:
                return String.format("%s \u20BD", number);
            case paymentProcessing:
                return String.format(view.getResources().getString(R.string.payment_precessing_status), number);
            case paid:
                return String.format(view.getResources().getString(R.string.paid_status), number);
            case canceling:
                return view.getResources().getString(R.string.canceling_status);
            case canceled:
                return String.format(view.getResources().getString(R.string.canceled_status), number);
            case paymentError:
                return String.format(view.getResources().getString(R.string.payment_error_status), number);
            case completed:
                return view.getResources().getString(R.string.completed_status);
            case confirming:
                return view.getResources().getString(R.string.confirming_status);
            case payoutProcessing:
                return view.getResources().getString(R.string.payout_processing_status);

            case payoutProcessingError:
                return view.getResources().getString(R.string.payout_processing_error_status);

            case done:
                return view.getResources().getString(R.string.paid_to_freelancer_status);
        }
        return null;
    }


}
