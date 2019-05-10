package com.my_project.test_aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.my_project.R;
import com.testaidl.IMyAidl;
import com.testaidl.model.Person;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018\11\5 0005.
 */

public class AidlActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        bindID();
        bindServices();


    }

    private void bindServices() {
        Intent intent1 = new Intent();
        intent1.setPackage("com.my_project");
        intent1.setAction("com.test_aidl.aidl");
        bindService(intent1, mConnection, BIND_AUTO_CREATE);
    }

    private void bindID() {
        final Button btn_test = findViewById(R.id.btn_test);
        final TextView tv_test = findViewById(R.id.tv_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                Person person = new Person("士兵：" + random.nextInt(100));
                try {
                    mAidl.addPerson(person);
                    List<Person> personList = mAidl.getPersonList();
                    tv_test.setText(personList.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private IMyAidl mAidl;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接后拿到 Binder，转换成 AIDL，在不同进程会返回个代理
            mAidl = IMyAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAidl = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
