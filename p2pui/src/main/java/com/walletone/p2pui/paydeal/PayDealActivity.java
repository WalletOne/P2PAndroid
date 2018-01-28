package com.walletone.p2pui.paydeal;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.walletone.p2pui.R;
import com.walletone.p2pui.W1P2PToolbar;
import com.walletone.sdk.P2PCore;
import com.walletone.sdk.models.RequestBuilder;

import java.io.UnsupportedEncodingException;

/**
 * Created by anton on 13.09.2017.
 */

public class PayDealActivity extends AppCompatActivity {

    public static final String ARG_AUTH_DATA = "PayDealActivity.ARG_AUTH_DATA";
    public static final String ARG_DEAL_ID = "PayDealActivity.ARG_DEAL_ID";

    public static final int RESULT_FAIL = RESULT_FIRST_USER + 1;

    private final String RETURN_HOST = "p2p-success-pay-deal";

    public static final int REQUEST_PAY_DEAL = 2;

    private boolean isVisible = false;
    private boolean needFinish = false;

    private WebView payDealWebView;

    private ProgressBar progressBar;

    private FrameLayout progressFrame;

    private Boolean redirectToPaymentToolAddition = false;

    private boolean finishEventDispatched = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_deal_activity_layout);

        W1P2PToolbar.installToolBar(this);

        payDealWebView = findViewById(R.id.pay_deal_web_view);
        progressFrame = findViewById(R.id.progressFrame);

        progressBar = findViewById(R.id.progress);
        progressBar.setMax(100);

        String authData = getIntent().getStringExtra(ARG_AUTH_DATA);
        String dealId = getIntent().getStringExtra(ARG_DEAL_ID);

        final RequestBuilder request = P2PCore.INSTANCE.dealsManager.payRequest(dealId, null, redirectToPaymentToolAddition, authData, "http://" + RETURN_HOST);

        try {
            String postData = request.getHttpBody();
            payDealWebView.setWebViewClient(new MyWebViewClient());
            setupWebView();
            payDealWebView.postUrl(request.getUrlString(), postData.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setupWebView() {
        final WebSettings wbs = payDealWebView.getSettings();
        wbs.setPluginState(WebSettings.PluginState.ON);
        wbs.setSaveFormData(true);
        wbs.setDomStorageEnabled(true);
        wbs.setJavaScriptEnabled(true);
        wbs.setBuiltInZoomControls(true);
        wbs.setAllowFileAccess(true);
        wbs.setSupportZoom(true);
        payDealWebView.setWebViewClient(new MyWebViewClient());
        payDealWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return true;
            }
        });

        payDealWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressFrame.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
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
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progressFrame.setVisibility(View.VISIBLE);
            return checkUrl(url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url == null) url = "";

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
        if (needFinish) {
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
        if (isVisible) {
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

