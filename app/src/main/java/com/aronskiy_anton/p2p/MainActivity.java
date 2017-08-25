package com.aronskiy_anton.p2p;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aronskiy_anton.sdk.P2PCore;
import com.aronskiy_anton.sdk.managers.NetworkManager;

public class MainActivity extends AppCompatActivity {

    AsyncRequest asyncRequest;
    String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        P2PCore core = new P2PCore();
        core.setPlatform("testplatform", "TestPlatformSignatureKey");
        asyncRequest = new AsyncRequest();
        asyncRequest.execute(core);


    }

    class AsyncRequest extends AsyncTask<P2PCore, Void, Void> {

        @Override
        protected Void doInBackground(P2PCore... arg) {
            urlString = "https://api.dev.walletone.com/p2p/api/v2/beneficiaries/alinakuzmenko/cards";
            arg[0].networkManager.request(
                    urlString,
                    NetworkManager.MethodType.GET,
                    null);
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

        }
    }
}
