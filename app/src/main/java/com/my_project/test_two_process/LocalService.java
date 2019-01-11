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

public class LocalService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("3333LocalService", "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("3333LocalService", "onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("3333LocalService", "onUnbind");
        return super.onUnbind(intent);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //正在连接
            IProcessAidl aidl = IProcessAidl.Stub.asInterface(service);
            try {
                String serviceName = aidl.getServiceName();
                Log.e("3333Local", serviceName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接，重新拉起服务
            Log.e("3333LocalService", "唤醒remote进程");
            Toast.makeText(LocalService.this, "唤醒remote进程", 0).show();
            OpenService();
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("3333LocalService", "启动守护进程");
        OpenService();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MyBinder myBinder = new MyBinder();
        return myBinder;
    }

    private void OpenService() {
        startService(new Intent(this
                , GuardianProcessService.class));
        bindService(new Intent(this
                , GuardianProcessService.class), connection, BIND_AUTO_CREATE);
    }

    private class MyBinder extends IProcessAidl.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return LocalService.class.getSimpleName() + "/////" + getProcessName();
        }
    }


    private String getProcessName() {
        int pid = android.os.Process.myPid();
        ActivityManager systemService = (ActivityManager) LocalService.this
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
