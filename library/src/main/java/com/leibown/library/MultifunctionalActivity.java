package com.leibown.library;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leibown.library.peimission.PermissionListener;
import com.leibown.library.peimission.PermissionManager;
import com.leibown.library.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class MultifunctionalActivity extends Activity {

    private View mContentView;
    private LinearLayout mLlTittleBar;


    private TextView statusTextView;


    //用于装载Loading,retry等View的容器
    private RelativeLayout mStatusContainer;
    //当前状态是否是重试状态
    private boolean isShowReTryView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout containerView = (LinearLayout) inflater.inflate(R.layout.activity_base, null);
        setContentView(containerView);

        //用来填充Android版本在4.4以上的状态栏
        View statusBar = findViewById(R.id.status_bar);
        mLlTittleBar = (LinearLayout) findViewById(R.id.ll_tittle_bar);

        mContentView = inflater.inflate(getResId(), null);
        containerView.addView(mContentView);
        LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        mContentView.setLayoutParams(childParams);

        if (isNeedStatusView()) {
            mStatusContainer = (RelativeLayout) inflater.inflate(R.layout.status_layout, null);
            containerView.addView(mStatusContainer);
            mStatusContainer.setLayoutParams(childParams);
            mStatusContainer.setVisibility(View.GONE);
            statusTextView = (TextView) mStatusContainer.findViewById(R.id.tv_status_content);
            mStatusContainer.findViewById(R.id.ll_status_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShowReTryView)
                        reTry();
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decorView = getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            //如果Android版本大于4.4，说明状态栏就可以被透明，我们自己的布局就可以放到状态栏之下
            //我们把自定义的ActionBar的高度增高
            ViewGroup.LayoutParams params = mLlTittleBar.getLayoutParams();
            params.height = DisplayUtil.getBarHeight(getApplicationContext()) + DisplayUtil.dip2px(getApplicationContext(), 58);
            mLlTittleBar.requestLayout();
            //把用于填充状态栏的View高度设置成跟状态栏一样的高度
            ViewGroup.LayoutParams params2 = statusBar.getLayoutParams();
            params2.height = DisplayUtil.getBarHeight(getApplicationContext());
            statusBar.requestLayout();
        }
        bindViews(savedInstanceState);
    }

    /**
     * 是否需要装载能显示各种状态的ViewGroup
     */
    public abstract boolean isNeedStatusView();

    /**
     * 获取Activity布局文件Id
     *
     * @return
     */
    public abstract int getResId();

    /**
     * 子类初始化view的方法
     *
     * @param savedInstanceState
     */
    public abstract void bindViews(Bundle savedInstanceState);

    /**
     * 设置ActionBar，传入ActionBar布局
     *
     * @param actionBar ActionBar布局
     */
    public void setActionBar(View actionBar) {
        mLlTittleBar.addView(actionBar);
    }

    /**
     * 设置显示ActionBar
     */
    public void showActionBar() {
        mLlTittleBar.setVisibility(View.VISIBLE);
    }

    /**
     * 设置ActionBar的背景颜色
     *
     * @param color 颜色值
     */
    public void setActionBarBackgroudColor(int color) {
        mLlTittleBar.setBackgroundColor(color);
    }

    /**
     * 设置ActionBar的背景图片
     *
     * @param imgRes 图片资源Id
     */
    public void setActionBarBackgroudResource(int imgRes) {
        mLlTittleBar.setBackgroundResource(imgRes);
    }

    /**
     * 重新请求网络数据，如果首次加载就出现加载失败，界面显示加载失败，在点击“加载失败，请重试”时执行的方法
     */
    public void reTry() {
    }


    /**
     * 自定义Loading,ReTry,Empty这种状态时显示的View
     *
     * @param view 能装各种状态的View
     */
    protected void setStatusView(View view) {
        if (isNeedStatusView()) {
            mStatusContainer.removeAllViews();
            mStatusContainer.addView(view);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            view.setLayoutParams(params);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShowReTryView)
                        reTry();
                }
            });
        }
    }

    /**
     * 自定义Loading,ReTry,Empty这种状态时显示的View
     *
     * @param view          能装各种状态的View
     * @param textViewResId 来标识各种状态的TextView
     */
    protected void setStatusView(View view, int textViewResId) {
        if (isNeedStatusView()) {
            setStatusView(view);
            statusTextView = (TextView) mStatusContainer.findViewById(textViewResId);
        }
    }

    /**
     * 获取loading,retry,empty状态下界面中的ImageView
     */
    protected ImageView getStatusImageView() {
        if (!isNeedStatusView()) {
            return null;
        }
        return (ImageView) mStatusContainer.findViewById(R.id.iv_status_img);
    }

    /**
     * 设置loading,retry,empty状态下界面中的ImageView显示的图片
     */
    protected void setStatusImageViewImageResource(int resId) {
        ((ImageView) mStatusContainer.findViewById(R.id.iv_status_img)).setImageResource(resId);
    }

    private String loadingText = "", emptyText = "", reTryText = "";

    protected void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }

    protected void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }

    protected void setReTryText(String reTryText) {
        this.reTryText = reTryText;
    }

    private void setStatusTextViewText(String statusContent) {
        if (statusTextView != null)
            statusTextView.setText(statusContent);
    }

    /**
     * 显示Loading状态
     */
    protected void showLoading() {
        if (isNeedStatusView()) {
            mContentView.setVisibility(View.GONE);
            mStatusContainer.setVisibility(View.VISIBLE);
            if ("".equals(loadingText)) {
                loadingText = getResources().getString(R.string.loading_content);
            }
            setStatusTextViewText(loadingText);
            isShowReTryView = false;
        }
    }

    /**
     * 显示Empty状态
     */
    protected void showEmpty() {
        if (isNeedStatusView()) {
            mContentView.setVisibility(View.GONE);
            mStatusContainer.setVisibility(View.VISIBLE);
            if ("".equals(emptyText)) {
                emptyText = getResources().getString(R.string.empty_content);
            }
            setStatusTextViewText(emptyText);
            isShowReTryView = false;
        }
    }

    /**
     * 显示Retry状态
     */
    protected void showRetry() {
        if (isNeedStatusView()) {
            mContentView.setVisibility(View.GONE);
            mStatusContainer.setVisibility(View.VISIBLE);
            if ("".equals(reTryText)) {
                reTryText = getResources().getString(R.string.retry_content);
            }
            setStatusTextViewText(reTryText);
            isShowReTryView = true;
        }
    }

    /**
     * 显示内容
     */
    protected void showContent() {
        if (isNeedStatusView()) {
            mContentView.setVisibility(View.VISIBLE);
            mStatusContainer.setVisibility(View.GONE);
            isShowReTryView = false;
        }
    }

    /**
     * 设置状态栏字体和图标为黑色
     * Android版本在6.0以上时可以调用此方法来改变状态栏字体图标颜色
     */
    protected void setStatusBarDarkMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 恢复状态栏字体和图标的颜色(可以理解为把状态栏字体图标重新设置为白色)
     */
    protected void restoreStatusBarMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }


    private int PERMISSION_REQUEST_CODE = 2102;
    private PermissionListener mPermissionListener;

    /**
     * 检查是否缺少权限
     *
     * @param listener
     * @param permissions        选线字符串数组
     */
    protected void checkPermissions(PermissionListener listener, String... permissions) {
        this.mPermissionListener = listener;
        PermissionManager.checkPermission(this, permissions, PERMISSION_REQUEST_CODE, mPermissionListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String[] deniedPermissions = hasAllPermissionsGranted(permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && deniedPermissions == null) {
            mPermissionListener.requestPermissionSuccess();
        } else {
            mPermissionListener.requestPermissionFail(deniedPermissions);
        }
    }

    /**
     * 是否所有权限已经获取到
     *
     * @param grantResults
     * @return
     */
    private String[] hasAllPermissionsGranted(@NonNull String[] permissions, @NonNull int[] grantResults) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                list.add(i);
            }
        }
        if (list.size() == 0)
            return null;
        String[] deniedPermissions = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            deniedPermissions[i] = permissions[list.get(i)];
        }
        return deniedPermissions;
    }
}
