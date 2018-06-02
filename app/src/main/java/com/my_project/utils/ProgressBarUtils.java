package com.my_project.utils;

import android.app.Activity;

import com.my_project.R;


/**
 * Created by nolan on 2017/9/29.
 */

public class ProgressBarUtils {

    private static ProgressDialog dialog;
    private static boolean isDestroy = false;
    private static Activity ctx;

    /**
     * 显示加载提示窗
     *
     * @param msg 提示文字
     */
    public static void showDialog(Activity ctxs ,CharSequence msg) {
        ctx = ctxs;
        showDialog(msg, false);
    }

    /**
     * 显示加载提示窗
     *
     * @param msg       提示文字
     * @param canCancel 是否可手动取消
     */
    protected static void showDialog(CharSequence msg, boolean canCancel) {
        if (isDestroy) {
            return;
        }
        if (dialog == null) {
            dialog = new ProgressDialog(ctx, R.style.Theme_ProgressDialog);
        }
        dialog.setCanceledOnTouchOutside(canCancel);
        dialog.setMessage(msg);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 关闭加载窗
     */
    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
