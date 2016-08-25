package com.gavynzhang.doit.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;

public class EventDetailsActivity extends BaseActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_event_details);

        initViews();

        mToolbar = $(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle("事件详情");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.md_grey_100));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void initViews(){
        mToolbar = $(R.id.toolbar);
    }
}
