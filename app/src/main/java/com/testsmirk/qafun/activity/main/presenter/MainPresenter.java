package com.testsmirk.qafun.activity.main.presenter;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.testsmirk.qafun.R;
import com.testsmirk.qafun.Utils.Contanst;
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
import butterknife.OnTouch;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by dkc on 3/16/2017.
 */

public class MainPresenter implements MainPresenterImpl {
    private static final String TAG = "MainPresenter";
    private final Activity activity;
    @BindView(R.id.recycler_view)
    RecyclerView recycleListView;
    @BindView(R.id.et_main_message)
    EditText et_main_message;
    private List<AnwserModle> list = new ArrayList<>();
    private MyAdapter adapter;

    public MainPresenter(Activity activity) {
        this.activity = activity;
    }

    @OnTouch(R.id.recycler_view)
    boolean keyboard(View view) {

        Log.i(TAG, "keyboard() called with: view = [" + view + "]");
        Utils.hideKeyBoard(view);
        return false;
    }

    @Override
    public void init() {
        ButterKnife.bind(this, activity);
        initView();
    }

    @OnClick(R.id.bt_main_send)
    void sendClick() {
        Log.i(TAG, "sendClick() called");
        String s = et_main_message.getText().toString();
        if (TextUtils.isEmpty(s)) {
            et_main_message.setError("please input message");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("info", s);
            jsonObject.put("appkey", "d02bc765efd840c263d790eb56077da6");
            jsonObject.put("userid", "123");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Utils.runPost(Contanst.URL, jsonObject.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], e = [" + e + "]");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
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
        recycleListView.setLayoutManager(new LinearLayoutManager(activity));
        if (adapter == null) {
            adapter = new MyAdapter(R.layout.layout_main, list);
            adapter.setEmptyView(Utils.getEmptyView(activity, "please send some message"));
        }
        recycleListView.setAdapter(adapter);
    }

    private class MyAdapter extends BaseQuickAdapter<AnwserModle, BaseViewHolder> {

        MyAdapter(int layoutResId, List<AnwserModle> data) {
            super(layoutResId, data);

        }

        @Override
        protected void convert(BaseViewHolder helper, AnwserModle item) {
            TextView view = helper.getView(R.id.tv_main_name);

            if (item == null || null == item.getResult()) {


            } else {

                if (item.isSend()){
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParams
//                    view.setLayoutParams();
                }
                helper.setText(R.id.tv_main_name, item.getResult().getText());

            }
        }
    }
}
