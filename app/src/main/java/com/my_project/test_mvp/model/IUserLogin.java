package com.my_project.test_mvp.model;

import com.my_project.test_mvp.model.OnLoginListener;

/**
 * Created by com_c on 2018/8/22.
 */

public interface IUserLogin {
     void login(String userName,String pwd,OnLoginListener onLoginListener);
}
