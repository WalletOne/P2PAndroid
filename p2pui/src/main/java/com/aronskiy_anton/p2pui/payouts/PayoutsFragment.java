package com.aronskiy_anton.p2pui.payouts;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.p2pui.bankcard.BankCardAdapter;
import com.aronskiy_anton.p2pui.bankcard.BankCardFragment;
import com.aronskiy_anton.sdk.models.BankCard;
import com.aronskiy_anton.sdk.models.Payout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anton on 12.09.2017.
 */

public class PayoutsFragment extends android.support.v4.app.Fragment implements PayoutsContract.View,
        PayoutsAdapter2.OnScrolledToBottomListener{

    private View noPayoutsView;

    private ListView payoutsListView;

    private RecyclerView payoutsListView2;

    private PayoutsAdapter payoutsAdapter;

    private PayoutsAdapter2 payoutsAdapter2;

    private LinearLayoutManager layoutManager;

    private boolean isLoading = false;

    private Integer pageCount = 0;

    PayoutsContract.Presenter presenter;

    public PayoutsFragment() {
    }

    public static PayoutsFragment newInstance() {
        return new PayoutsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //payoutsAdapter = new PayoutsAdapter(new ArrayList<Payout>(0));

        payoutsAdapter2 = new PayoutsAdapter2(new ArrayList<Payout>(0));
        payoutsAdapter2.setOnScrolledToBottomListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.payouts_fragment_layout, container, false);

        // Set up bank payouts view
        //payoutsListView = root.findViewById(R.id.payouts_list);
        //payoutsListView.setAdapter(payoutsAdapter);

        layoutManager = new LinearLayoutManager(getContext());
        payoutsListView2 = root.findViewById(R.id.payouts_list2);
        payoutsListView2.setLayoutManager(layoutManager);
        payoutsListView2.setAdapter(payoutsAdapter2);



        // Set up no payouts view
        noPayoutsView = root.findViewById(R.id.no_payouts);
/*
        link_card_item = root.findViewById(R.id.link_card_item);
        link_card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addNewCard();
            }
        });
*/
        return root;
    }

    @Override
    public void setPresenter(@NonNull PayoutsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPayouts(List<Payout> payouts) {
        //payoutsAdapter.replaceData(payouts);
        payoutsAdapter2.replaceData(payouts);
        //payoutsListView.setVisibility(View.VISIBLE);
        payoutsListView2.setVisibility(View.VISIBLE);
        noPayoutsView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showEmptyList() {

    }

    @Override
    public void onScrolledToBottom() {
        presenter.loadMore();
    }

}
