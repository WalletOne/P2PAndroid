package com.walletone.p2pui.paymenttool;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.walletone.p2pui.R;
import com.walletone.p2pui.linkpaymenttool.LinkPaymentToolActivity;
import com.walletone.sdk.models.PaymentTool;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.util.Preconditions.checkNotNull;
import static com.walletone.p2pui.paymenttool.PaymentToolActivity.ARG_PAYMENT_TOOL_ID;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class PaymentToolFragment extends android.support.v4.app.Fragment implements PaymentToolContract.View {

    private View noPaymentToolsView;

    private View paymentToolsBlockView;

    private ViewGroup linkPaymentToolItem;

    private RecyclerView paymentToolsRecyclerView;

    private PaymentToolAdapter paymentToolsAdapter;

    private PaymentToolContract.Presenter presenter;

    private ProgressBar progressBar;

    public PaymentToolFragment() {
    }

    public static PaymentToolFragment newInstance() {
        return new PaymentToolFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentToolsAdapter = new PaymentToolAdapter(new ArrayList<PaymentTool>(0), itemListener, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.payment_tool_fragment_layout, container, false);

        paymentToolsBlockView = root.findViewById(R.id.payment_tools_block);

        // Set up recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        paymentToolsRecyclerView = root.findViewById(R.id.payment_tools_list);
        paymentToolsRecyclerView.setLayoutManager(layoutManager);
        paymentToolsRecyclerView.setAdapter(paymentToolsAdapter);
        setupItemTouchHelper();

        // Set up no paymentTools view
        noPaymentToolsView = root.findViewById(R.id.no_payment_tools);

        linkPaymentToolItem = root.findViewById(R.id.link_card_item);
        linkPaymentToolItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addNewPaymentTool();
            }
        });
        setLinkPaymentToolVisibility(presenter.isAddNewPaymentToolAvailable());

        progressBar = root.findViewById(R.id.progress_bar);

        setHasOptionsMenu(true);

        return root;
    }

    private void setupItemTouchHelper() {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final PaymentTool paymentTool = ((PaymentToolAdapter.ItemViewHolder) viewHolder).getPaymentTool();

                if (direction == ItemTouchHelper.LEFT) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.delete_payment_tool_confirmation);

                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            paymentToolsAdapter.notifyItemRangeChanged(position, paymentToolsAdapter.getItemCount());
                        }
                    }).setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            paymentToolsAdapter.removeItem(position);
                            presenter.deletePaymentTool(paymentTool);
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(paymentToolsRecyclerView);
    }

    private void setLinkPaymentToolVisibility(boolean visibility) {
        linkPaymentToolItem.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(presenter.isAddNewPaymentToolAvailable()) {
            inflater.inflate(R.menu.payment_tools_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Integer itemId = item.getItemId();
        if (itemId.equals(R.id.menu_link_card)) {
            presenter.addNewPaymentTool();
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(PaymentToolContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void showPaymentTools(List<PaymentTool> paymentTools) {
        paymentToolsAdapter.replaceData(paymentTools);
        paymentToolsBlockView.setVisibility(View.VISIBLE);
        noPaymentToolsView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyList() {
        noPaymentToolsView.setVisibility(View.VISIBLE);
        paymentToolsBlockView.setVisibility(View.GONE);
    }

    @Override
    public void showLinkPaymentToolActivity() {
        Intent intent = new Intent(getContext(), LinkPaymentToolActivity.class);
        startActivityForResult(intent, LinkPaymentToolActivity.REQUEST_LINK_PAYMENT_TOOL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case LinkPaymentToolActivity.REQUEST_LINK_PAYMENT_TOOL:
                if (resultCode == RESULT_OK) {
                    presenter.loadPaymentTools(true);
                } else if (resultCode == LinkPaymentToolActivity.RESULT_FAIL) {
                    Toast.makeText(getContext(), "Link paymentTool result error", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void setLoadingIndicator(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void closePaymentToolAndShowPayDealActivity() {
        Intent intent = new Intent();
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void showError(Throwable error) {
        Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Listener for clicks on bank paymentTool in the ListView.
     */
    PaymentToolItemListener itemListener = new PaymentToolItemListener() {

        @Override
        public void onPaymentToolClick(PaymentTool clickedPaymentTool) {
            Intent intent = new Intent();
            intent.putExtra(ARG_PAYMENT_TOOL_ID, clickedPaymentTool.getPaymentToolId());
            getActivity().setResult(RESULT_OK, intent);
            getActivity().finish();
        }
    };

    public interface PaymentToolItemListener {

        void onPaymentToolClick(PaymentTool clickedPaymentTool);
    }
}
