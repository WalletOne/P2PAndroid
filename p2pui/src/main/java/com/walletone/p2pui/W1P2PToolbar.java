//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.walletone.p2pui;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class W1P2PToolbar {
    private static final String LOG_TAG = "W1P2PToolbar";

    public W1P2PToolbar() {
    }

    public static void installToolBar(AppCompatActivity activity) {
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null) {
            Log.d(LOG_TAG, "ActionBar available.");
        } else {
            Log.d(LOG_TAG, "No Actionbar detected. Installing Toolbar");
            View toolBarContainer = activity.findViewById(R.id.w1_p2p_toolbar_container);
            if(toolBarContainer == null) {
                Log.d(LOG_TAG, "Unable to find toolbar in Activity.");
            } else {
                toolBarContainer.setVisibility(View.VISIBLE);
                Toolbar toolbar = (Toolbar)activity.findViewById(R.id.w1_p2p_toolbar);
                activity.setSupportActionBar(toolbar);
                if(Build.VERSION.SDK_INT >= 21) {
                    activity.getSupportActionBar().setElevation(4f);
                } else {
                    Log.d(LOG_TAG, "Device pre-Lollipop. Enable Toolbar shadow workaround.");
                    View toolBarShadow = activity.findViewById(R.id.w1_p2p_toolbar_shadow);
                    if(toolBarShadow != null) {
                        toolBarShadow.setVisibility(View.VISIBLE);
                    } else {
                        Log.w(LOG_TAG, "Toolbar shadow is missing");
                    }
                }

            }
        }
    }
}
