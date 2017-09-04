package com.aronskiy_anton.p2p;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aronskiy_anton.sdk.P2PCore;
import com.aronskiy_anton.sdk.library.CompleteHandler;
import com.aronskiy_anton.sdk.models.Deal;

public class MainActivity extends AppCompatActivity {

    AsyncRequest asyncRequest;
    String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        P2PCore core = new P2PCore();
        core.setPlatform("testplatform", "TestPlatformSignatureKey");
        core.setBeneficiary("alinakuzmenko", "Alina Kuzmenko", "79287654321");
        core.setPayer("vitaliykuzmenko", "Vitaliy Kuzmenko", "79281234567");
        asyncRequest = new AsyncRequest();
        asyncRequest.execute(core);


    }

    class AsyncRequest extends AsyncTask<P2PCore, Void, Void> {

        @Override
        protected Void doInBackground(P2PCore... arg) {
            urlString = "https://api.dev.walletone.com/p2p/api/v2/beneficiaries/alinakuzmenko/cards";

//            arg[0].networkManager.request(urlString, NetworkManager.MethodType.GET, null, BankCard.class);
/*
            arg[0].networkManager.request(
                    urlString,
                    NetworkManager.MethodType.GET,
                    null);
*/

            arg[0].dealsManager.status("9B9B32AF-F75C-4294-B2BC-BA88B80D09F0", new CompleteHandler<Deal, Throwable>() {
                @Override
                public void completed(Deal model, Throwable var2) {
                    System.out.print(model.toString());
                }
            });

            arg[0].dealsManager.status("9B9B32AF-F75C-4294-B2BC-BA88B80D09F0", new CompleteHandler<Deal, Throwable>() {
                @Override
                public void completed(Deal model, Throwable var2) {
                    System.out.print(model.toString());
                }
            });

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

                @Override
                public void failed(Throwable var1, Throwable var2) {

                }
            });
*/

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

        }
    }
}
