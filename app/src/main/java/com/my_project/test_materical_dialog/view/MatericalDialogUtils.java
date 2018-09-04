package com.my_project.test_materical_dialog.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by com_c on 2018/6/4.
 */

public class MatericalDialogUtils {

    private  OnClickButtonListener onClickButtonListener;

    /**
     * 只显示一个按钮调用这个方法
     *
     * @param context
     * @param message
     * @param positiveText
     */
//    public static void showSingleButtonDialog(Context context, String message, String positiveText, OnButtonClickListener onButtonClickListener) {
//        showMatericalDialog(context, message, null, positiveText, onButtonClickListener);
//    }
//
//
//    public static void showNormalDialog(Context context, String message, final OnButtonClickListener onButtonClickListener) {
//        showMatericalDialog(context, message, "取消", "确定", onButtonClickListener);
//    }

//    public static void showMatericalDialog(Context context, String message, String negativeText, String positiveText, final OnButtonClickListener onButtonClickListener) {
//        showMatericalDialog(context, "提示", message, negativeText, positiveText, onButtonClickListener);
//    }
    public  void showMatericalDialog(Context context, String title, String message, String negativeText, String positiveText) {
        final AlertDialog.Builder mMaterialDialog = new AlertDialog.Builder(context);
        mMaterialDialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onClickButtonListener.clickPositiveButton();
                    }
                })
                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onClickButtonListener.clickNegativeButton();
                    }
                });
        mMaterialDialog.setCancelable(false);
        mMaterialDialog.show();
    }

    public  void setOnClickOnClickButtonListener(OnClickButtonListener onClickButtonListener) {
        this.onClickButtonListener = onClickButtonListener;
    }

    public interface OnClickButtonListener {
        void clickNegativeButton();

        void clickPositiveButton();
    }
}
