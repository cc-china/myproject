package com.my_project.text_io;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.my_project.text_io.interfaces.IUpload;

/**
 * Created by com_c on 2018/5/9.
 */

public class A extends Activity implements IUpload{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new B(A.this);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,"哈哈哈哈",Toast.LENGTH_LONG).show();
    }

    @Override
    public void updata(int num) {
        Toast.makeText(A.this,num+"",Toast.LENGTH_SHORT).show();
    }
}
