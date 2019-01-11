package com.my_project.test_face_pp;

/**
 * Created by Administrator on 2018\12\21 0021.
 */


import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.megvii.facepp.api.FacePPApi;
//import com.megvii.facepp.api.IFacePPCallBack;
//import com.megvii.facepp.api.bean.CompareResponse;

import java.util.HashMap;
import java.util.Map;

public class FaceContrastUtils extends AppCompatActivity {
//
//    TextView txtResponse;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(setView());
//
////        FacePPApi faceppApi = new FacePPApi("syAe75QXfQHDt9YcmC8BJAJD0mX5nwqJ", "Q23rhNN6TsA8A6TcTOHkBsu-a7hBOUEB");
//        FacePPApi faceppApi = new FacePPApi("oTaH7pgtZIBn1F2w9jmOe_LUxI2FCZEf",
//                "dfEJD7_eQf-P613oqS78X_zbElita4Ru");
//        Map<String, String> params = new HashMap<>();
//        // params.put("return_attribute", "age,gender");
//
//        byte[] data1 = BitmapUtil.File2byte(Environment.getExternalStorageDirectory()
//                .getAbsolutePath() + "/test/6.jpg");
//        byte[] data2 = BitmapUtil.File2byte(Environment.getExternalStorageDirectory()
//                .getAbsolutePath() + "/test/8.jpg");
//        faceppApi.compare(params, data1, data2, new IFacePPCallBack<CompareResponse>() {
//            @Override
//            public void onSuccess(CompareResponse compareResponse) {
//                refreshView(compareResponse.toString());
//            }
//
//            @Override
//            public void onFailed(String error) {
//                refreshView(error);
//            }
//        });
//    }
//    private View setView() {
//        LinearLayout layout = new LinearLayout(this);
//        layout.setLayoutParams(new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        layout.setOrientation(LinearLayout.VERTICAL);
//        txtResponse = new TextView(this);
//        txtResponse.setText("");
//
//        layout.addView(txtResponse);
//        return layout;
//    }
//    private void refreshView(final String response) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                txtResponse.setText(response);
//            }
//        });
//    }
}
