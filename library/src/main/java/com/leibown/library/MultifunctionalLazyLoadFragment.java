package com.leibown.library;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
import com.leibown.library.widget.status.StatusController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */

public abstract class MultifunctionalLazyLoadFragment extends Fragment implements StatusController {

    private View mContentView;
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

        mContentView = inflater.inflate(getResId(), null);
        containerView.addView(mContentView);
        LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        mContentView.setLayoutParams(childParams);
        statusBar = containerView.findViewById(R.id.status_bar);
        statusBarWhenActionbarHide = containerView.findViewById(R.id.status_bar_when_actionbar_hide);
        if (isNeedStatusView()) {
            mStatusContainer = new StatusViewContainer();
            DefaultStatusView defaultStatusView = new DefaultStatusView(getContext());
            mStatusContainer.setStatusView(defaultStatusView);
            containerView.addView(mStatusContainer.getView());
            mStatusContainer.getView().setLayoutParams(childParams);
            mStatusContainer.getView().setVisibility(View.GONE);
            mStatusContainer.setOnRetryListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reTry();
                }
            });
        }

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

    @Override
    public void setLoadingText(String loadingText) {
        mStatusContainer.getStatusView().setLoadingText(loadingText);
    }

    @Override
    public void setEmptyText(String emptyText) {
        mStatusContainer.getStatusView().setEmptyText(emptyText);
    }

    @Override
    public void setEmptyImgRes(int res) {
        mStatusContainer.getStatusView().setEmptyImgRes(res);
    }

    @Override
    public void setErrorImgRes(int imgRes) {
        mStatusContainer.getStatusView().setErrorImgRes(imgRes);
    }

    @Override
    public void setLoadingImgRes(int imgRes) {
        mStatusContainer.getStatusView().setLoadingImgRes(imgRes);
    }

    @Override
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
    @Override
    public void showLoading() {
        if (isNeedStatusView()) {
            mContentView.setVisibility(View.GONE);
            mStatusContainer.getView().setVisibility(View.VISIBLE);
            mStatusContainer.showLoading();
        }
    }

    /**
     * 显示Empty状态
     */
    @Override
    public void showEmpty() {
        if (isNeedStatusView()) {
            mContentView.setVisibility(View.GONE);
            mStatusContainer.getView().setVisibility(View.VISIBLE);
            mStatusContainer.showEmpty();
        }
    }

    /**
     * 显示Retry状态
     */
    @Override
    public void showRetry() {
        if (isNeedStatusView()) {
            mContentView.setVisibility(View.GONE);
            mStatusContainer.getView().setVisibility(View.VISIBLE);
            mStatusContainer.showError();
        }
    }

    /**
     * 显示内容
     */
    @Override
    public void showContent() {
        if (isNeedStatusView()) {
            mContentView.setVisibility(View.VISIBLE);
            mStatusContainer.getView().setVisibility(View.GONE);
        }
    }

    @Override
    public void loadComplete() {

    }

    @Override
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


    private boolean mIsFirstVisible = true;

    private boolean isViewCreated = false;

    private boolean currentVisibleState = false;


    /**
     * @author wangshijia
     * @date 2018/2/2
     * Fragment 第一次可见状态应该在哪里通知用户 在 onResume 以后？
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 对于默认 tab 和 间隔 checked tab 需要等到 isViewCreated = true 后才可以通过此通知用户可见
        // 这种情况下第一次可见不是在这里通知 因为 isViewCreated = false 成立,等从别的界面回到这里后会使用 onFragmentResume 通知可见
        // 对于非默认 tab mIsFirstVisible = true 会一直保持到选择则这个 tab 的时候，因为在 onActivityCreated 会返回 false
        if (isViewCreated) {
            if (isVisibleToUser && !currentVisibleState) {
                dispatchUserVisibleHint(true);
            } else if (!isVisibleToUser && currentVisibleState) {
                dispatchUserVisibleHint(false);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isViewCreated = true;
        // !isHidden() 默认为 true  在调用 hide show 的时候可以使用
        if (!isHidden() && getUserVisibleHint()) {
            // 这里的限制只能限制 A - > B 两层嵌套
            dispatchUserVisibleHint(true);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        LogUtils.e(getClass().getSimpleName() + "  onHiddenChanged dispatchChildVisibleState  hidden " + hidden);

        if (hidden) {
            dispatchUserVisibleHint(false);
        } else {
            dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirstVisible) {
            if (!isHidden() && !currentVisibleState && getUserVisibleHint()) {
                dispatchUserVisibleHint(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 当前 Fragment 包含子 Fragment 的时候 dispatchUserVisibleHint 内部本身就会通知子 Fragment 不可见
        // 子 fragment 走到这里的时候自身又会调用一遍 ？
        if (currentVisibleState && getUserVisibleHint()) {
            dispatchUserVisibleHint(false);
        }
    }

    private boolean isFragmentVisible(Fragment fragment) {
        return !fragment.isHidden() && fragment.getUserVisibleHint();
    }


    /**
     * 统一处理 显示隐藏
     *
     * @param visible
     */
    private void dispatchUserVisibleHint(boolean visible) {
        //当前 Fragment 是 child 时候 作为缓存 Fragment 的子 fragment getUserVisibleHint = true
        //但当父 fragment 不可见所以 currentVisibleState = false 直接 return 掉
//        LogUtils.e(getClass().getSimpleName() + "  dispatchUserVisibleHint isParentInvisible() " + isParentInvisible());
        // 这里限制则可以限制多层嵌套的时候子 Fragment 的分发
        if (visible && isParentInvisible()) return;
//
//        //此处是对子 Fragment 不可见的限制，因为 子 Fragment 先于父 Fragment回调本方法 currentVisibleState 置位 false
//        // 当父 dispatchChildVisibleState 的时候第二次回调本方法 visible = false 所以此处 visible 将直接返回
        if (currentVisibleState == visible) {
            return;
        }

        currentVisibleState = visible;

        if (visible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                onFragmentFirstVisible();
            }
            onFragmentResume();
            dispatchChildVisibleState(true);
        } else {
            dispatchChildVisibleState(false);
            onFragmentPause();
        }
    }

    /**
     * 用于分发可见时间的时候父获取 fragment 是否隐藏
     *
     * @return true fragment 不可见， false 父 fragment 可见
     */
    private boolean isParentInvisible() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof MultifunctionalLazyLoadFragment) {
            MultifunctionalLazyLoadFragment fragment = (MultifunctionalLazyLoadFragment) parentFragment;
            return !fragment.isSupportVisible();
        } else {
            return false;
        }
    }

    private boolean isSupportVisible() {
        return currentVisibleState;
    }

    /**
     * 当前 Fragment 是 child 时候 作为缓存 Fragment 的子 fragment 的唯一或者嵌套 VP 的第一 fragment 时 getUserVisibleHint = true
     * 但是由于父 Fragment 还进入可见状态所以自身也是不可见的， 这个方法可以存在是因为庆幸的是 父 fragment 的生命周期回调总是先于子 Fragment
     * 所以在父 fragment 设置完成当前不可见状态后，需要通知子 Fragment 我不可见，你也不可见，
     * <p>
     * 因为 dispatchUserVisibleHint 中判断了 isParentInvisible 所以当 子 fragment 走到了 onActivityCreated 的时候直接 return 掉了
     * <p>
     * 当真正的外部 Fragment 可见的时候，走 setVisibleHint (VP 中)或者 onActivityCreated (hide show) 的时候
     * 从对应的生命周期入口调用 dispatchChildVisibleState 通知子 Fragment 可见状态
     *
     * @param visible
     */
    private void dispatchChildVisibleState(boolean visible) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        List<Fragment> fragments = childFragmentManager.getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment child : fragments) {
                if (child instanceof MultifunctionalLazyLoadFragment && !child.isHidden() && child.getUserVisibleHint()) {
                    ((MultifunctionalLazyLoadFragment) child).dispatchUserVisibleHint(visible);
                }
            }
        }
    }

    public void onFragmentFirstVisible() {
        Log.e("leibown", getClassSimpleName() + "  对用户第一次可见");

    }

    public void onFragmentResume() {
        Log.e("leibown", getClassSimpleName() + "  对用户可见");
    }

    public void onFragmentPause() {
        Log.e("leibown", getClassSimpleName() + "  对用户不可见");
    }


    protected String getClassSimpleName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        mIsFirstVisible = true;
    }


    public StatusViewContainer getStatusViewContainer() {
        return mStatusContainer;
    }
}
