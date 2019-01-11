package com.my_project.internal_obj;

import java.io.Serializable;

/**
 * Created by Administrator on 2018\10\22 0022.
 */

public class Outer implements Serializable{
    private String age;

    public  String setCode(String a) {
        Inner inner = new Inner();
        inner.setName();
        inner.name = "code";
        return age;
    }

    private class Inner {
        private String name;

        private void setName() {
            age = "19";
            setCode(age);
        }

    }
}
