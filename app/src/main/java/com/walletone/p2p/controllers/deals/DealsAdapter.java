package com.walletone.p2p.controllers.deals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.walletone.p2p.R;
import com.walletone.p2p.models.Deal;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by anton on 14.09.2017.
 */

public class DealsAdapter extends BaseAdapter {
    
    private List<Deal> deals;
    private DealsFragment.DealItemListener mItemListener;

    public DealsAdapter(List<Deal> deals, DealsFragment.DealItemListener itemListener) {
        setList(deals);
        mItemListener = itemListener;
    }

    public void replaceData(List<Deal> deals) {
        setList(deals);
        notifyDataSetChanged();
    }

    private void setList(List<Deal> deals) {
        this.deals = deals;
    }

    @Override
    public int getCount() {
        return deals.size();
    }

    @Override
    public Deal getItem(int i) {
        return deals.get(i);
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
            rowView = inflater.inflate(R.layout.deal_item, viewGroup, false);
        }

        final Deal deal = getItem(i);

        TextView titleTV = (TextView) rowView.findViewById(R.id.title);
        titleTV.setText(deal.getTitle());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onDealClick(deal);
            }
        });

        return rowView;
    }
}
