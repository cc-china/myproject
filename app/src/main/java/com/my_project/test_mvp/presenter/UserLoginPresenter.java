package com.my_project.test_mvp.presenter;

import com.my_project.test_mvp.model.OnLoginListener;
import com.my_project.test_mvp.model.UserLoginEvent;
import com.my_project.test_mvp.model.User;
import com.my_project.test_mvp.view.IUserLoginV;

/**
 * Created by com_c on 2018/8/22.
 */

public class UserLoginPresenter {
    private UserLoginEvent userLoginEvent;
    private IUserLoginV userLoginView;

    public UserLoginPresenter(IUserLoginV iUserLoginV) {
        this.userLoginView = iUserLoginV;
        this.userLoginEvent = new UserLoginEvent();
    }

    private android.os.Handler mHandler = new android.os.Handler();

    public void login() {
        userLoginView.showLoading();
        userLoginEvent.login(userLoginView.getUserName(), userLoginView.getPassword(), new OnLoginListener() {
            @Override
            public void loginSuccess(User user) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.toNextActivity();
                        userLoginView.hideLoading();
                    }
                });
            }

            @Override
            public void loginFailed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.showFailedError();
                        userLoginView.hideLoading();
                    }
                });

            }
        });
    }

    public void clean() {
        userLoginView.clearUserName();
        userLoginView.clearPassword();
    }
}
