package com.leibown.library.widget.status;

/**
 * Created by leibown on 2018/9/21.
 */

public interface StatusView {


    void setErrorText(String errorText);

    void setEmptyText(String emptyText);

    void setErrorImgRes(int errorImgRes);

    void setEmptyImgRes(int emptyImgRes);

    void setLoadingText(String loadingText);

    void setLoadingImgRes(int loadingImgRes);


    void showEmpty();

    void showError();

    void showLoading();

    void loadComplete();

    void setNoMoreData();

}
