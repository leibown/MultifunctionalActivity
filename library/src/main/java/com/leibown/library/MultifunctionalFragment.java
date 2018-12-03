package com.leibown.library;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.leibown.library.peimission.PermissionListener;
import com.leibown.library.peimission.PermissionManager;
import com.leibown.library.utils.DisplayUtil;
import com.leibown.library.widget.status.DefaultStatusView;
import com.leibown.library.widget.status.StatusViewContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */

public abstract class MultifunctionalFragment extends Fragment  {

    private LinearLayout mLlTittleBar;

    //用于装载Loading,retry等View的容器
    private StatusViewContainer mStatusContainer;
    //当前状态是否是重试状态
    private LinearLayout containerView;
    protected View statusBar;
    private View statusBarWhenActionbarHide;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        containerView = (LinearLayout) inflater.inflate(R.layout.fragment_multifunctional, null);

        //用来填充Android版本在4.4以上的状态栏
        mLlTittleBar = containerView.findViewById(R.id.ll_tittle_bar);

        View mContentView = inflater.inflate(getResId(), null);

        mStatusContainer = new StatusViewContainer(getContext());
        LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        containerView.addView(mStatusContainer.getView(), childParams);

        DefaultStatusView defaultStatusView = new DefaultStatusView(getContext());
        mStatusContainer.setStatusView(defaultStatusView);
        mStatusContainer.setContentView(mContentView);
        mStatusContainer.showContent();

        mStatusContainer.setOnRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reTry();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                View decorView = getActivity().getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                decorView.setSystemUiVisibility(option);
                getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            //如果Android版本大于4.4，说明状态栏就可以被透明，我们自己的布局就可以放到状态栏之下
            //我们把自定义的ActionBar的高度增高
            ViewGroup.LayoutParams params = mLlTittleBar.getLayoutParams();
            params.height = DisplayUtil.getBarHeight(getContext()) + DisplayUtil.dip2px(getContext(), 58);
            mLlTittleBar.requestLayout();

            ViewGroup.LayoutParams params2 = statusBar.getLayoutParams();
            params2.height = DisplayUtil.getBarHeight(getContext());
            statusBarWhenActionbarHide.setLayoutParams(params2);
            statusBar.requestLayout();
        } else
            statusBar.setVisibility(View.GONE);
        bindViews(savedInstanceState);
        return containerView;
    }

    public View getContentView() {
        return containerView;
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
     * 设置显示ActionBar
     */
    public void hideActionBar() {
        mLlTittleBar.setVisibility(View.GONE);
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
     * 这是状态栏背景颜色
     *
     * @param color
     */
    public void setStatusBarBackgroundColor(int color) {
        statusBarWhenActionbarHide.setBackgroundColor(color);
    }

    public void setLoadingText(String loadingText) {
        mStatusContainer.getStatusView().setLoadingText(loadingText);
    }

    public void setEmptyText(String emptyText) {
        mStatusContainer.getStatusView().setEmptyText(emptyText);
    }

    public void setEmptyImgRes(int res) {
        mStatusContainer.getStatusView().setEmptyImgRes(res);
    }

    public void setErrorImgRes(int imgRes) {
        mStatusContainer.getStatusView().setErrorImgRes(imgRes);
    }

    public void setLoadingImgRes(int imgRes) {
        mStatusContainer.getStatusView().setLoadingImgRes(imgRes);
    }

    public void setErrorText(String errorText) {
        mStatusContainer.getStatusView().setErrorText(errorText);
    }

    /**
     * 重新请求网络数据，如果首次加载就出现加载失败，界面显示加载失败，在点击“加载失败，请重试”时执行的方法
     */
    public void reTry() {
    }


    /**
     * 显示Loading状态
     */
    public void showLoading() {
        mStatusContainer.showLoading();
    }

    /**
     * 显示Empty状态
     */
    public void showEmpty() {
        mStatusContainer.showEmpty();
    }

    /**
     * 显示Retry状态
     */
    public void showRetry() {
        mStatusContainer.showError();
    }

    /**
     * 显示内容
     */
    public void showContent() {
        mStatusContainer.showContent();
    }

    public void loadComplete() {

    }

    public void setNoMoreData() {

    }


    private int PERMISSION_REQUEST_CODE = 2102;
    private PermissionListener mPermissionListener;

    /**
     * 检查是否缺少权限
     *
     * @param listener
     * @param permissions 选线字符串数组
     */
    protected void checkPermissions(PermissionListener listener, String... permissions) {
        this.mPermissionListener = listener;
        PermissionManager.checkPermission(getActivity(), permissions, PERMISSION_REQUEST_CODE, mPermissionListener);
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

    public void showStatusBar() {
        statusBarWhenActionbarHide.setVisibility(View.VISIBLE);
    }

    public void hideStatusBar() {
        statusBarWhenActionbarHide.setVisibility(View.GONE);
    }

    /**
     * 设置状态栏字体和图标为黑色
     * Android版本在6.0以上时可以调用此方法来改变状态栏字体图标颜色
     */
    protected void setStatusBarDarkMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 恢复状态栏字体和图标的颜色(可以理解为把状态栏字体图标重新设置为白色)
     */
    protected void restoreStatusBarMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public StatusViewContainer getStatusViewContainer() {
        return mStatusContainer;
    }
}
