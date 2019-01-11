package com.my_project.test_aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.testaidl.IMyAidl;
import com.testaidl.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\10\31 0031.
 * 服务端 service
 */

public class MyAidlService extends Service {
    private final String TAG = this.getClass().getSimpleName();
    public ArrayList<Person> mPersons;
    /**
     * 创建生成的本地 Binder 对象，实现 AIDL 制定的方法
     */
    private IBinder mIBinder = new IMyAidl.Stub() {
        @Override
        public void addPerson(Person person) throws RemoteException {
            Log.e("11111111111",person.toString());
            mPersons.add(person);
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Person> getPersonList() throws RemoteException {
            return mPersons;
        }
    };

    /**
     * 客户端与服务端绑定时的回调，返回 mIBinder 后客户端就可以通过它远程调用服务端的方法，即实现了通讯
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mPersons = new ArrayList<>();
        Log.e(TAG, "MyAidlService onBind");
        return mIBinder;
    }

}
