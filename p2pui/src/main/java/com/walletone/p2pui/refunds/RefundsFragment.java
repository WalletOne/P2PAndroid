package com.walletone.p2pui.refunds;

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
import com.walletone.sdk.models.Refund;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aronskiy Anton on 12.09.2017.
 */

public class RefundsFragment extends android.support.v4.app.Fragment implements RefundsContract.View,
        RefundsAdapter.OnScrolledToBottomListener{

    private View noRefundsView;

    private RecyclerView refundsListView;

    private RefundsAdapter refundsAdapter;

    private RefundsContract.Presenter presenter;

    public RefundsFragment() {
    }

    public static RefundsFragment newInstance() {
        return new RefundsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refundsAdapter = new RefundsAdapter(new ArrayList<Refund>(0));
        refundsAdapter.setOnScrolledToBottomListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.refunds_fragment_layout, container, false);

        // Set up refunds view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        refundsListView = root.findViewById(R.id.refunds_list);
        refundsListView.setLayoutManager(layoutManager);
        refundsListView.setHasFixedSize(true);
        refundsListView.setAdapter(refundsAdapter);

        // Set up no refunds view
        noRefundsView = root.findViewById(R.id.no_refunds);
        return root;
    }

    @Override
    public void setPresenter(@NonNull RefundsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showRefunds(List<Refund> refunds) {
        refundsAdapter.replaceData(refunds);
        refundsListView.setVisibility(View.VISIBLE);
        noRefundsView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showEmptyList() {
        refundsListView.setVisibility(View.GONE);
        noRefundsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable error) {
        Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAllDataAreLoaded() {
        refundsAdapter.disableLoadMore();
    }

    @Override
    public void onScrolledToBottom() {
        presenter.loadMore();
    }

}
