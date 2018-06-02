package com.my_project.test_rx_java.javabean;

/**
 * Created by com_c on 2018/3/27.
 */

public class Translation {

    private int status;
    private content content;


    class content {
        private String from;
        private String to;
        private String vendor;
        private String out;
        private int errNo;
    }

    //定义 输出返回数据 的方法
    public StringBuffer show() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Rxjava翻译结果：" + status + content.from + content.to + content.vendor + content.out + content.errNo);
        return buffer;
    }

}
