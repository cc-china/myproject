package com.my_project.test_two_process;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.my_project.IProcessAidl;
import com.my_project.R;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018\11\15 0015.
 */

public class GuardianProcessService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("3333Guardian", "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("3333Guardian", "onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("3333Guardian", "onUnbind");
        return super.onUnbind(intent);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //正在连接
            IProcessAidl aidl = IProcessAidl.Stub.asInterface(service);
            try {
                String serviceName = aidl.getServiceName();
                Log.e("3333Guardian", serviceName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接，重新拉起服务
            Log.e("3333Guardian", "唤醒local进程");
            Toast.makeText(GuardianProcessService.this,"唤醒local进程",0).show();
            OpenService();
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("3333Guardian", "启动本地进程");
        bindService(new Intent(GuardianProcessService.this
                , LocalService.class), connection, BIND_AUTO_CREATE);
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MyBinder myBinder = new MyBinder();
        return myBinder;
    }

    private void OpenService() {
        startService(new Intent(GuardianProcessService.this
                , LocalService.class));
        bindService(new Intent(GuardianProcessService.this
                , LocalService.class), connection, BIND_AUTO_CREATE);
    }

    private class MyBinder extends IProcessAidl.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return GuardianProcessService.class.getSimpleName() + "/////" + getProcessName();
        }
    }

    private String getProcessName() {
        int pid = android.os.Process.myPid();
        ActivityManager systemService = (ActivityManager) GuardianProcessService.this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = systemService.getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = i.next();
            if (info.pid == pid) {
                return info.processName;
            }
        }
        return null;
    }

}
