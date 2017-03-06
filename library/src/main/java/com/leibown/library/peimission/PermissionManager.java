package com.leibown.library.peimission;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;

/**
 * Created by hulei on 2016/5/31.
 * 权限请求类
 */
public class PermissionManager {

    public static void checkPermission(Activity context, String[] permissions, int requestCode, PermissionListener permissionListener) {
        if (PermissionsChecker.lacksPermissions(context, permissions)) {
            /** permissions have not been granted. Requesting permissions.*/
            ActivityCompat.requestPermissions(context, permissions, requestCode);
        } else {
            /**permissions have been granted.**/
            permissionListener.requestPermissionSuccess();
        }
    }

}
