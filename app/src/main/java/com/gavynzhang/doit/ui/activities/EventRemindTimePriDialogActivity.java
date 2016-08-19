package com.gavynzhang.doit.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;
import com.gavynzhang.doit.utils.LogUtils;

public class EventRemindTimePriDialogActivity extends BaseActivity implements View.OnClickListener{

    private static final int MODE_NORMAL = 0;
    private static final int MODE_TOMATO = 1;

    private TextView dialo_title;

    private TextView level_1_text;
    private TextView level_2_text;
    private TextView level_3_text;
    private TextView level_4_text;
    private TextView level_5_text;
    private TextView level_6_text;

    private ImageView level_1_check_img;
    private ImageView level_2_check_img;
    private ImageView level_3_check_img;
    private ImageView level_4_check_img;
    private ImageView level_5_check_img;
    private ImageView level_6_check_img;

    /**
     * 启动本Activity
     *
     * @param context
     */
    public static void actionStart(Context context){
        Intent intent = new Intent(context, EventRemindTimePriDialogActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_event_remind_time_pri_dialog);

        initViews();
        setOnClickListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.level_1_text:
                Toast.makeText(EventRemindTimePriDialogActivity.this, "ClickOk",Toast.LENGTH_SHORT).show();
                break;
            case R.id.level_2_text:
                break;
            case R.id.level_3_text:
                break;
            case R.id.level_4_text:
                break;
            case R.id.level_5_text:
                break;
            case R.id.level_6_text:
                break;
            default:
                break;

        }
    }

    /**
     * 初始化控件
     */
    private void initViews(){


        level_1_text = $(R.id.level_1_text);
        level_2_text = $(R.id.level_2_text);
        level_3_text = $(R.id.level_3_text);
        level_4_text = $(R.id.level_4_text);
        level_5_text = $(R.id.level_5_text);
        level_6_text = $(R.id.level_6_text);
    }

    private void setOnClickListener(){

        level_1_text.setOnClickListener(this);
        level_2_text.setOnClickListener(this);
        level_3_text.setOnClickListener(this);
        level_4_text.setOnClickListener(this);
        level_5_text.setOnClickListener(this);
        level_6_text.setOnClickListener(this);

    }
}
