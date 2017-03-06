package com.leibown.library.peimission;

/**
 * Created by Administrator on 2017/3/6.
 */

public interface PermissionListener {
    /**
     * 请求权限成功
     */
    void requestPermissionSuccess();

    /**
     * 请求权限失败
     *
     * @param deniedPermissions 未允许的权限
     */
    void requestPermissionFail(String[] deniedPermissions);
}
