package com.testsmirk.qafun.activity.main;

import android.os.Bundle;
import android.view.WindowManager;

import com.testsmirk.qafun.R;
import com.testsmirk.qafun.activity.main.base.BaseApp;
import com.testsmirk.qafun.activity.main.presenter.MainPresenter;

public class MainActivity extends BaseApp {

    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_main);
        if (mainPresenter == null) {
            mainPresenter = new MainPresenter(this);
            mainPresenter.init();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainPresenter.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDestroy();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mainPresenter.onRestart();

    }
}
