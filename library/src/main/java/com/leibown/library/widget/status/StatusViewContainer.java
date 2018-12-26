package com.leibown.library.widget.status;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.jetbrains.annotations.Nullable;

/**
 * Created by leibown on 2018/9/21.
 */

public class StatusViewContainer {

    private View.OnClickListener retryListener;
    private View.OnClickListener emptyListener;

    private static final int STATUS_LOADING = 0;
    private static final int STATUS_EMPTY = 1;
    private static final int STATUS_ERROR = 2;
    private static final int STATUS_CONTENT = 3;

    protected DefaultStatusView statusView;
    protected View contentView;

    private int status = STATUS_LOADING;

    protected ViewGroup container;

    protected Context mContext;

    public StatusViewContainer(Context context) {
        mContext = context;
        container = initContainer();
        if (container == null)
            container = new FrameLayout(context);
    }

    public ViewGroup initContainer() {
        return null;
    }

    public void setContentView(View view) {
        if (contentView != null)
            container.removeView(contentView);
        contentView = view;
        FrameLayout.LayoutParams childParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(view, childParams);
    }

    public void setDefaultStatusView(DefaultStatusView view) {
        if (statusView != null)
            container.removeView(statusView);

        this.statusView = view;
        addViews(view);

        statusView.getClickableView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (status) {
                    case STATUS_EMPTY:
                        if (emptyListener != null)
                            emptyListener.onClick(v);
                        break;
                    case STATUS_ERROR:
                        if (retryListener != null)
                            retryListener.onClick(v);
                        break;
                }
            }
        });
    }

    private void addViews(View view) {
        FrameLayout.LayoutParams childParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(view, childParams);
    }

    /**
     * 加入新的状态view(此方法可以加入除默认状态view以外的其他状态),新的状态view加入后默认隐藏
     *
     * @param views
     */
    public void addOtherStatusViews(View... views) {
        for (int i = 0; i < views.length; i++) {
            FrameLayout.LayoutParams childParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            views[i].setVisibility(View.GONE);
            container.addView(views[i], childParams);
        }
    }


    /**
     * 获取默认状态view
     *
     * @return
     */
    public StatusView getStatusView() {
        if (statusView == null) {
            throw new IllegalArgumentException("plz call setDefaultStatusView() before this method");
        }

        return statusView;
    }

    //RootView
    private ViewGroup rootView;

    /**
     * 设置根部view（用于当你设置了container,且container不是根部view时使用）
     *
     * @param rootView
     */
    public void setRootView(ViewGroup rootView) {
        this.rootView = rootView;
    }

    public void removeAllViews() {
        if (rootView != null)
            rootView.removeAllViews();
        if (container != null)
            container.removeAllViews();
    }

    /**
     * 获取根部view，当根部view为空时放回container
     *
     * @return
     */
    public View getRootView() {
        if (rootView != null)
            return rootView;
        return container;
    }


    public void showEmpty() {
        hideAllViews();
        status = STATUS_EMPTY;
        statusView.setVisibility(View.VISIBLE);
        statusView.showEmpty();
    }

    public void showError() {
        hideAllViews();
        status = STATUS_ERROR;
        statusView.setVisibility(View.VISIBLE);
        statusView.showError();
    }

    public void showLoading() {
        hideAllViews();
        status = STATUS_LOADING;
        statusView.setVisibility(View.VISIBLE);
        statusView.showLoading();
    }

    public void showContent() {
        hideAllViews();
        status = STATUS_CONTENT;
        contentView.setVisibility(View.VISIBLE);
    }


    public void setOnRetryListener(@Nullable final View.OnClickListener onClickListener) {
        this.retryListener = onClickListener;
    }

    public void setOnEmptyClickListener(@Nullable final View.OnClickListener onClickListener) {
        this.emptyListener = onClickListener;
    }

    /**
     * 隐藏所有子view（可以用于切换状态时，隐藏其他statusView）
     */
    public void hideAllViews() {
        for (int i = 0; i < container.getChildCount(); i++) {
            container.getChildAt(i).setVisibility(View.GONE);
        }
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
        getStatusView().setErrorText(errorText);
    }

}
