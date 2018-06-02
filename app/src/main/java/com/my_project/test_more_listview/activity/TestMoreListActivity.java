package com.my_project.test_more_listview.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.my_project.R;
import com.my_project.sqlitedatabase.SQLiteDB;
import com.my_project.test_more_listview.DBModel;
import com.my_project.test_more_listview.adapter.RecyclerAdapter;
import com.my_project.test_more_listview.interfaces.OnScrollToListener;
import com.my_project.test_more_listview.utils.WrapContentLinearLayoutManager;
import com.my_project.xunfly_utils.JsonParser;
import com.my_project.xunfly_utils.XunFlySetting;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017\11\17 0017.
 */
public class TestMoreListActivity extends AppCompatActivity {

    @Bind(R.id.auto_editview)
    TextView autoEditview;
    @Bind(R.id.iv_video)
    ImageView ivVideo;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private RecyclerView recyclerView;

    private RecyclerAdapter myAdapter;

    private LinearLayoutManager linearLayoutManager;
    private SQLiteDB sqLiteDB;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_more_list);
        ButterKnife.bind(this);
        setToolbar();
        sqLiteDB = new SQLiteDB(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recyclerView.getItemAnimator().setAddDuration(100);
        recyclerView.getItemAnimator().setRemoveDuration(100);
        recyclerView.getItemAnimator().setMoveDuration(200);
        recyclerView.getItemAnimator().setChangeDuration(100);

        myAdapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnScrollToListener(new OnScrollToListener() {

            @Override
            public void scrollTo(int position) {
                recyclerView.scrollToPosition(position);
            }
        });
        initDatas();
        initView();
    }

    private void setToolbar() {
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        /*
        * 初始化autoCompleteTextView
        * */
        ArrayList<String> str = new ArrayList<>();
        sqLiteDB.openDB();
        //查询具体某条数据
//        DBModel data = sqLiteDB.findOneData("北京市人民政府");
        List<DBModel> data = sqLiteDB.findAllData();
        for (int i = 0; i < data.size(); i++) {
            str.add(data.get(i).getUserName());
        }
        sqLiteDB.destoryInstance();
//        AutoEViewAdapter adapter = new AutoEViewAdapter(this, str);
//        autoEditview.setAdapter(adapter);

        //语音转换文字
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(TestMoreListActivity.this, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(TestMoreListActivity.this, mInitListener);

        mSharedPreferences = getSharedPreferences(XunFlySetting.PREFER_NAME,
                Activity.MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPhonetics();
            }
        });
    }

    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    int ret = 0; // 函数调用返回值

    private void showPhonetics() {
        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }
        // 开始听写
        // 如何判断一次听写结束：OnResult isLast=true 或者 onError
        // 移动数据分析，收集开始听写事件
        FlowerCollector.onEvent(TestMoreListActivity.this, "iat_recognize");

//        autoEditview.setText(null);// 清空显示内容
        mIatResults.clear();
        // 设置参数
        setParam();
        boolean isShowDialog = mSharedPreferences.getBoolean(
                "iat_show", true);
        if (isShowDialog) {
            // 显示听写对话框
            mIatDialog.setListener(mRecognizerDialogListener);
            mIatDialog.show();
            showTip(getString(R.string.text_begin));
        } else {
            // 不显示听写对话框
            ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                showTip("听写失败,错误码：" + ret);
            } else {
                showTip(getString(R.string.text_begin));
            }
        }
    }


    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        autoEditview.setText(resultBuffer.toString().replace("。", ""));
