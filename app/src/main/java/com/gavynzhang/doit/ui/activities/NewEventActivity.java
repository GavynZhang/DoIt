package com.gavynzhang.doit.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;

public class NewEventActivity extends BaseActivity {

    private Toolbar mToolbar;


    /**
     * 用于启动本Activity
     *
     * @param context
     */
    public static void actionStart(Context context){
        Intent intent = new Intent(context, NewEventActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_event);

        mToolbar = $(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle("新事件");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.md_grey_100));
    }
}
