package com.tencent.mtademo;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by foreachli on 2018/6/21.
 */

public class CommonUtils {

    /**
     * 判断签名是debug签名还是release签名
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
