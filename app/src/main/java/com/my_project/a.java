package com.my_project;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.my_project.test_nfc.activity.NfcActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by com_c on 2018/7/5.
 */

public class a extends Activity {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private TextView tv_value, tv_value2;
    private Tag tag;
    private static final int REQUEST_CODE_NFC_PERMISSION = 0x12345678;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_nfc_change_account);
        checkPermission();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //初始化PendingIntent
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0);

    }
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.NFC)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示")
                        .setMessage("需要Nfc权限才能正常使用此功能")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(a.this, new String[]{Manifest.permission.NFC}, REQUEST_CODE_NFC_PERMISSION);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(a.this, "授权取消", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create().show();

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.NFC}, REQUEST_CODE_NFC_PERMISSION);
            }


        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        processTag(intent);
    }

    private void processTag(Intent intent) {
        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        readNfcContent();
        onNfcTouch();
    }

    /**
     * 当接受到可处理的NCF时回调该方法
     */
    protected  void onNfcTouch(){
        setContentView(R.layout.activity_test_nfc_change_account);
        tv_value = findViewById(R.id.tv_value);
        tv_value2 = findViewById(R.id.tv_value2);
        try {
            tv_value.setText(readNfcContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    /**
     * 读取nfc内容
     */
    protected final String readNfcContent() throws Exception {
        checkPermission();
        String resultMsg = "";
        Ndef ndef = Ndef.get(tag);
        if (ndef != null) {
            try {

                ndef.connect();

                NdefMessage ndefMessage = ndef.getNdefMessage();
                byte[] msg = ndefMessage.toByteArray();
                byte[] realMsg = new byte[msg.length - 3];
                for (int i = 3; i < msg.length; i++) {
                    realMsg[i - 3] = msg[i];
                }
                if (realMsg.length != 0) {
                    resultMsg = new String(realMsg, Charset.forName("UTF-8"));
                } else {
                    resultMsg = "";
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("read nfc content failed");
            } finally {
                try {
                    ndef.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultMsg;
    }

    /**
     * 向NFC中写入内容
     *
     * @param msg 需要写入的信息
     * @return 是否写入成功
     * @throws Exception 写入异常
     */
    protected final boolean writeNfc(String msg) throws Exception {
        checkPermission();
        boolean flag = false;
        try {
            if (tag != null) {
                //新建NdefRecord数组，本例中数组只有一个元素
                NdefRecord[] records = {createRecord(msg)};
                //新建一个NdefMessage实例
                NdefMessage message = new NdefMessage(records);
                // 解析TAG获取到NDEF实例
                Ndef ndef = Ndef.get(tag);
                // 打开连接
                ndef.connect();
                // 写入NDEF信息
                ndef.writeNdefMessage(message);
                // 关闭连接
                ndef.close();
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("write NFC ERROR");
        }
        return flag;
    }

    /**
     * 清空NFC卡片中的内容
     *
     * @return 是否清空成功
     * @throws Exception 清空时出现的异常
     */
    protected final boolean deleteNfc() throws Exception {
        checkPermission();
        boolean flag = false;
        try {
            if (tag != null) {
                //新建NdefRecord数组，本例中数组只有一个元素
                NdefRecord[] records = {new NdefRecord(NdefRecord.TNF_EMPTY, null, null, null)};
                //新建一个NdefMessage实例
                NdefMessage message = new NdefMessage(records);
                // 解析TAG获取到NDEF实例
                Ndef ndef = Ndef.get(tag);
                // 打开连接
                ndef.connect();
                // 写入NDEF信息
                ndef.writeNdefMessage(message);
                // 关闭连接
                ndef.close();
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * 用于创建NFC信息
     *
     * @param msg 需要包装的信息
     * @return 包装后的NdefRecord
     * @throws UnsupportedEncodingException
     */
    private NdefRecord createRecord(String msg) throws UnsupportedEncodingException {
        //组装字符串，准备好你要写入的信息
        //将字符串转换成字节数组
        byte[] textBytes = msg.getBytes("UTF8");
        //将字节数组封装到一个NdefRecord实例中去
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                null, null, textBytes);
        return textRecord;
    }

    //使当前窗口置顶，权限高于三重过滤
    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            //设置当前activity为栈顶
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //恢复栈
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    // 字符序列转换为16进制字符串
    private String bytesToHexString(byte[] src) {
        return bytesToHexString(src, true);
    }

    private String bytesToHexString(byte[] src, boolean isPrefix) {
        StringBuilder stringBuilder = new StringBuilder();
        if (isPrefix == true) {
            stringBuilder.append("0x");
        }
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.toUpperCase(Character.forDigit(
                    (src[i] >>> 4) & 0x0F, 16));
            buffer[1] = Character.toUpperCase(Character.forDigit(src[i] & 0x0F,
                    16));
//            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取NFC标签ID
     *
     * @return 标签的ID，可能返回空
     */
    protected final String getTagId() {
        if (null == tag) {
            return "";
        }
        checkPermission();
        return bytesToHexString(tag.getId());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_NFC_PERMISSION) {
            if (grantResults.length > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "需要Nfc权限才能正常使用此功能", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "需要Nfc权限才能正常使用此功能", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
