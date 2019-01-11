package com.my_project.test_face_pp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.my_project.MainActivity;
import com.my_project.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018\12\21 0021.
 */

public class SufaceViewActivity extends Activity implements SurfaceHolder.Callback {
    private Camera camera;
    private ImageView iv_bitmap;
    private TextView tv_timer;
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sufaceview);
        bindID();
        permission();
    }

    private void permission() {
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(SufaceViewActivity.this,
                    android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(SufaceViewActivity.this, new String[]{android.Manifest.permission.CAMERA}, 0);
            } else if (ContextCompat.checkSelfPermission(SufaceViewActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SufaceViewActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
    }

    private void bindID() {
        SurfaceView sfv_camera = findViewById(R.id.sfv_camera);
        iv_bitmap = findViewById(R.id.iv_bitmap);
        tv_timer = findViewById(R.id.tv_timer);
        SurfaceHolder surfaceHolder = sfv_camera.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 設置顯示器類型，setType必须设置

    }

    /**
     * 开启相机预览
     */
    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (camera == null) {
                        camera = Camera.open(1);
                    }
                    camera.setPreviewDisplay(holder);
                    camera.setDisplayOrientation(90);
                    camera.startPreview();
                    camera.autoFocus(autoFocusCallback);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private final Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            getPreViewImage();
        }
    };


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //主要在这里实现Camera的释放
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
            ;
        }
    }

    /**
     * 获取预览图片
     */
    private void getPreViewImage() {

        camera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
//                set(data);
                Camera.Size size = camera.getParameters().getPreviewSize();
                try {
                    YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                    if (image != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compressToJpeg(new Rect(0, 0, size.width, size.height),
                                100, stream);
                        Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());

                        //图片转换成本地文件
                        saveIamgerFile2location(stream.toByteArray());
                        // 因为图片会发生旋转，因此要对图片进行旋转到和手机在一个方向上
                        rotateMyBitmap(bmp);
                        // **********************************
                        stream.close();
                    }
                } catch (Exception ex) {
                    Log.e("Sys", "Error:" + ex.getMessage());
                }
            }
        });
    }

    private void saveIamgerFile2location(byte[] bytes) {
//        BitmapUtil.byte2File(bytes, BitmapUtil.path);
        BitmapUtil.byte2File(bytes, Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/test/222.jpg");
        if (new File(BitmapUtil.path).exists()) {
            Toast.makeText(SufaceViewActivity.this, "存储成功", Toast.LENGTH_SHORT).show();
            if (camera != null) {
                camera.setPreviewCallback(null);
            }
        }
    }

    private void set(byte[] data) {
        camera.setOneShotPreviewCallback(null);
        //处理data
        Camera.Size previewSize = camera.getParameters().getPreviewSize();//获取尺寸,格式转换的时候要用到
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                previewSize.width,
                previewSize.height,
                null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);// 80--JPG图片的质量[0-100],100最高
        byte[] rawImage = baos.toByteArray();
        //将rawImage转换成bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
        rotateMyBitmap(bitmap);
    }

    public void rotateMyBitmap(Bitmap bmp) { //*****旋转一下
        Matrix matrix = new Matrix();
        matrix.postRotate(270);
        Bitmap bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap nbmp2 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        //*******显示一下
        if (nbmp2 != null)
            iv_bitmap.setImageBitmap(nbmp2);
        return;
    }


    private static int convertYUVtoRGB(int y, int u, int v) {
        int r, g, b;
        r = y + (int) 1.402f * v;
        g = y - (int) (0.344f * u + 0.714f * v);
        b = y + (int) 1.772f * u;
        r = r > 255 ? 255 : r < 0 ? 0 : r;
        g = g > 255 ? 255 : g < 0 ? 0 : g;
        b = b > 255 ? 255 : b < 0 ? 0 : b;
        return 0xff000000 | (b << 16) | (g << 8) | r;
    }

}
