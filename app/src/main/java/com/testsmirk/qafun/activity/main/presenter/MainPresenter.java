package com.testsmirk.qafun.activity.main.presenter;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.testsmirk.qafun.R;
import com.testsmirk.qafun.Utils.Contanst;
import com.testsmirk.qafun.Utils.DensityUtil;
import com.testsmirk.qafun.Utils.Utils;
import com.testsmirk.qafun.activity.main.model.AnwserModle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

import static com.baidu.tts.client.SpeechSynthesizer.PARAM_SPEAKER;


/**
 * Created by dkc on 3/16/2017.
 */

public class MainPresenter implements MainPresenterImpl, SpeechSynthesizerListener {
    private static final String TAG = "MainPresenter";

    private static final int PRINT = 0;
    private static final int UI_CHANGE_INPUT_TEXT_SELECTION = 1;
    private static final int UI_CHANGE_SYNTHES_TEXT_SELECTION = 2;
    private final Activity activity;
    @BindView(R.id.recycler_view)
    RecyclerView recycleListView;
    @BindView(R.id.et_main_message)
    EditText et_main_message;
    private List<AnwserModle> list = new ArrayList<>();
    private MyAdapter adapter;
    private LinearLayoutManager layout;

    private Handler mHandler = new Handler() {

        /*
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case PRINT:
                    break;
                case UI_CHANGE_INPUT_TEXT_SELECTION:

                    break;
                case UI_CHANGE_SYNTHES_TEXT_SELECTION:

                    break;
                default:
                    break;
            }
        }

    };
    private SpeechSynthesizer speechSynthesizer;

    public MainPresenter(Activity activity) {
        this.activity = activity;
    }


    @Override
    public void init() {
        ButterKnife.bind(this, activity);
        speechSynthesizer = Utils.initialEnv(activity, this);
        initView();
        listener();
    }

    private void listener() {
    }

    @Override
    public void onRestart() {
    }

    @Override
    public void onDestroy() {
//        mSpeechSynthesizer.release();

    }

    @Override
    public void onResume() {
//        mSpeechSynthesizer.resume();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
//        mSpeechSynthesizer.stop();
    }


    private void addMsg2List(String str) {
        AnwserModle anwserModle = new AnwserModle();
        anwserModle.setSend(true);
        AnwserModle.ResultBean resultBean = new AnwserModle.ResultBean();
        resultBean.setText(str);
        anwserModle.setResult(resultBean);
        adapter.addData(anwserModle);
    }

    @OnClick(R.id.bt_main_send)
    void sendClick() {

        Log.i(TAG, "sendClick() called");
        String s = et_main_message.getText().toString();
        if (TextUtils.isEmpty(s)) {
            et_main_message.setError("please input message");
            return;
        }
        et_main_message.setText("");
        addMsg2List(s);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("info", s);
            jsonObject.put("key", "d02bc765efd840c263d790eb56077da6");
            jsonObject.put("userid", "123");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUrl.Builder builder = HttpUrl.parse(Contanst.URL).newBuilder();
        builder.addQueryParameter("info", s);
        builder.addQueryParameter("key", "d02bc765efd840c263d790eb56077da6");
        builder.addQueryParameter("userid", "userid");
        Utils.runGet(builder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], e = [" + e + "]");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                String string = response.body().string();
                Log.i(TAG, "onResponse() called with: call = [" + call + "], response = [" + string + "]");

                final AnwserModle anwserModle = Utils.getGson().fromJson(string, AnwserModle.class);
                recycleListView.post(new Runnable() {
                    @Override
                    public void run() {

                        if (anwserModle == null || null == anwserModle.getResult()) {

                            return;

                        }
                        adapter.addData(anwserModle);
                        recycleListView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                layout.scrollToPosition(adapter.getData().size());
                            }
                        }, 300);
                    }
                });
            }
        });


    }

    private String getLocation(Context context) {
        LocationManager systemService = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        return "";

    }

    private void initView() {
        layout = new LinearLayoutManager(activity);
        recycleListView.setLayoutManager(layout);
        if (adapter == null) {
            adapter = new MyAdapter(R.layout.layout_main, list);
            adapter.setEmptyView(Utils.getEmptyView(activity, "please send some message"));
        }
        recycleListView.setAdapter(adapter);
    }

    /*
       * @param arg0
       */
    @Override
    public void onSynthesizeStart(String utteranceId) {
    }

    /**
     * 合成数据和进度的回调接口，分多次回调
     *
     * @param utteranceId
     * @param data        合成的音频数据。该音频数据是采样率为16K，2字节精度，单声道的pcm数据。
     * @param progress    文本按字符划分的进度，比如:你好啊 进度是0-3
     */
    @Override
    public void onSynthesizeDataArrived(String utteranceId, byte[] data, int progress) {
        // toPrint("onSynthesizeDataArrived");
        mHandler.sendMessage(mHandler.obtainMessage(UI_CHANGE_SYNTHES_TEXT_SELECTION, progress, 0));
    }

    /**
     * 合成正常结束，每句合成正常结束都会回调，如果过程中出错，则回调onError，不再回调此接口
     *
     * @param utteranceId
     */
    @Override
    public void onSynthesizeFinish(String utteranceId) {
    }

    /**
     * 播放开始，每句播放开始都会回调
     *
     * @param utteranceId
     */
    @Override
    public void onSpeechStart(String utteranceId) {
    }

    /**
     * 播放进度回调接口，分多次回调
     *
     * @param utteranceId
     * @param progress    文本按字符划分的进度，比如:你好啊 进度是0-3
     */
    @Override
    public void onSpeechProgressChanged(String utteranceId, int progress) {
        // toPrint("onSpeechProgressChanged");
        mHandler.sendMessage(mHandler.obtainMessage(UI_CHANGE_INPUT_TEXT_SELECTION, progress, 0));
    }

    /**
     * 播放正常结束，每句播放正常结束都会回调，如果过程中出错，则回调onError,不再回调此接口
     *
     * @param utteranceId
     */
    @Override
    public void onSpeechFinish(String utteranceId) {
    }

    /**
     * 当合成或者播放过程中出错时回调此接口
     *
     * @param utteranceId
     * @param error       包含错误码和错误信息
     */
    @Override
    public void onError(String utteranceId, SpeechError error) {
        Log.i(TAG, "onError() called with: utteranceId = [" + utteranceId + "], error = [" + error + "]");
    }

    private class MyAdapter extends BaseQuickAdapter<AnwserModle, BaseViewHolder> {

        MyAdapter(int layoutResId, List<AnwserModle> data) {
            super(layoutResId, data);

        }

        @Override
        protected void convert(BaseViewHolder helper, final AnwserModle item) {
            TextView view = helper.getView(R.id.tv_main_name);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        if (item.isSend()) {
                            MainPresenter.this.speechSynthesizer.setParam(PARAM_SPEAKER, String.valueOf(2));
                            MainPresenter.this.speechSynthesizer.speak(item.getResult().getText());
                        } else {
                            MainPresenter.this.speechSynthesizer.setParam(PARAM_SPEAKER, String.valueOf(0));
                            MainPresenter.this.speechSynthesizer.speak(item.getResult().getText());
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "onLongClick: ", e);
                    }
                    return false;
                }
            });
            if (item == null || null == item.getResult()) {


            } else if (item.getError_code() == 0) {
                String text = item.getResult().getText();
                if (item.isSend()) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    int margin = DensityUtil.dip2px(activity, 10);
                    layoutParams.setMargins(margin, margin, margin, margin);
                    view.setLayoutParams(layoutParams);
                } else {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    int margin = DensityUtil.dip2px(activity, 10);
                    layoutParams.setMargins(margin, margin, margin, margin);
                    view.setLayoutParams(layoutParams);
                    MainPresenter.this.speechSynthesizer.speak(text);
                }
                helper.setText(R.id.tv_main_name, text);

            }
        }
    }
}
