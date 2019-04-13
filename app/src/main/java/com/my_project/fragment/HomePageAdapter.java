package com.my_project.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.my_project.R;
import com.my_project.Snackbar;
import com.my_project.customer_app_upload.TestAPPUpload;
import com.my_project.test_aidl.AidlActivity;
import com.my_project.test_android_h5.Android2JavaScript;
import com.my_project.test_anim.TestAnimActivity;
import com.my_project.test_arithmetic.TestArithMeticActivity;
import com.my_project.test_custom_camera.SimpleActivity;
import com.my_project.test_custom_date_picker.activity.DatePickerActivity;
import com.my_project.test_customer_hashmap.activity.TestCustomerHashMapActivity;
import com.my_project.test_event_touch_intercapt.activity.TestDisPatchTouchEventActivity;
import com.my_project.test_face_pp.FaceppActivity;
import com.my_project.test_image_choose.activity.TestImageChooseActivity;
import com.my_project.test_jni.activity.MakeJNIActivity;
import com.my_project.test_list_reverse.activity.AloneListReverseActivity;
import com.my_project.test_more_listview.activity.TestMoreListActivity;
import com.my_project.test_more_thread.TestMoreThreadActivity;
import com.my_project.test_more_thread.UIThread2ChildThreadCommunicationActivity;
import com.my_project.test_mpandroid_chart.activity.MPAndroidChartActivity;
import com.my_project.test_mvp.view.TestMVPActivity;
import com.my_project.test_nfc.activity.NFCChangeAccountActivity;
import com.my_project.test_player.activity.TestPlayerActivity;
import com.my_project.test_progressbar.ui.TestProgressBar;
import com.my_project.test_pulish_video.activity.TestPulishVideoActivity;
import com.my_project.test_refreing_data.activity.TestRefershingDataActivity;
import com.my_project.test_rx_java.activity.RXJavaActivity;
import com.my_project.test_system_video.activity.TestVideoActivity;
import com.my_project.test_touch_event.activity.TestTouchEventActivity;
import com.my_project.test_view_custom.activity.CustomerTurnTableActivity;
import com.my_project.test_view_custom.activity.TestViewCustomActivity;
import com.my_project.text_io.activity.FileOperationActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.my_project.test_materical_dialog.acitivity.MatericalDialogActivity;

/**
 * Created by com_c on 2017/12/26.
 */

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {
    private Activity ctx;
    private List<String> mList;

     HomePageAdapter(Activity activity, List<String> list) {
        this.ctx = activity;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_recycler_home_page, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,  int p) {
        final int position = p;
        holder.tvName.setText(mList.get(position));
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mList.get(position)) {
                    case "测试recycleview刷新":
                        ctx.startActivity(new Intent(ctx, TestRefershingDataActivity.class));
                        break;
                    case "测试播放器":
                        ctx.startActivity(new Intent(ctx, TestPlayerActivity.class));
                        break;
                    case "测试进度条":
                        ctx.startActivity(new Intent(ctx, TestProgressBar.class));
                        break;
                    case "测试系统相册视频":
                        ctx.startActivity(new Intent(ctx, TestVideoActivity.class));
                        break;
                    case "监听控件的触摸事件":
                        ctx.startActivity(new Intent(ctx, TestTouchEventActivity.class));
                        break;
                    case "仿快手上传视频":
                        ctx.startActivity(new Intent(ctx, TestPulishVideoActivity.class));
                        break;
                    case "图片多选器":
                        ctx.startActivity(new Intent(ctx, TestImageChooseActivity.class));
                        break;
                    case "自定义View":
                        ctx.startActivity(new Intent(ctx, TestViewCustomActivity.class));
                        break;
                    case "多层级listview联动":
                        ctx.startActivity(new Intent(ctx, TestMoreListActivity.class));
                        break;
                    case "事件分发与拦截机制":
                        ctx.startActivity(new Intent(ctx, TestDisPatchTouchEventActivity.class));
                        break;
                    case "仿斗鱼滑动拼图验证码View":
                        ctx.startActivity(new Intent(ctx, TestViewCustomActivity.class));
                        break;
                    case "JNI调用":
                        ctx.startActivity(new Intent(ctx, MakeJNIActivity.class));
                        break;
                    case "RXJava使用":
                        ctx.startActivity(new Intent(ctx, RXJavaActivity.class));
                        break;
                    case "仿小米日历选择器":
                        ctx.startActivity(new Intent(ctx, DatePickerActivity.class));
                        break;
                    case "MPAndroidChart图表类使用":
                        ctx.startActivity(new Intent(ctx, MPAndroidChartActivity.class));
                        break;
                    case "JavaIO流操作":
                        ctx.startActivity(new Intent(ctx, FileOperationActivity.class));
                        break;
                    case "测试":
                        ctx.startActivity(new Intent(ctx, Snackbar.class));
                        break;
                    case "单链表反转":
                        ctx.startActivity(new Intent(ctx, AloneListReverseActivity.class));
                        break;
                    case "NFC读取写入":
                        ctx.startActivity(new Intent(ctx, NFCChangeAccountActivity.class));
                        break;
                    case "自定义相册拍照":
                        ctx.startActivity(new Intent(ctx, SimpleActivity.class));
                        break;
                    case "MatericalDialog":
                        ctx.startActivity(new Intent(ctx, MatericalDialogActivity.class));
                        break;
                    case "测试自定义更新":
                        ctx.startActivity(new Intent(ctx, TestAPPUpload.class));
                        break;
                    case "自定义HashMap简化实现":
                        ctx.startActivity(new Intent(ctx, TestCustomerHashMapActivity.class));
                        break;
                    case "MVP模型":
                        ctx.startActivity(new Intent(ctx, TestMVPActivity.class));
                        break;
                    case "Android多线程通信":
                        ctx.startActivity(new Intent(ctx, TestMoreThreadActivity.class));
                        break;
                    case "冒泡快排二分法查找等算法":
                        ctx.startActivity(new Intent(ctx, TestArithMeticActivity.class));
                        break;
                    case "Android动画":
                        ctx.startActivity(new Intent(ctx, TestAnimActivity.class));
                        break;
                    case "子线程创建Handler":
                        ctx.startActivity(new Intent(ctx, UIThread2ChildThreadCommunicationActivity.class));
                        break;
                    case "aidl服务端":
                        ctx.startActivity(new Intent(ctx, AidlActivity.class));
                        break;
                    case "H5页面与Android相互调用":
                        ctx.startActivity(new Intent(ctx, Android2JavaScript.class));
                        break;
                    case "人脸识别SDK":
                        ctx.startActivity(new Intent(ctx, FaceppActivity.class));
                        break;
                    case "抽奖转盘":
                        ctx.startActivity(new Intent(ctx, CustomerTurnTableActivity.class));
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        Button tvName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
