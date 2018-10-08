package com.leibown.demo;

import android.os.Bundle;
import android.view.View;

import com.leibown.library.MultifunctionalActivity;

/**
 * Created by Administrator on 2017/3/3.
 */

public abstract class BaseActivity extends MultifunctionalActivity {

    /**
     * 如果此方法返回false，那切换状态相关方法就会失效
     *
     * @return
     */
    @Override
    public boolean isNeedStatusView() {
        return true;
    }

    @Override
    public void bindViews(Bundle savedInstanceState) {
        //设置没有数据时的提示文字
        setEmptyText("暂时没有数据返回");

        //设置正在加载时数据时的提示文字
        setLoadingText("正在玩命加载中");

        //设置加载失败，重试时的提示文字
        setReTryText("加载失败请稍后再试");

        //设置各种状态时中间显示的图片
//        setStatusImageViewImageResource(R.drawable.android);

//        View statusView = View.inflate(this, R.layout.layout_status, null);
//        //设置各种状态时的View
//        setStatusView(statusView);


        View view = View.inflate(this, R.layout.layout_actionbar, null);
        //设置ActionBar，传入ActionBar布局
        setActionBar(view);
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示内容
                showContent();
            }
        });
    }
}
