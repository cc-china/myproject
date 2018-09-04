package com.my_project.test_mvp.model;

/**
 * Created by com_c on 2018/8/22.
 */

public class UserLoginEvent implements IUserLogin {
    @Override
    public void login(final String userName, final String pwd, final OnLoginListener onLoginListener) {
        //开启子线程登录
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (userName.equals("123") && pwd.equals("123")) {
                    onLoginListener.loginSuccess(new User(userName,pwd));
                } else {
                    onLoginListener.loginFailed();
                }
            }
        }.start();
    }
}
