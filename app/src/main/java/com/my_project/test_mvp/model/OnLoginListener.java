package com.my_project.test_mvp.model;

import com.my_project.test_mvp.model.User;

/**
 * Created by com_c on 2018/8/22.
 */

public interface OnLoginListener {
    void loginSuccess(User user);
    void loginFailed();
}
