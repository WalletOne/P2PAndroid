package com.aronskiy_anton.p2p.controllers.deal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aronskiy_anton.p2p.R;
import com.aronskiy_anton.p2p.models.Deal;
import com.aronskiy_anton.p2p.models.DealRequest;
import com.aronskiy_anton.p2pui.bankcard.BankCardActivity;
import com.aronskiy_anton.p2pui.bankcard.BankCardPresenter;
import com.aronskiy_anton.p2pui.paydeal.PayDealActivity;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;
import static android.text.InputType.TYPE_NUMBER_FLAG_SIGNED;
import static com.aronskiy_anton.p2pui.bankcard.BankCardActivity.ARG_CARD_ID;
import static com.aronskiy_anton.p2pui.bankcard.BankCardActivity.ARG_OWNER_ID;
import static com.aronskiy_anton.p2pui.bankcard.BankCardActivity.ARG_SHOW_USE_NEW_CARD_LINK;
import static com.aronskiy_anton.p2pui.bankcard.BankCardActivity.REQUEST_SELECT_CARD;
import static com.aronskiy_anton.p2pui.bankcard.BankCardPresenter.Owner.BENEFICIARY;
import static com.aronskiy_anton.p2pui.bankcard.BankCardPresenter.Owner.PAYER;
import static com.aronskiy_anton.p2pui.paydeal.PayDealActivity.ARG_AUTH_DATA;
import static com.aronskiy_anton.p2pui.paydeal.PayDealActivity.ARG_DEAL_ID;
import static com.aronskiy_anton.p2pui.paydeal.PayDealActivity.REQUEST_PAY_DEAL;


/**
 * Created by anton on 14.09.2017.
 */

public class DealFragment extends Fragment implements DealContract.View {

    private DealContract.Presenter presenter;

    private LinearLayout deailInfo;

    private ListView requestsList;

    private LinearLayout noRequestsBlock;

    //private RelativeLayout requestsBlock;

    private ProgressBar requestProgressBar;

    private ProgressBar deailInfoProgressBar;

    private TextView detailTitle;

    private TextView detailDescription;

    private FloatingActionButton fabAddRequest;

    private DealRequestAdapter adapter;


    public static DealFragment newInstance(@Nullable String dealId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_DEAL_ID, dealId);
        DealFragment fragment = new DealFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new DealRequestAdapter(new ArrayList<DealRequest>(0), itemListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.deal_detail_fragment, container, false);

        deailInfo = (LinearLayout) root.findViewById(R.id.deal_info);
        deailInfoProgressBar = (ProgressBar) root.findViewById(R.id.deal_info_progress_bar);
        //requestsBlock = (RelativeLayout) root.findViewById(R.id.requests_block);
        requestsList = (ListView) root.findViewById(R.id.requests_list);
        requestsList.setAdapter(adapter);

        requestProgressBar = root.findViewById(R.id.requests_progress_bar);

        noRequestsBlock = (LinearLayout) root.findViewById(R.id.no_requests);

