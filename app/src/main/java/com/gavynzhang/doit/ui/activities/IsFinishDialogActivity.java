package com.gavynzhang.doit.ui.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;
import com.gavynzhang.doit.model.db.MyDatabaseHelper;
import com.gavynzhang.doit.model.entities.Event;

public class IsFinishDialogActivity extends BaseActivity {

    private TextView isFinishDialogTitle;
    private Button finishEvent;
    private Button unFinishEvent;

    private int tomatoNum;
    private Event mEvent;

    private MyDatabaseHelper dbHelper = MyDatabaseHelper.getMyDatabasesHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_is_finish_dialog);

        tomatoNum = getIntent().getIntExtra("tomatoNum", 0);
        mEvent = (Event)getIntent().getSerializableExtra("event");


        initViews();

        isFinishDialogTitle.setText("已过去"+tomatoNum+"个番茄钟，是否已完成任务？");

        finishEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存事件完成情况到数据库
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("isFinish", 1);
                db.update("Event", values, "id = ?", new String[]{String.valueOf(mEvent.getId())});
                //退出本Activity(MainActivity为SingleTask)
                Intent intent = new Intent(IsFinishDialogActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        unFinishEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IsFinishDialogActivity.this, EventDetailsActivity.class);
                intent.putExtra("eventFromIsFinishDialog", mEvent);
                startActivity(intent);
            }
        });
    }

    private void initViews(){
        isFinishDialogTitle = $(R.id.is_finish_dialog_title);
        finishEvent = $(R.id.finish_event_btn);
        unFinishEvent = $(R.id.unfinish_event_btn);
    }
}
