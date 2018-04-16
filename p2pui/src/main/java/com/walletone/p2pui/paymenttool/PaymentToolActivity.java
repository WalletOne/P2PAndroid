package com.walletone.p2pui.paymenttool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.walletone.p2pui.R;
import com.walletone.p2pui.W1P2PToolbar;
import com.walletone.p2pui.library.Owner;
import com.walletone.p2pui.util.ActivityUtils;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class PaymentToolActivity extends AppCompatActivity {

    private static final String LOG_TAG = "W1P2PToolbar";

    public static final String ARG_OWNER_ID = "PaymentToolActivity.ARG_OWNER_ID";
    public static final String ARG_PAYMENT_TOOL_ID = "PaymentToolActivity.ARG_PAYMENT_TOOL_ID";
    public static final String ARG_SHOW_USE_NEW_PAYMENT_TOOL_LINK = "PaymentToolActivity.ARG_SHOW_USE_NEW_PAYMENT_TOOL_LINK";

    public static final int REQUEST_SELECT_PAYMENT_TOOL = 1;
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

        Owner owner = (Owner) getIntent().getSerializableExtra(ARG_OWNER_ID);

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        LinearLayout toolBarContainer = findViewById(R.id.container);

        int[] location = new int[2];
        toolBarContainer.getLocationOnScreen(location);
        int locationY = location[1];

        Log.d(LOG_TAG, "locationY: " + locationY);
        Log.d(LOG_TAG, "getY(): " + toolBarContainer.getY());

        if(locationY <= 0) {
            Log.d(LOG_TAG, "setY(): " + getStatusBarHeight());
            toolBarContainer.setY(getStatusBarHeight());
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
