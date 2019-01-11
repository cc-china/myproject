package com.my_project;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.my_project.fragment.DynamicFragment;
import com.my_project.fragment.HomePageFragment;
import com.my_project.fragment.MessageFragment;
import com.my_project.fragment.MyDataFragment;
import com.my_project.sqlitedatabase.SQLiteDB;
import com.my_project.test_bug.TempraryActivity;
import com.my_project.test_more_listview.DBModel;
import com.my_project.test_two_process.GuardianProcessService;
import com.my_project.test_two_process.LocalService;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017\11\13 0013.
 */

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.vp_fragment)
    ViewPager vp_fragment;
    @Bind(R.id.tl_tab)
    TabLayout tl_tab;
    @Bind(R.id.btn_open_local_service)
    Button btn_open_local_service;
    @Bind(R.id.btn_off_local_service)
    Button btn_off_local_service;
    @Bind(R.id.btn_off_remote_service)
    Button btn_off_remote_service;
    //每一个分类名称
    private String[] mTitles = new String[4];
    //对应的fragment集合
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    //每个分类的类对象
    private ArrayList mTabEntities = new ArrayList<>();

    //图标---用图片选择器
    private int[] mIconSelectorIds = {R.drawable.icon_home_page_selector
            , R.drawable.icon_home_page_selector
            , R.drawable.icon_home_page_selector
            , R.drawable.icon_home_page_selector};
    private SQLiteDB sqLiteDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestPermissions();
        initPageAdapter();
        testHandler();
        sqLiteDB = new SQLiteDB(this);
    }

    private void testHandler() {
        Message message = new Message();
        message.what = 1;
        message.arg1 = 1;
        handler.sendMessage(message);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

    private void initPageAdapter() {
//        vp_fragment.setNoScroll(true);

        //添加tab名称
        mTitles[0] = getString(R.string.tl_home_page);
        mTitles[1] = getString(R.string.tl_message);
        mTitles[2] = getString(R.string.tl_dynamic);
        mTitles[3] = getString(R.string.tl_my_data);
        //实例化fragment
        HomePageFragment homePageFragment = new HomePageFragment();
        MessageFragment messageFragment = new MessageFragment();
        DynamicFragment dynamicFragment = new DynamicFragment();
        MyDataFragment HomePageFragment = new MyDataFragment();
        mFragments.add(homePageFragment);
        mFragments.add(messageFragment);
        mFragments.add(dynamicFragment);
        mFragments.add(HomePageFragment);
        //添加tab选项卡
        for (int i = 0; i < mTitles.length; i++) {
            tl_tab.addTab(tl_tab.newTab());
        }
        MainPageAdapter adapter = new MainPageAdapter(getSupportFragmentManager(), this, mTitles, mFragments);
        vp_fragment.setAdapter(adapter);
        tl_tab.setupWithViewPager(vp_fragment);
        tl_tab.setTabsFromPagerAdapter(adapter);
        for (int i = 0; i < mTitles.length; i++) {
            TabLayout.Tab tab = tl_tab.getTabAt(i);
            View view = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            tvName.setText(mTitles[i]);
            ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            ivIcon.setImageResource(mIconSelectorIds[i]);
            //设置tab选项卡（自定义tabItem）
            tab.setCustomView(view);
        }

    }

    @OnClick({R.id.btn_insert_data,
            R.id.btn_open_local_service,
            R.id.btn_off_local_service,
            R.id.btn_off_remote_service})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_insert_data:
                //为数据库插入数据;
//                sqLiteDB.openDB();
//                insertData();
                startActivity(new Intent(MainActivity.this,TempraryActivity.class));
                break;
            case R.id.btn_open_local_service:
                startService(new Intent(MainActivity.this, LocalService.class));
                startService(new Intent(this
                        , GuardianProcessService.class));
                bindService(new Intent(this
                        , GuardianProcessService.class), connection2, BIND_AUTO_CREATE);
                break;
            case R.id.btn_off_local_service:
                stopService(new Intent(MainActivity.this, LocalService.class));
                break;
            case R.id.btn_off_remote_service:
                stopService(new Intent(MainActivity.this, GuardianProcessService.class));
                break;
        }
    }


    private ServiceConnection connection2 = new ServiceConnection() {
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
            Toast.makeText(MainActivity.this, "唤醒remote进程", 0).show();
            startService(new Intent(MainActivity.this
                    , GuardianProcessService.class));
            bindService(new Intent(MainActivity.this
                    , GuardianProcessService.class), connection2, BIND_AUTO_CREATE);
        }
    };


    private void insertData() {
        DBModel model = new DBModel();
        model.setId(18);
        model.setUserId(18);
        model.setUserName("小美");
        model.setPid(13);
        model.setpName("北京市国资委审计科区县局清查办公室");
        model.setType(0);//0 根节点   a 叶子节点
        model.setTreedepth(5);
        model.setPhone("18888888888");
        model.setOc("a-10");
        sqLiteDB.insert(model);
        sqLiteDB.destoryInstance();
    }

    private void requestPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.LOCATION_HARDWARE, Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS}, 0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Snackbar.make(getWindow().getDecorView(), "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                // TODO 清空数据
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
