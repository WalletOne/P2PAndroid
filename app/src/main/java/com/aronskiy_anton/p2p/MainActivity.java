package com.aronskiy_anton.p2p;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aronskiy_anton.p2pui.bankcard.BankCardActivity;
import com.aronskiy_anton.p2pui.payouts.PayoutsActivity;
import com.aronskiy_anton.p2pui.refunds.RefundsActivity;
import com.aronskiy_anton.p2pui.util.ActivityUtils;
import com.aronskiy_anton.sdk.P2PCore;
import com.aronskiy_anton.sdk.constants.CurrencyId;
import com.aronskiy_anton.sdk.library.CompleteHandler;
import com.aronskiy_anton.sdk.models.Deal;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        P2PCore.INSTANCE.setBeneficiary("alinakuzmenko", "Alina Kuzmenko", "79287654321");
        P2PCore.INSTANCE.setPayer("vitaliykuzmenko", "Vitaliy Kuzmenko", "79281234567");

/*
        Intent intent = new Intent(this, BankCardActivity.class);
        startActivity(intent);
*/
        Intent intent = new Intent(this, BankCardActivity.class);
        startActivity(intent);

    }

}
