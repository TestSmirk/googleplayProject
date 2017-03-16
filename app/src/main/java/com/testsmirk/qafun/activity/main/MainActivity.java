package com.testsmirk.qafun.activity.main;

import android.os.Bundle;

import com.testsmirk.qafun.R;
import com.testsmirk.qafun.activity.main.base.BaseApp;
import com.testsmirk.qafun.activity.main.presenter.MainPresenter;

public class MainActivity extends BaseApp {
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mainPresenter == null) {
            mainPresenter = new MainPresenter(this);
            mainPresenter.init();
        }
    }
}
