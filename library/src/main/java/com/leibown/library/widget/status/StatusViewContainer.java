package com.leibown.library.widget.status;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.jetbrains.annotations.Nullable;

/**
 * Created by leibown on 2018/9/21.
 */

public class StatusViewContainer {

    private static final int STATUS_LOADING = 0;
    private static final int STATUS_EMPTY = 1;
    private static final int STATUS_ERROR = 2;
    private static final int STATUS_CONTENT = 3;

    private View statusView;
    private View contentView;

    private int status = STATUS_LOADING;

    private RelativeLayout linearLayout;

    public StatusViewContainer(Context context) {
        linearLayout = new RelativeLayout(context);
        linearLayout.setBackgroundColor(Color.parseColor("#0000ff"));
    }

    public void setContentView(View view) {
        if (contentView != null)
            linearLayout.removeView(contentView);
        contentView = view;
        LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.addView(view, childParams);
    }

    public void setStatusView(View view) {
        if (statusView != null)
            linearLayout.removeView(statusView);

        if (view instanceof StatusView) {
            this.statusView = view;
            LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            linearLayout.addView(view, childParams);
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
        return linearLayout;
    }


    public void showEmpty() {
        status = STATUS_EMPTY;
        statusView.setVisibility(View.VISIBLE);
        ((StatusView) statusView).showEmpty();
        contentView.setVisibility(View.GONE);
    }

    public void showError() {
        status = STATUS_ERROR;
        statusView.setVisibility(View.VISIBLE);
        ((StatusView) statusView).showError();
        contentView.setVisibility(View.GONE);
    }

    public void showLoading() {
        status = STATUS_LOADING;
        statusView.setVisibility(View.VISIBLE);
        ((StatusView) statusView).showLoading();
        contentView.setVisibility(View.GONE);
    }

    public void showContent() {
        status = STATUS_CONTENT;
        statusView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
    }

    public void setOnRetryListener(@Nullable final View.OnClickListener onClickListener) {
        if (getView() != null && onClickListener != null)
            statusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status == STATUS_ERROR)
                        onClickListener.onClick(v);
                }
            });
    }

    public void setOnEmptyClickListener(@Nullable final View.OnClickListener onClickListener) {
        if (getView() != null && onClickListener != null)
            statusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status == STATUS_EMPTY)
                        onClickListener.onClick(v);
                }
            });
    }


    /**
     * 加载完成
     */
    public void loadComplete() {
        getStatusView().loadComplete();
    }


    /**
     * 没有更多数据
     */
    public void setNoMoreData() {
        getStatusView().setNoMoreData();
    }


    public void setLoadingText(String loadingText) {
        getStatusView().setLoadingText(loadingText);
    }

    public void setEmptyText(String emptyText) {
        getStatusView().setEmptyText(emptyText);
    }

    public void setEmptyImgRes(int imgRes) {
        getStatusView().setEmptyImgRes(imgRes);
    }


    public void setErrorImgRes(int imgRes) {
        getStatusView().setErrorImgRes(imgRes);
    }

    public void setLoadingImgRes(int imgRes) {
        getStatusView().setLoadingImgRes(imgRes);
    }

    public void setErrorText(String errorText) {
        getStatusView().setEmptyText(errorText);
    }

}
