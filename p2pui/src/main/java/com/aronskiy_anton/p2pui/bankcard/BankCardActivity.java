package com.aronskiy_anton.p2pui.bankcard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.p2pui.util.ActivityUtils;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public class BankCardActivity extends AppCompatActivity {

    public static final String ARG_OWNER_ID = "BankCardActivity.ARG_OWNER_ID";

    private String ownerId;

    private BankCardPresenter.Owner owner = BankCardPresenter.Owner.BENEFICIARY;

    private BankCardPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card_layout);

        setTitle(getResources().getString(R.string.bank_cards_activity_title));

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        BankCardFragment bankCardFragment =
                (BankCardFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (bankCardFragment == null) {
            // Create the fragment
            bankCardFragment = BankCardFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), bankCardFragment, R.id.contentFrame);
        }

        // Get the requested owner id
        ownerId = getIntent().getStringExtra(ARG_OWNER_ID);
        owner = getOwnerById(ownerId);

        presenter = new BankCardPresenter(owner, bankCardFragment);
    }

    private BankCardPresenter.Owner getOwnerById(String id){
        if("beneficiary".equals(id)){
            return BankCardPresenter.Owner.BENEFICIARY;
        } else if ("payer".equals(id)){
            return BankCardPresenter.Owner.PAYER;
        } else{
            return BankCardPresenter.Owner.BENEFICIARY;
        }
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
