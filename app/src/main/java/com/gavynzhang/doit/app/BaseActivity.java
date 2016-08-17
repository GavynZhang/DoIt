package com.gavynzhang.doit.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gavynzhang.doit.utils.LogUtils;

/**
 * Created by GavynZhang on 2016/8/16.
 */
public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("BaseActivity", getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    /**
     * @param id :view组件对应id
     * */
    public <T extends View> T $(int id) {
        return (T) findViewById(id);
    }
}
