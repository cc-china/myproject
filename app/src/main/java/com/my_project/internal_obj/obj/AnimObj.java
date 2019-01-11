package com.my_project.internal_obj.obj;

import android.util.Log;

/**
 * Created by Administrator on 2018\10\15 0015.
 */

public class AnimObj {

    private  String name = "anim";

    public AnimObj() {
        Log.e("11111111_Anim_","Anim");
    }

    public  String getName() {
        Log.e("11111111_Anim_getName","Anim_getName");
        return name;
    }

}
