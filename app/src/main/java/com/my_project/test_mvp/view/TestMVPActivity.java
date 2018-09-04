package com.my_project.test_mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.my_project.R;
import com.my_project.test_mvp.presenter.UserLoginPresenter;

/**
 * Created by com_c on 2018/8/22.
 */

public class TestMVPActivity extends AppCompatActivity implements View.OnClickListener, IUserLoginV {

    private EditText et_name, et_pwd;
    private Button btn_commit, btn_clean;
    private ProgressBar pBar;
    private UserLoginPresenter userLoginPresenter = new UserLoginPresenter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        bindID();
        initView();
    }

    private void initView() {
        btn_commit.setOnClickListener(this);
        btn_clean.setOnClickListener(this);
    }

    private void bindID() {
        et_name = findViewById(R.id.et_name);
        et_pwd = findViewById(R.id.et_pwd);
        btn_commit = findViewById(R.id.btn_commit);
        btn_clean = findViewById(R.id.btn_clean);
        pBar = findViewById(R.id.pBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                userLoginPresenter.login();
                break;
            case R.id.btn_clean:
                userLoginPresenter.clean();
                break;
        }
    }

    @Override
    public String getUserName() {
        return et_name.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return et_pwd.getText().toString().trim();
    }

    @Override
    public void clearUserName() {
        et_name.setText("");
    }

    @Override
    public void clearPassword() {
        et_pwd.setText("");
    }

    @Override
    public void showLoading() {
        pBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pBar.setVisibility(View.GONE);
    }

    @Override
    public void toNextActivity() {
        Toast.makeText(TestMVPActivity.this, "成功跳转", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFailedError() {
        Toast.makeText(TestMVPActivity.this, "账号密码有误", Toast.LENGTH_SHORT).show();
    }
}