//        autoEditview.setSelection(autoEditview.length());
    }

    private boolean mTranslateEnable = false;
    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            if (mTranslateEnable) {
                printTransResult(results);
            } else {
                printResult(results);
            }

        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            if (mTranslateEnable && error.getErrorCode() == 14002) {
                showTip(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
            } else {
                showTip(error.getPlainDescription(true));
            }
        }

    };

    private void printTransResult(RecognizerResult results) {
        String trans = JsonParser.parseTransResult(results.getResultString(), "dst");
        String oris = JsonParser.parseTransResult(results.getResultString(), "src");

        if (TextUtils.isEmpty(trans) || TextUtils.isEmpty(oris)) {
            showTip("解析结果失败，请确认是否已开通翻译功能。");
        } else {
//                mResultText.setText( "原始语言:\n"+oris+"\n目标语言:\n"+trans );
        }

    }

    private String TAG = TestMoreListActivity.class.getSimpleName();
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        this.mTranslateEnable = mSharedPreferences.getBoolean(getString(R.string.pref_key_translate), false);
        if (mTranslateEnable) {
            Log.i(TAG, "translate enable");
            mIat.setParameter(SpeechConstant.ASR_SCH, "a");
            mIat.setParameter(SpeechConstant.ADD_CAP, "translate");
            mIat.setParameter(SpeechConstant.TRS_SRC, "its");
        }

        String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "en");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "cn");
            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "cn");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "en");
            }
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }


    private List<DBModel> listUser = new ArrayList<>();

    private void initDatas() {
        sqLiteDB.openDB();

        DBModel m1 = new DBModel();
        m1.setId(1);
        m1.setUserId(1);
        m1.setUserName("北京市人民政府");
        m1.setPid(0);
        m1.setpName("");
        m1.setType(1);//0 根节点   1 叶子节点
        m1.setTreedepth(1);
        m1.setPhone("18888888888");
        m1.setOc("1-10");
        sqLiteDB.insert(m1);

        DBModel m2 = new DBModel();
        m2.setId(2);
        m2.setUserId(2);
        m2.setUserName("天津市人民政府");
        m2.setPid(0);
        m2.setpName("");
        m2.setType(1);//0 根节点   1 叶子节点
        m2.setTreedepth(1);
        m2.setPhone("18888888888");
        m2.setOc("1-10");
        sqLiteDB.insert(m2);

        DBModel m3 = new DBModel();
        m3.setId(3);
        m3.setUserId(3);
        m3.setUserName("上海市人民政府");
        m3.setPid(0);
        m3.setpName("");
        m3.setType(1);//0 根节点   1 叶子节点
        m3.setTreedepth(1);
        m3.setPhone("18888888888");
        m3.setOc("1-10");
        sqLiteDB.insert(m3);

        DBModel m4 = new DBModel();
        m4.setId(4);
        m4.setUserId(2);
        m4.setUserName("陕西省人民政府");
        m4.setPid(0);
        m4.setpName("");
        m4.setType(1);//0 根节点   1 叶子节点
        m4.setTreedepth(1);
        m4.setPhone("18888888888");
        m4.setOc("1-10");
        sqLiteDB.insert(m4);

        DBModel m5 = new DBModel();
        m5.setId(5);
        m5.setUserId(5);
        m5.setUserName("商务部对外贸易办公室");
        m5.setPid(0);
        m5.setpName("");
        m5.setType(0);//0 根节点   1 叶子节点
        m5.setTreedepth(1);
        m5.setPhone("18888888888");
        m5.setOc("1-10");
        sqLiteDB.insert(m5);

        DBModel m6 = new DBModel();
        m6.setId(6);
        m6.setUserId(6);
        m6.setUserName("外交部新闻发布办公室");
        m6.setPid(0);
        m6.setpName("");
        m6.setType(0);//0 根节点   1 叶子节点
        m6.setTreedepth(1);
        m6.setPhone("18888888888");
        m6.setOc("1-10");
        sqLiteDB.insert(m6);

        DBModel m7 = new DBModel();
        m7.setId(7);
        m7.setUserId(7);
        m7.setUserName("北京市公安局党委领导小组");
        m7.setPid(1);
        m7.setpName("北京市人民政府");
        m7.setType(0);//0 根节点   1 叶子节点
        m7.setTreedepth(2);
        m7.setPhone("18888888888");
        m7.setOc("1-10");
        sqLiteDB.insert(m7);

        DBModel m8 = new DBModel();
        m8.setId(8);
        m8.setUserId(8);
        m8.setUserName("北京市国土资源局");
        m8.setPid(1);
        m8.setpName("北京市人民政府");
        m8.setType(1);//0 根节点   1 叶子节点
        m8.setTreedepth(2);
        m8.setPhone("18888888888");
        m8.setOc("1-10");
        sqLiteDB.insert(m8);

        DBModel m9 = new DBModel();
        m9.setId(9);
        m9.setUserId(9);
        m9.setUserName("北京市国土资源局财务科");
        m9.setPid(8);
        m9.setpName("北京市国土资源局");
        m9.setType(1);//0 根节点   1 叶子节点
        m9.setTreedepth(3);
        m9.setPhone("18888888888");
        m9.setOc("1-10");
        sqLiteDB.insert(m9);

        DBModel m10 = new DBModel();
        m10.setId(10);
        m10.setUserId(10);
        m10.setUserName("北京市国土资源局财务科区县级人事办");
        m10.setPid(9);
        m10.setpName("北京市国土资源局财务科");
        m10.setType(0);//0 根节点   1 叶子节点
        m10.setTreedepth(4);
        m10.setPhone("18888888888");
        m10.setOc("1-10");
        sqLiteDB.insert(m10);

        DBModel m11 = new DBModel();
        m11.setId(11);
        m11.setUserId(11);
        m11.setUserName("北京市国土资源局国有资产审计科");
        m11.setPid(8);
        m11.setpName("北京市国土资源局财务科");
        m11.setType(1);//0 根节点   1 叶子节点
        m11.setTreedepth(3);
        m11.setPhone("18888888888");
        m11.setOc("1-10");
        sqLiteDB.insert(m11);

        DBModel m12 = new DBModel();
        m12.setId(12);
        m12.setUserId(12);
        m12.setUserName("北京市国资委乡镇复审小组");
        m12.setPid(11);
        m12.setpName("北京市国土资源局国有资产审计科");
        m12.setType(1);//0 根节点   1 叶子节点
        m12.setTreedepth(4);
        m12.setPhone("18888888888");
        m12.setOc("1-10");
        sqLiteDB.insert(m12);

        DBModel m13 = new DBModel();
        m13.setId(13);
        m13.setUserId(13);
        m13.setUserName("北京市国资委审计科区县局清查办公室");
        m13.setPid(11);
        m13.setpName("北京市国土资源局国有资产审计科");
        m13.setType(0);//0 根节点   1 叶子节点
        m13.setTreedepth(4);
        m13.setPhone("18888888888");
        m13.setOc("1-10");
        sqLiteDB.insert(m13);

        DBModel m14 = new DBModel();
        m14.setId(14);
        m14.setUserId(14);
        m14.setUserName("小虎");
        m14.setPid(13);
        m14.setpName("北京市国资委审计科区县局清查办公室");
        m14.setType(0);//0 根节点   1 叶子节点
        m14.setTreedepth(5);
        m14.setPhone("18888888888");
        m14.setOc("1-10");
        sqLiteDB.insert(m14);

        DBModel m15 = new DBModel();
        m15.setId(15);
        m15.setUserId(15);
        m15.setUserName("小龙");
        m15.setPid(13);
        m15.setpName("北京市国资委审计科区县局清查办公室");
        m15.setType(0);//0 根节点   1 叶子节点
        m15.setTreedepth(5);
        m15.setPhone("18888888888");
        m15.setOc("1-10");
        sqLiteDB.insert(m15);

        DBModel m16 = new DBModel();
        m16.setId(16);
        m16.setUserId(16);
        m16.setUserName("小牛");
        m16.setPid(13);
        m16.setpName("北京市国资委审计科区县局清查办公室");
        m16.setType(0);//0 根节点   1 叶子节点
        m16.setTreedepth(5);
        m16.setPhone("18888888888");
        m16.setOc("1-10");
        sqLiteDB.insert(m16);

        listUser.clear();
        DBModel model = sqLiteDB.findFirstLevelTableData();
        //查询根节点
        List<DBModel> noChildrenList = sqLiteDB.findTableRootNode(model.getPid());
        //查询叶子节点
//        List<DBModel> hasChildrenList = sqLiteDB.findAllOrgBook(model.getTreedepth(), null);
        listUser.addAll(noChildrenList);
//        listUser.addAll(hasChildrenList);
        myAdapter.addAll(listUser, 0);
        sqLiteDB.destoryInstance();
    }


    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if (mTranslateEnable && error.getErrorCode() == 14002) {
                showTip(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
            } else {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            if (mTranslateEnable) {
                printTransResult(results);
            } else {
                printResult(results);
            }

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    @OnClick(R.id.fl_click)
    public void onClick() {
        startActivity(new Intent(TestMoreListActivity.this, SearchOrganizationActivity.class));

    }
}