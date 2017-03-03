package com.leibown.multifunctionalacitvity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.leibown.library.MultifunctionalActivity;

public class MainActivity extends MultifunctionalActivity {

    @Override
    public boolean isNeedStatusView() {
        return true;
    }

    @Override
    public int getResId() {
        return R.layout.activity_main;
    }

    @Override
    public void bindViews(Bundle savedInstanceState) {
//        setEmptyText("没得东西了，滚~");
//        showEmpty();
        View view = View.inflate(this, R.layout.layout_actionbar, null);
        setActionBar(view);
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContent();
            }
        });
    }

    @Override
    public void reTry() {
        Toast.makeText(this, "点击了重试", Toast.LENGTH_SHORT).show();
    }

    public void doClick(View view) {
        TextView tv = (TextView) view;
        setActionBarBackgroudColor(Color.parseColor(tv.getText().toString()));
    }

    public void doImgClick(View view) {
        switch (view.getId()) {
            case R.id.iv_1:
                setActionBarBackgroudResource(R.drawable.bg_nav);
                break;
            case R.id.iv_2:
                setActionBarBackgroudResource(R.drawable.a);
                break;
            case R.id.iv_3:
                setActionBarBackgroudResource(R.drawable.b);
                break;
        }
    }

    public void doBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                showLoading();
                break;
            case R.id.btn_2:
                showEmpty();
                break;
            case R.id.btn_3:
                showRetry();
                break;
        }
    }
}
