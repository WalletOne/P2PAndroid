package com.aronskiy_anton.p2pui.paymenttool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.p2pui.W1P2PToolbar;
import com.aronskiy_anton.p2pui.util.ActivityUtils;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class PaymentToolActivity extends AppCompatActivity {

    public static final String ARG_OWNER_ID = "PaymentToolActivity.ARG_OWNER_ID";
    public static final String ARG_PAYMENT_TOOL_ID = "PaymentToolActivity.ARG_PAYMENT_TOOL_ID";
    public static final String ARG_SHOW_USE_NEW_PAYMENT_TOOL_LINK = "PaymentToolActivity.ARG_SHOW_USE_NEW_PAYMENT_TOOL_LINK";

    public static final int REQUEST_SELECT_CARD = 1;
    public static final int RESULT_FAIL = RESULT_FIRST_USER + 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_tool_layout);

        W1P2PToolbar.installToolBar(this);
        setTitle(getResources().getString(R.string.payment_tools_activity_title));

        ActionBar ab = getSupportActionBar();
        if(ab == null) {
            String errorMsg = "This activity requires an AppCompat theme with an action bar, finishing activity...";
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        PaymentToolFragment paymentToolFragment =
                (PaymentToolFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (paymentToolFragment == null) {
            paymentToolFragment = PaymentToolFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), paymentToolFragment, R.id.contentFrame);
        }

        PaymentToolPresenter.Owner owner = (PaymentToolPresenter.Owner) getIntent().getSerializableExtra(ARG_OWNER_ID);

        PaymentToolPresenter presenter = new PaymentToolPresenter(owner, paymentToolFragment);
        presenter.setAddPaymentToolAvailable(getIntent().getBooleanExtra(ARG_SHOW_USE_NEW_PAYMENT_TOOL_LINK, false));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
