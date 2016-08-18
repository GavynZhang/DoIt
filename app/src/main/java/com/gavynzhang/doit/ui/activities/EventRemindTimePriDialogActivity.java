package com.gavynzhang.doit.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;

public class EventRemindTimePriDialogActivity extends BaseActivity {


    public static void actionStart(Context context){
        Intent intent = new Intent(context, EventRemindTimePriDialogActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_event_remind_time_pri_dialog);
    }
}
