package com.bingo.base.handler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.bingo.base.utils.NetUtils;
import com.bingo.base.utils.PermissionUtils;
import com.bingo.base.utils.ToastUtils;

/**
 * Created by bingo on 2019/1/21.
 * Time:2019/1/21
 */

public class CommonHandler {
    /**
     * 处理网络的检测
     *
     * @param context
     * @param callback
     */
    public static void handleNet(Context context, Callback callback) {
        if (!NetUtils.isNetworkConnected(context)) {
            ToastUtils.show(context, "网络不可用！");
            return;
        }
        callback.proceed();
    }

    private static final int MIN_DELAY_TIME = 1000;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    /**
     * 处理快速点击时间
     *
     * @param callback
     */
    public static void handleFastClick(Callback callback) {
        if (isFastClick()) {
            return;
        }
        callback.proceed();
    }

    /**
     * 处理权限问题
     *
     * @param context
     * @param callback
     */
    public static void handlePermission(final Context context, final String[] permissions, final Callback callback) {
        if (!PermissionUtils.checkPermissions(context, permissions)) {
            new AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("为了应用可以正常使用，请您点击确认申请权限。")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            PermissionUtils.requestPermissionsResult((Activity) context, 1, permissions
                                    , new PermissionUtils.OnPermissionListener() {
                                        @Override
                                        public void onPermissionGranted() {
                                            //获得权限，执行原方法
                                            callback.proceed();
                                        }

                                        @Override
                                        public void onPermissionDenied() {
                                            PermissionUtils.showTipsDialog(context);
                                        }
                                    });
                        }
                    })
                    .create()
                    .show();
        } else {
            callback.proceed();
        }
    }

    /**
     * 处理权限问题
     *
     * @param context
     * @param callback
     */
    public static void handleAll(final Context context, final String[] permissions, final Callback callback) {
        handleNet(context, new Callback() {
            @Override
            public void proceed() {
                handleFastClick(new Callback() {
                    @Override
                    public void proceed() {
                        handlePermission(context, permissions, callback);
                    }
                });
            }
        });
    }

    public interface Callback {
        //继续执行
        void proceed();
    }
}
