package com.my_project.test.activity;

import android.os.AsyncTask;

/**
 * Created by Administrator on 2018\12\10 0010.
 */

public class A_service extends AsyncTask {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }



    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    private void s(){
        new A_service().execute("");
    }
}
