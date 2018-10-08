package com.leibown.library.widget.status;

import android.view.View;

import org.jetbrains.annotations.Nullable;

/**
 * Created by leibown on 2018/9/21.
 */

public class StatusViewContainer {

    private View statusView;
    private boolean isShowError = false;


    public void setStatusView(View view) {

        if (view instanceof StatusView) {
            this.statusView = view;
        } else {
            throw new IllegalArgumentException("plz implements StatusView witch you enter");
        }
    }


    public StatusView getStatusView() {
        if (statusView == null) {
            throw new IllegalArgumentException("plz call setStatusView() before this method");
        }

        return (StatusView) statusView;
    }

    public View getView() {
        if (statusView == null) {
            throw new IllegalArgumentException("plz call setStatusView() before this method");
        }
        return statusView;
    }

    public boolean isShowError() {
        return isShowError;
    }


    public void showEmpty() {
        isShowError = false;
        if (statusView != null) {
            ((StatusView) statusView).showEmpty();
        }
    }

    public void showError() {
        isShowError = true;
        if (statusView != null) {
            ((StatusView) statusView).showError();
        }
    }

    public void showLoading() {
        isShowError = false;
        if (statusView != null) {
            ((StatusView) statusView).showLoading();
        }
    }

    public void setOnRetryListener(@Nullable final View.OnClickListener onClickListener) {
        if (getView() != null)
            getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShowError)
                        onClickListener.onClick(v);
                }
            });
    }
}
