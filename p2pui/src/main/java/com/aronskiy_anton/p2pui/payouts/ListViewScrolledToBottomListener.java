package com.aronskiy_anton.p2pui.payouts;

/**
 * Created by anton on 12.09.2017.
 */

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Listener for getting call when ListView gets scrolled to bottom
 */
public class ListViewScrolledToBottomListener implements AbsListView.OnScrollListener {

    ListViewScrolledToBottomCallback scrolledToBottomCallback;

    private int currentFirstVisibleItem;
    private int currentVisibleItemCount;
    private int totalItemCount;
    private int currentScrollState;

    public interface ListViewScrolledToBottomCallback {
        public void onScrolledToBottom();
    }

    public ListViewScrolledToBottomListener(Fragment fragment, ListView listView) {
        try {
            scrolledToBottomCallback = (ListViewScrolledToBottomCallback) fragment;
            listView.setOnScrollListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString()
                    + " must implement ListViewScrolledToBottomCallback");
        }
    }

    public ListViewScrolledToBottomListener(Activity activity, ListView listView) {
        try {
            scrolledToBottomCallback = (ListViewScrolledToBottomCallback) activity;
            listView.setOnScrollListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ListViewScrolledToBottomCallback");
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.currentScrollState = scrollState;
        if (isScrollCompleted()) {
            if (isScrolledToBottom()) {
                scrolledToBottomCallback.onScrolledToBottom();
            }
        }
    }

    private boolean isScrollCompleted() {
        if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isScrolledToBottom() {
        System.out.println("First:" + currentFirstVisibleItem);
        System.out.println("Current count:" + currentVisibleItemCount);
        System.out.println("Total count:" + totalItemCount);
        int lastItem = currentFirstVisibleItem + currentVisibleItemCount;
        if (lastItem == totalItemCount) {
            return true;
        } else {
            return false;
        }
    }
}