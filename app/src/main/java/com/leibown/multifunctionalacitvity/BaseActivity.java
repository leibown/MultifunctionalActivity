package com.leibown.multifunctionalacitvity;

import android.os.Bundle;
import android.view.View;

import com.leibown.library.MultifunctionalActivity;

/**
 * Created by Administrator on 2017/3/3.
 */

public abstract class BaseActivity extends MultifunctionalActivity {

    @Override
    public boolean isNeedStatusView() {
        return true;
    }

    @Override
    public void bindViews(Bundle savedInstanceState) {
        setEmptyText("暂时没有数据返回");
        setLoadingText("正在玩命加载中");
        setReTryText("加载失败请稍后再试");
        setStatusBarDarkMode();
        setStatusImageViewImageResource(R.drawable.android);

        View view = View.inflate(this, R.layout.layout_actionbar, null);
        setActionBar(view);
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContent();
            }
        });
    }
}
