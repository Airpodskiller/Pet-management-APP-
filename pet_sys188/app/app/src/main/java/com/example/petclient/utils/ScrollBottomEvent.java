package com.example.petclient.utils;

import android.view.View;
import android.widget.AbsListView;

/**
 * 滑动到底部
 */
public class ScrollBottomEvent implements AbsListView.OnScrollListener {
    public ScrollBottomEvent(CallBack callBack){
        this.callBack = callBack;
    }
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        switch (i) {
            case SCROLL_STATE_IDLE:
                boolean flag = isListViewReachBottomEdge(absListView);
                if(flag){
                    //到底部
                    this.callBack.call();
                }
                break;
        }
    }
    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {}

    public boolean isListViewReachBottomEdge(final AbsListView listView) {
        boolean result = false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result = (listView.getHeight() >= bottomChildView.getBottom());
        };
        return result;
    }

    public CallBack callBack;
}
