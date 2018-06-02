package com.my_project.test_rx_java.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.my_project.R;
import com.my_project.test_rx_java.javabean.Translation;
import com.my_project.test_rx_java.retrofit.GetRequest_Interface;
import com.my_project.test_rx_java.retrofit.RetrofitUtils;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by com_c on 2018/3/22.
 */

public class RXJavaActivity extends Activity {
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.et_word)
    EditText etWord;
    @Bind(R.id.btn_ok)
    Button btnOk;
    private String TAG = "RXJavaActivity";
    private GetRequest_Interface retrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        ButterKnife.bind(this);
        retrofit = RetrofitUtils.createrApi();
        initView();
    }

    private void initData(String content) {
        retrofit.getCall(content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Translation>() {

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Translation translation) {
                        tvName.setText(translation.show().toString());
                    }
                });
    }

    private void initView() {
        //1、创建被观察者（Observable）对象
        /**
         *使用RXJava事件流链式调用
         **/
        Observable.interval(1, TimeUnit.SECONDS)
                .just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
            Disposable mDisposable;

            //通过复写对应方法来响应被观察者
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "开始采用subscribe链接");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer value) {
                if (value == 2) {
                    mDisposable.dispose();
                }
                Log.d(TAG, "对Next事件" + value + "作出响应");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                String content = etWord.getText().toString().trim();
                initData(content);
                break;
        }
    }
}