        //Set up fab
        fabAddRequest = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_request);
        fabAddRequest.setImageResource(R.drawable.ic_add);
        fabAddRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddRequestDialog();
            }
        });

        detailTitle = (TextView) root.findViewById(R.id.deal_detail_title);
        detailDescription = (TextView) root.findViewById(R.id.deal_detail_description);

        return root;
    }

    private boolean isFabShouldShown() {
        return presenter.isFabShouldShown();
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
        deailInfoProgressBar.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAddRequestDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        final EditText editText = new EditText(getContext());
        editText.setInputType(TYPE_NUMBER_FLAG_DECIMAL | TYPE_NUMBER_FLAG_SIGNED | TYPE_CLASS_NUMBER);

        alert.setTitle(R.string.create_request);
        alert.setMessage(R.string.what_amount_you_do_this_deal);

        alert.setView(editText);

        alert.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = editText.getText().toString();
                presenter.createDealRequest(value);
                showBankCardActivityForSelect(BENEFICIARY);

            }
        });

        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SELECT_CARD:
                if (resultCode == RESULT_OK) {
                    Integer cardId = data.getIntExtra(ARG_CARD_ID, 0);
                    presenter.setSelectedCardId(cardId);

                    switch (presenter.getUserTypeId()) {
                        case EMPLOYER:
                            presenter.createP2PDeal(cardId == 0 ? null : cardId);
                            break;
                        case FREELANCER:
                            presenter.addCreatedDealRequest();
                            break;
                    }
                } else if (resultCode == BankCardActivity.RESULT_FAIL) {
                    Snackbar.make(getView(), "Select paymentTool result error", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PAY_DEAL:
                if (resultCode == RESULT_OK) {
                    presenter.onPayRequestResultOk();
                } else if (resultCode == BankCardActivity.RESULT_FAIL) {
                    Snackbar.make(getView(), "Pay request result error", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void showDealInfo(Deal deal) {
        deailInfo.setVisibility(View.VISIBLE);
        detailTitle.setText(deal.getTitle());
        detailDescription.setText(deal.getShortDescription());
    }

    @Override
    public void showMissingDeal() {
        detailTitle.setText("");
        detailDescription.setText(R.string.no_data);
        deailInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showLoadingRequestsError() {
        Snackbar.make(getView(), "Loading requests error", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showNoRequests() {
        requestsList.setVisibility(View.GONE);
        noRequestsBlock.setVisibility(View.VISIBLE);
        fabAddRequest.setVisibility(isFabShouldShown() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showRequests(List<DealRequest> requests) {
        adapter.replaceData(requests);
        noRequestsBlock.setVisibility(View.GONE);
        requestsList.setVisibility(View.VISIBLE);
        fabAddRequest.setVisibility(isFabShouldShown() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRequestLoadingIndicator(boolean show) {
        requestProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showEmployerDialog(final DealRequest request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.change_deal_request_status);

        DialogInterface.OnClickListener acceptListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                presenter.acceptDeal(request);
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                presenter.cancelDeal(request);
            }
        };

        DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                presenter.confirmDeal(request);
            }
        };

        switch (request.getStateId()) {
            case created:
                builder.setPositiveButton(R.string.accept_button_label, acceptListener);
                break;
            case paymentProcessing:
                return;
            case paid:
                builder.setNegativeButton(R.string.cancel_deal_button_label, cancelListener);
                break;
            case canceling:
            case canceled:
                return;
            case paymentError:
                builder.setPositiveButton(R.string.try_again_button_label, acceptListener);
                builder.setNegativeButton(R.string.cancel_deal_button_label, cancelListener);
                break;
            case completed:
                builder.setPositiveButton(R.string.confirm_completition_button_label, confirmListener);
                break;
            case confirming:
            case payoutProcessing:
            case payoutProcessingError:
                return;
            default:
                return;
        }

        builder.show();
    }

    @Override
    public void showFreelancerDialog(final DealRequest request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.change_deal_request_status);

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                presenter.cancelRequest(request);
            }
        };

        DialogInterface.OnClickListener completeListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                presenter.completeRequest(request);
            }
        };

        switch (request.getStateId()) {
            case created:
                builder.setNegativeButton(R.string.cancel_request, cancelListener);
                break;
            case paymentProcessing:
                return;
            case paid:
                builder.setPositiveButton(R.string.complete, completeListener);
                break;
            case canceling:
            case canceled:
            case paymentError:
            case completed:
            case confirming:
            case payoutProcessing:
            case payoutProcessingError:
                return;
            default:
                return;
        }

        builder.show();
    }

    @Override
    public void showBankCardActivityForSelect(BankCardPresenter.Owner owner) {
        Intent intent = new Intent(getContext(), BankCardActivity.class);
        intent.putExtra(ARG_OWNER_ID, owner);
        intent.putExtra(ARG_SHOW_USE_NEW_CARD_LINK, owner == PAYER);
        startActivityForResult(intent, REQUEST_SELECT_CARD);
    }

    @Override
    public void showAlertToEnterCVV(boolean redirectToCardAddition) {

        // Existing bank paymentTool
        if (!redirectToCardAddition) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle(R.string.enter_cvv);

            final EditText editText = new EditText(getContext());
            editText.setInputType(TYPE_NUMBER_FLAG_SIGNED | TYPE_CLASS_NUMBER);

            int maxLength = 3;
            editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

            editText.setHint(R.string.cvv_cvc_3_digits);

            builder.setView(editText);

            DialogInterface.OnClickListener payListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String authData = editText.getText().toString();
                    presenter.onPayDealButtonClicked(authData);
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };

            builder.setPositiveButton(R.string.pay_button_label, payListener);
            builder.setNegativeButton(R.string.cancel, cancelListener);

            builder.show();
        } else {
            presenter.onPayDealButtonClicked("");
        }
    }

    @Override
    public void showPayDealActivity(String authData, String dealId) {
        Intent intent = new Intent(getContext(), PayDealActivity.class);
        intent.putExtra(ARG_AUTH_DATA, authData);
        intent.putExtra(ARG_DEAL_ID, dealId);
        startActivityForResult(intent, REQUEST_PAY_DEAL);
    }

    @Override
    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setEmployerDealTitle() {
        setTitle(getString(R.string.deal_employer_view_title));
    }

    @Override
    public void setFreelancerDealTitle() {
        setTitle(getString(R.string.deal_freelancer_view_title));
    }

    public void setTitle(String title) {
        ((DealActivity) getActivity()).setActionBarTitle(title);
    }

    /**
     * Listener for clicks on tasks in the ListView.
     */
    RequestItemListener itemListener = new RequestItemListener() {
        @Override
        public void onRequestClick(DealRequest clickedRequest) {
            presenter.onRequestClick(clickedRequest);
        }
    };

    public interface RequestItemListener {

        void onRequestClick(DealRequest clickedRequest);

    }


}
