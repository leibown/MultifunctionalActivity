package com.leibown.library.widget.status;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leibown.library.R;


/**
 * Created by leibown on 2018/6/20.
 */

public class DefaultStatusView extends RelativeLayout implements StatusView {
    ImageView ivStatusImg;
    TextView tvStatusContent;
    LinearLayout llStatusView;
    TextView tvStatusBtn;

    private Context context;
    private String errorText;
    private int errorImgRes;
    private String emptyText;
    private int emptyImgRes;
    private String loadingText;
    private int loadingImgRes;

    public String getErrorText() {
        return errorText;
    }

    public DefaultStatusView(Context context) {
        this(context, null);
    }

    public DefaultStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {

        LayoutInflater.from(context).inflate(R.layout.default_status_layout, this, true);

        ivStatusImg = findViewById(R.id.iv_status_img);
        tvStatusContent = findViewById(R.id.tv_status_content);
        llStatusView = findViewById(R.id.ll_status_view);


        errorText = "加载失败，请点击重试";
        emptyText = "空空如也";
        loadingText = "玩命加载中...";
        emptyImgRes = R.drawable.status_empty;
        errorImgRes = R.drawable.status_error;
        loadingImgRes = R.drawable.loading;

    }

    @Override
    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    @Override
    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }

    @Override
    public void setErrorImgRes(int errorImgRes) {
        this.errorImgRes = errorImgRes;
    }

    @Override
    public void setEmptyImgRes(int emptyImgRes) {
        this.emptyImgRes = emptyImgRes;
    }

    @Override
    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }

    @Override
    public void setLoadingImgRes(int loadingImgRes) {
        this.loadingImgRes = loadingImgRes;
    }

    @Override
    public void showEmpty() {
        ivStatusImg.setImageResource(emptyImgRes);
        requestImageViewLayout();
        tvStatusContent.setText(emptyText);
    }

    @Override
    public void showError() {
        ivStatusImg.setImageResource(errorImgRes);
        requestImageViewLayout();
        tvStatusContent.setText(errorText);
    }

    @Override
    public void showLoading() {
        ivStatusImg.setImageResource(loadingImgRes);
        requestImageViewLayout();
        tvStatusContent.setText(loadingText);
    }

    @Override
    public void loadComplete() {

    }

    @Override
    public void setNoMoreData() {

    }

    private void requestImageViewLayout() {
        ViewGroup.LayoutParams layoutParams = ivStatusImg.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ivStatusImg.requestLayout();
    }

}
