package com.walletone.p2pui.payouts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.walletone.p2pui.R;
import com.walletone.sdk.models.Payout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aronskiy Anton on 12.09.2017.
 */

public class PayoutsFragment extends android.support.v4.app.Fragment implements PayoutsContract.View,
        PayoutsAdapter.OnScrolledToBottomListener {

    private View noPayoutsView;

    private RecyclerView payoutsListView;

    private PayoutsAdapter payoutsAdapter;

    private PayoutsContract.Presenter presenter;

    public PayoutsFragment() {
    }

    public static PayoutsFragment newInstance() {
        return new PayoutsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payoutsAdapter = new PayoutsAdapter(new ArrayList<Payout>(0));
        payoutsAdapter.setOnScrolledToBottomListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.payouts_fragment_layout, container, false);

        // Set up payouts view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        payoutsListView = root.findViewById(R.id.payouts_list2);
        payoutsListView.setLayoutManager(layoutManager);
        payoutsListView.setAdapter(payoutsAdapter);

        // Set up no payouts view
        noPayoutsView = root.findViewById(R.id.no_payouts);
        return root;
    }

    @Override
    public void setPresenter(@NonNull PayoutsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPayouts(List<Payout> payouts) {
        payoutsAdapter.replaceData(payouts);
        payoutsListView.setVisibility(View.VISIBLE);
        noPayoutsView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showEmptyList() {
        payoutsListView.setVisibility(View.GONE);
        noPayoutsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable error) {
        Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAllDataAreLoaded() {
        payoutsAdapter.disableLoadMore();
    }

    @Override
    public void onScrolledToBottom() {
        presenter.loadMore();
    }

}
