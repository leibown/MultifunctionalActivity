package com.leibown.multifunctionalacitvity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {


    @Override
    public int getResId() {
        return R.layout.activity_main;
    }

    @Override
    public void bindViews(Bundle savedInstanceState) {
        super.bindViews(savedInstanceState);
    }

    @Override
    public void reTry() {
        Toast.makeText(this, "点击了重试", Toast.LENGTH_SHORT).show();
    }

    public void doClick(View view) {
        Log.e("leibown", "点击了色块");
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
            case R.id.btn_4:
                setStatusBarDarkMode();
                break;
            case R.id.btn_5:
                restoreStatusBarMode();
                break;
        }
    }
}
