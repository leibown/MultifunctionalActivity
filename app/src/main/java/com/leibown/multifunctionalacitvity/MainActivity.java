package com.leibown.multifunctionalacitvity;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.leibown.library.peimission.PermissionListener;

public class MainActivity extends BaseActivity {


    @Override
    public int getResId() {
        return R.layout.activity_main;
    }

    @Override
    public void bindViews(Bundle savedInstanceState) {
        super.bindViews(savedInstanceState);

        showActionBar();

        checkPermissions(new PermissionListener() {
            @Override
            public void requestPermissionSuccess() {

            }

            @Override
            public void requestPermissionFail(String[] deniedPermissions) {

            }
        }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CALENDAR);

    }

    @Override
    public void reTry() {
        Toast.makeText(this, "点击了重试", Toast.LENGTH_SHORT).show();
    }


    public void doClick(View view) {
        TextView tv = (TextView) view;
        //设置ActionBar的背景颜色
        setActionBarBackgroudColor(Color.parseColor(tv.getText().toString()));
    }


    public void doImgClick(View view) {
        int resId = 0;
        switch (view.getId()) {
            case R.id.iv_1:
                resId = R.drawable.bg_nav;
                break;
            case R.id.iv_2:
                resId = R.drawable.a;
                break;
            case R.id.iv_3:
                resId = R.drawable.b;
                break;
        }
        //设置ActionBar的背景图片
        setActionBarBackgroudResource(resId);
    }


    public void doBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                //显示加载中状态
                showLoading();
                break;
            case R.id.btn_2:
                //显示空数据状态
                showEmpty();
                break;
            case R.id.btn_3:
                //显示加载失败重试状态
                showRetry();
                break;
            case R.id.btn_4:
                //设置状态栏图标和文字为黑色
                setStatusBarDarkMode();
                break;
            case R.id.btn_5:
                //恢复状态栏图标和文字
                restoreStatusBarMode();
                break;
        }
    }
}
