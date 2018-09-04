package com.my_project.test_mvp.view;

/**
 * Created by com_c on 2018/8/22.
 */

public interface IUserLoginV {
    String getUserName();

    String getPassword();

    void clearUserName();

    void clearPassword();

    void showLoading();

    void hideLoading();

    void toNextActivity();

    void showFailedError();
}
