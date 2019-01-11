package com.my_project.internal_obj.obj;

import android.util.Log;

/**
 * Created by Administrator on 2018\10\15 0015.
 */

public class DogObj extends AnimObj {
      String name = "dog";

    public DogObj() {
        Log.e("11111111_dog_","Dog");
    }

    public  String getName() {
        Log.e("11111111_dog_getName","Dog_getName");
        return name;
    }
}
