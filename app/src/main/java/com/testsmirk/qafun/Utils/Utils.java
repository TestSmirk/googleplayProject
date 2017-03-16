package com.testsmirk.qafun.Utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.testsmirk.qafun.R;

import java.util.UUID;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by dkc on 3/16/2017.
 */

public class Utils {
    public static View getEmptyView(Context context) {
        View emptyView = LayoutInflater.from(context).inflate(R.layout.layout_empty, null);
        return emptyView;

    }

    public static View getEmptyView(Context context, String msg) {
        View emptyView = LayoutInflater.from(context).inflate(R.layout.layout_empty, null);
        TextView messageView = ButterKnife.findById(emptyView, R.id.tv_main_empty);
        messageView.setText(msg);
        return emptyView;

    }

    public static void showKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static OkHttpClient.Builder client = new OkHttpClient.Builder();
    public static void runGet(HttpUrl.Builder urlBuiler, final okhttp3.Callback callback) {
//设置缓存

        final Request request = new Request.Builder()
                .url(urlBuiler.build())
                .build();

        final Call call = client.build().newCall(request);
        call.enqueue(callback);
    }
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static void runPost(String url, String json, okhttp3.Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.build().newCall(request).enqueue(callback);
    }


    public static String getUUID(Context context) {
        TelephonyManager teleManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String tmSerial = teleManager.getSimSerialNumber();
        String tmDeviceId = teleManager.getDeviceId();
        String androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        if (tmSerial == null) tmSerial = "1";
        if (tmDeviceId == null) tmDeviceId = "1";
        if (androidId == null) androidId = "1";
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDeviceId.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }


    public static Gson getGson() {

        return new GsonBuilder().registerTypeAdapter(String.class, new StringConverter()).serializeNulls().create();
    }

}
