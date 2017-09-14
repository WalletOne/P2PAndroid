package com.aronskiy_anton.p2p.controllers.deal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aronskiy_anton.p2p.R;
import com.aronskiy_anton.p2p.models.Deal;
import static com.aronskiy_anton.p2p.controllers.deal.DealActivity.ARG_DEAL_ID;


/**
 * Created by anton on 14.09.2017.
 */

public class DealFragment extends Fragment implements DealContract.View {

    private DealContract.Presenter presenter;

    private TextView detailTitle;

    private TextView detailDescription;


    public static DealFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_DEAL_ID, taskId);
        DealFragment fragment = new DealFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.deal_detail_fragment, container, false);

        detailTitle = (TextView) root.findViewById(R.id.deal_detail_title);
        detailDescription = (TextView) root.findViewById(R.id.deal_detail_description);


        return root;
    }

    @Override
    public void setPresenter(DealContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showAddRequestDialog() {

    }

    @Override
    public void setTitle(String title) {
        
    }

    @Override
    public void showDealInfo(Deal deal) {
        detailTitle.setText(deal.getTitle());
        detailDescription.setText(deal.getShortDescription());
    }

    @Override
    public void showMissingDeal() {
        detailTitle.setText("");
        detailDescription.setText(R.string.no_data);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
