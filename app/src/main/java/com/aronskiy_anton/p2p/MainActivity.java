package com.aronskiy_anton.p2p;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aronskiy_anton.p2pui.bankcard.BankCardActivity;
import com.aronskiy_anton.sdk.P2PCore;
import com.aronskiy_anton.sdk.constants.CurrencyId;
import com.aronskiy_anton.sdk.library.CompleteHandler;
import com.aronskiy_anton.sdk.models.Deal;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    AsyncRequest asyncRequest;
    String urlString = "https://api.dev.walletone.com/p2p/api/v2/beneficiaries/alinakuzmenko/cards";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        P2PCore.INSTANCE.setBeneficiary("alinakuzmenko", "Alina Kuzmenko", "79287654321");
        //core.setBeneficiary("alinakuzmenko", "Alina Kuzmenko", "79287654321");
        //core.setPayer("vitaliykuzmenko", "Vitaliy Kuzmenko", "79281234567");
        //asyncRequest = new AsyncRequest();
        //asyncRequest.execute(core);


        Intent intent = new Intent(this, BankCardActivity.class);
        startActivity(intent);


    }

    static class AsyncRequest extends AsyncTask<P2PCore, Void, Void> {

        @Override
        protected Void doInBackground(P2PCore... arg) {


/*
            arg[0].dealsManager.status("9B9B32AF-F75C-4294-B2BC-BA88B80D09F0", new CompleteHandler<Deal, Throwable>() {
                @Override
                public void completed(Deal model, Throwable var2) {
                    System.out.print(model.toString());
                }
            });

*/

/*
            arg[0].payoutsManager.payouts(0, 20, "9B9B32AF-F75C-4294-B2BC-BA88B80D09F0", new CompleteHandler<PayoutResult, Throwable>() {
                @Override
                public void completed(PayoutResult list, Throwable var2) {
                    System.out.print("");
                }
            });
*/

/*
            arg[0].refundsManager.refunds(0, 20, "9B9B32AF-F75C-4294-B2BC-BA88B80D09F0", new CompleteHandler<RefundsResult, Throwable>() {
                @Override
                public void completed(RefundsResult list, Throwable var2) {
                    System.out.print("");
                }
            });
*/
/*
            arg[0].payersCards.cards(new CompleteHandler<List<BankCard>, Throwable>() {
                @Override
                public void completed(List<BankCard> list, Throwable var2) {
                    System.out.print(TextUtils.join(";", list));
                }
            });
*/
/*
            arg[0].beneficiariesCards.cards(new CompleteHandler<List<BankCard>, Throwable>() {
                @Override
                public void completed(List<BankCard> list, Throwable var2) {
                    System.out.print(TextUtils.join(";", list));
                }

                @Override
                public void failed(Throwable var1, Throwable var2) {

                }
            });
            */

/*
            arg[0].beneficiariesCards.card(98, new CompleteHandler<BankCard, Throwable>() {
                @Override
                public void completed(BankCard model, Throwable var2) {
                    System.out.print(model.toString());
                }

            });
*/

           arg[0].dealsManager.create(
                   "antontest",
                   "vitaliykuzmenko",
                   "alinakuzmenko",
                   "79281234567",
                   99,
                   98,
                   new BigDecimal(20.0),
                   CurrencyId.RUB,
                   "Short description!",
                   "Fuuuuuul description",
                   true,
                   new CompleteHandler<Deal, Throwable>() {
                @Override
                public void completed(Deal model, Throwable var2) {
                    System.out.print(model.toString());
                }

            });


            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

        }
    }
}
