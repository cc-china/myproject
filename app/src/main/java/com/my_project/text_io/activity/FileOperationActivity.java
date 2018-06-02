package com.my_project.text_io.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.my_project.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by com_c on 2018/5/9.
 * <p>
 * Java的IO流简介：
 * Java中的流按照---->
 * 流的方向分类：输入流和输出流
 * 处理数据单位分类：字节流（一次读入8位二进制）和字符流（一次读入16位二进制），
 * 实现功能分类：节点流和处理流
 * <p>
 * 字节流和字符流的原理是相同的，只不过处理的单位不同：
 * ①：后缀是stream是字节流，
 * ②：后缀是Reader、writer是字符流
 * 设备上的数据无论是图片还是dvd，文字，音视频，都是以二进制文件存储的，二进制文件最终都是以一个8位为数据单元进行提现的，
 * 所以计算机中最小数据单元就是字节，意味着字节流可以处理设备上的所有数据，所有字节流是可以处理字符数据的
 * 字节流读写operation：
 * File file = new File（D:/test.txt）；
 * 读：FileInputStream
 * InputStream is = new FileInputStream（file）  is.read( new byte[])--就是读出来的
 * 写：FileOutputStream
 * OutputStream os = new FileOutputStream（file） os.write（new String（“hello ，world”））
 * <p>
 * 字符流读写operation：
 * 读：FileReader
 * Reader read = new FileReader（file） read.read(new char[])
 * 写：FileWrite
 * Write  write = new FileWrite（file）  write.write（new String（“hello,world”））
 * <p>
 * 用缓冲流读写文件：其实这个也很简单，基本上就是加个Buffer  然后 将对应的FilexxStream传进去，再传一个缓冲区大小
 * 读字节：BufferInputStream
 * InputStream is = new BufferInputStream（new FileInputStream（file），size） is.read( new byte[size])--就是读出来的
 * 写字节：BufferOutputStream
 * OutputStream os = new BufferOutputStream（new FileOutputStream（file），size） os.write( new byte[size])--就是写进去的
 * 读字符：BufferReader
 * Reader read = new BufferReader（new FileReader（file），size） read.read(new char[size])
 * 写字符：
 * Write write = new BufferWrite（new FileWrite（file），size）  write.write(new String("hello，world"))
 * <p>
 * 字节流转换为字符流
 * 先读：FileInputStream
 * InputStream is = new FileInputStream（file）
 * 把字节流转换为字符流，就是把字符流和字节流组合的结果
 * Reader read = new InputStreamReader（is）
 * read.read(new byte[])
 */

public class FileOperationActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_operation);
    }

    public void operation() throws Exception {
        InputStream is = new BufferedInputStream(new FileInputStream(new File("")), 1024);
    }
}
