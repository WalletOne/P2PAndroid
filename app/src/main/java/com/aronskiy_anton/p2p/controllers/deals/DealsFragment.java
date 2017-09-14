package com.aronskiy_anton.p2p.controllers.deals;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.aronskiy_anton.p2p.R;
import com.aronskiy_anton.p2p.models.Deal;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by anton on 13.09.2017.
 */

public class DealsFragment extends Fragment implements DealsContract.View {

    private DealsContract.Presenter presenter;
    
    private DealsAdapter adapter;

    private View noDealsView;

    private LinearLayout dealsView;

    private ProgressBar progressBar;

    private EditText titleDeal;

    private EditText descriptionDeal;

    public DealsFragment() {
    }

    public static DealsFragment newInstance() {
        return new DealsFragment();
    }

    @Override
    public void setPresenter(@NonNull DealsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new DealsAdapter(new ArrayList<Deal>(0), itemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.deals_fragment, container, false);

        dealsView = root.findViewById(R.id.dealsLL);
        noDealsView = root.findViewById(R.id.noDeals);
        progressBar = root.findViewById(R.id.progress_bar);

        titleDeal = root.findViewById(R.id.title_deal);
        descriptionDeal = root.findViewById(R.id.description_deal);

        // Set up tasks view
        ListView listView = (ListView) root.findViewById(R.id.deals_list);
        listView.setAdapter(adapter);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(presenter.isAddButtonAvailable()) {
            inflater.inflate(R.menu.deals_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Integer itemId = item.getItemId();
        if (itemId.equals(R.id.menu_add_deal)) {
            showAddDealDialog();
        }
        return true;
    }

    @Override
    public void setLoadingIndicator(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLoadingDealsError() {
        Snackbar.make(getView(), "Error", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showDeals(List<Deal> deals) {
        adapter.replaceData(deals);
        dealsView.setVisibility(View.VISIBLE);
        noDealsView.setVisibility(View.GONE);
    }

    @Override
    public void showNoDeals() {
        dealsView.setVisibility(View.GONE);
        noDealsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAddDealDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.enter_deal_data);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_deal_dialog2, (ViewGroup) getView(), false);
        final EditText title = (EditText) viewInflated.findViewById(R.id.title_deal);
        final EditText description = (EditText) viewInflated.findViewById(R.id.description_deal);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                presenter.createDeal(title.getText().toString(), description.getText().toString());
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    /**
     * Listener for clicks on tasks in the ListView.
     */
    DealItemListener itemListener = new DealItemListener() {
        @Override
        public void onDealClick(Deal clickedDeal) {
            presenter.openDealDetails(clickedDeal);
        }

    };

    public interface DealItemListener {

        void onDealClick(Deal clickedDeal);

    }
}
