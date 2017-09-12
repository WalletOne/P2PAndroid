package com.aronskiy_anton.p2pui.linkcard;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aronskiy_anton.p2pui.R;
import com.aronskiy_anton.p2pui.bankcard.BankCardPresenter;
import com.aronskiy_anton.sdk.P2PCore;
import com.aronskiy_anton.sdk.library.Base64;
import com.aronskiy_anton.sdk.models.RequestBuilder;

import java.io.UnsupportedEncodingException;

/**
 * Created by anton on 11.09.2017.
 */

public class LinkCardActivity extends AppCompatActivity {

    public static final int RESULT_FAIL = RESULT_FIRST_USER + 1;

    private final String RETURN_HOST = "p2p-success-link-new-card";

    public static final int REQUEST_LINK_CARD = 1;

    private boolean isVisible = false;
    private boolean needFinish = false;

    BankCardPresenter.Owner owner = BankCardPresenter.Owner.BENEFICIARY;

    WebView linkCardWebView;

    private boolean finishEventDispatched = false;

    public LinkCardActivity() {
    }

    public LinkCardActivity(BankCardPresenter.Owner owner) {
        this.owner = owner;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.link_card_activity_layout);

        linkCardWebView = findViewById(R.id.link_card_web_view);


        final RequestBuilder request = P2PCore.INSTANCE.beneficiariesCards.linkNewCardRequest("http://" + RETURN_HOST);

        try {
            String postData = request.getHttpBody();
            linkCardWebView.setWebViewClient(new MyWebViewClient());
            setupWebView();
            linkCardWebView.postUrl(request.getUrlString(), postData.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setupWebView() {
        final WebSettings wbs = linkCardWebView.getSettings();
        wbs.setPluginState(WebSettings.PluginState.ON);
        wbs.setSaveFormData(true);
        wbs.setDomStorageEnabled(true);
        wbs.setJavaScriptEnabled(true);
        wbs.setBuiltInZoomControls(true);
        wbs.setAllowFileAccess(true);
        wbs.setSupportZoom(true);
        linkCardWebView.setWebViewClient(new MyWebViewClient());
        linkCardWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return true;
            }
        });
    }


    private class MyWebViewClient extends WebViewClient {
        private String pendingUrl;


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (pendingUrl == null) {
                pendingUrl = url;
            }
            //showProgress();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return checkUrl(url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url == null) url = "";

            //hideProgress();
            checkUrl(url);
            if (!url.equals(pendingUrl)) {

                pendingUrl = null;
            }
        }
    }

    private boolean checkUrl(String url) {
        if (url == null) url = "";
        url = url.toLowerCase();
        boolean handled = false;
        if (!finishEventDispatched) {
            if (url.equals("http://" + RETURN_HOST + "/")) {
                handled = true;
                finishEventDispatched = true;
                onResultSuccess();
            } else {
                //handled = true;
                //finishEventDispatched = true;
                //onResultFail();
            }
        }
        return handled;
    }

    public void onResultSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    public void onResultFail() {
        setResult(RESULT_FAIL);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        if(needFinish) {
            finish();
            needFinish = false;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    public void onBackPressed() {
        if(isVisible) {
            super.onBackPressed();
        } else {
            needFinish = true;
        }
    }

    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}