package com.leibown.library.widget.status;

import android.view.View;

import org.jetbrains.annotations.Nullable;

/**
 * Created by leibown on 2018/9/21.
 */

public class StatusViewContainer {

    private static final int STATUS_LOADING = 0;
    private static final int STATUS_EMPTY = 1;
    private static final int STATUS_ERROR = 2;

    private View statusView;

    private int status = STATUS_LOADING;


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


    public void showEmpty() {
        status = STATUS_EMPTY;
        if (statusView != null) {
            ((StatusView) statusView).showEmpty();
        }
    }

    public void showError() {
        status = STATUS_ERROR;
        if (statusView != null) {
            ((StatusView) statusView).showError();
        }
    }

    public void showLoading() {
        status = STATUS_LOADING;
        if (statusView != null) {
            ((StatusView) statusView).showLoading();
        }
    }

    public void setOnRetryListener(@Nullable final View.OnClickListener onClickListener) {
        if (getView() != null && onClickListener != null)
            getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status == STATUS_ERROR)
                        onClickListener.onClick(v);
                }
            });
    }

    public void setOnEmptyClickListener(@Nullable final View.OnClickListener onClickListener) {
        if (getView() != null && onClickListener != null)
            getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status == STATUS_EMPTY)
                        onClickListener.onClick(v);
                }
            });
    }

}
