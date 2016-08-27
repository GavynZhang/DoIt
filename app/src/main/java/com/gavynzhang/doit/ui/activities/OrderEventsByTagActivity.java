package com.gavynzhang.doit.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;
import com.gavynzhang.doit.model.entities.Event;
import com.gavynzhang.doit.ui.adapter.EventItemAdapter;
import com.gavynzhang.doit.utils.GetEvents;

import java.util.ArrayList;
import java.util.List;

public class OrderEventsByTagActivity extends BaseActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_events_by_tag);

        String tagName = getIntent().getStringExtra("tagName");

        initViews();

        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle(tagName);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.md_grey_100));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        List<Event> displayEvents = GetEvents.getEventsByTag(tagName);
        initData(displayEvents);


    }

    private void initViews(){
        mToolbar = $(R.id.toolbar);
        mRecyclerView = $(R.id.event_list_by_tag);
    }

    private void initData(List<Event> events){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(OrderEventsByTagActivity.this));
        mRecyclerView.setAdapter(new EventItemAdapter(events));
    }
}
