package com.gavynzhang.doit.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


/**
 * Created by GavynZhang on 2016/8/18.
 */
public class EventRemindTimePriDialogActivity extends BaseActivity implements View.OnClickListener{

    private static final int MODE_NORMAL = 0;
    private static final int MODE_TOMATO = 1;

    private int nowMode = MODE_NORMAL;

    private TextView dialog_title;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_event_remind_time_pri_dialog);

        //根据NewEventActivity设置nowMode
        Intent intent = getIntent();
        nowMode = intent.getIntExtra("event_mode",MODE_NORMAL);

        //初始化视图
        initViews();
        //根据nowMode设置文本
        setLevelText();
        //根据上次选择设置已选择项
        setTmpLevel();
        //设置监听
        setOnClickListener();
    }

    /**
     * 设置点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.level_1_text:

                level_1_text.setTextColor(getResources().getColor(R.color.mint_green));
                level_1_check_img.setVisibility(View.VISIBLE);

                saveAndReturnData(1);

                break;
            case R.id.level_2_text:

                level_2_text.setTextColor(getResources().getColor(R.color.mint_green));
                level_2_check_img.setVisibility(View.VISIBLE);

                saveAndReturnData(2);

                break;
            case R.id.level_3_text:

                level_3_text.setTextColor(getResources().getColor(R.color.mint_green));
                level_3_check_img.setVisibility(View.VISIBLE);

                saveAndReturnData(3);

                break;
            case R.id.level_4_text:

                level_4_text.setTextColor(getResources().getColor(R.color.mint_green));
                level_4_check_img.setVisibility(View.VISIBLE);

                saveAndReturnData(4);

                break;
            case R.id.level_5_text:

                level_5_text.setTextColor(getResources().getColor(R.color.mint_green));
                level_5_check_img.setVisibility(View.VISIBLE);

                saveAndReturnData(5);

                break;
            case R.id.level_6_text:

                level_6_text.setTextColor(getResources().getColor(R.color.mint_green));
                level_6_check_img.setVisibility(View.VISIBLE);

                saveAndReturnData(6);

                break;
            default:
                break;

        }
    }

    /**
     * 保存数据并返回数据给 NewEventActivity
     *
     * @param level 选择的数据标识
     */
    private void saveAndReturnData(int level){
        SharedPreferences.Editor editor = getSharedPreferences("level_tmp", MODE_PRIVATE).edit();
        if (nowMode == MODE_NORMAL){
            //存储选择等级数据
            editor.putInt("normal_mode_level", level);
        }else{
            editor.putInt("tomato_mode_level", level);
        }
        editor.apply();

        Intent intent = new Intent();
        intent.putExtra("event_level", level);
        setResult(RESULT_OK, intent);
        finish();
    }
    /**
     * 根据EVENT_MODE设置相关文字
     */
    private void setLevelText(){
        switch (nowMode){
            case MODE_NORMAL:

                break;
            case MODE_TOMATO:

                dialog_title.setText("选择优先级");

                level_1_text.setText("优先级 1");
                level_2_text.setText("优先级 2");
                level_3_text.setText("优先级 3");
                level_4_text.setText("优先级 4");
                level_5_text.setText("优先级 5");
                level_6_text.setText("优先级 6");


                break;
            default:
                break;
        }
    }

    /**
     * 设置保存的选择
     */
    private void setTmpLevel(){
        SharedPreferences pref = getSharedPreferences("level_tmp", MODE_PRIVATE);
        int tmpNormalLevel = pref.getInt("normal_mode_level", 1);
        int tmpTomatoLevel = pref.getInt("tomato_mode_level", 1);

        int tmp;
        if (nowMode == MODE_NORMAL){
            tmp = tmpNormalLevel;
        }else{
            tmp = tmpTomatoLevel;
        }

        switch (tmp){
            case 1:
                level_1_text.setTextColor(getResources().getColor(R.color.mint_green));
                level_1_check_img.setVisibility(View.VISIBLE);
                break;
            case 2:
                level_1_text.setTextColor(getResources().getColor(R.color.md_grey_800));
                level_1_check_img.setVisibility(View.GONE);

                level_2_text.setTextColor(getResources().getColor(R.color.mint_green));
                level_2_check_img.setVisibility(View.VISIBLE);
                break;
            case 3:
                level_1_text.setTextColor(getResources().getColor(R.color.md_grey_800));
                level_1_check_img.setVisibility(View.GONE);

                level_3_text.setTextColor(getResources().getColor(R.color.mint_green));
                level_3_check_img.setVisibility(View.VISIBLE);
                break;
            case 4:
                level_1_text.setTextColor(getResources().getColor(R.color.md_grey_800));
                level_1_check_img.setVisibility(View.GONE);

                level_4_text.setTextColor(getResources().getColor(R.color.mint_green));
                level_4_check_img.setVisibility(View.VISIBLE);
                break;
            case 5:
                level_1_text.setTextColor(getResources().getColor(R.color.md_grey_800));
                level_1_check_img.setVisibility(View.GONE);

                level_5_text.setTextColor(getResources().getColor(R.color.mint_green));
                level_5_check_img.setVisibility(View.VISIBLE);
                break;
            case 6:
                level_1_text.setTextColor(getResources().getColor(R.color.md_grey_800));
                level_1_check_img.setVisibility(View.GONE);

                level_6_text.setTextColor(getResources().getColor(R.color.mint_green));
                level_6_check_img.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }

    /**
     * 初始化控件
     */
    private void initViews(){

        dialog_title = $(R.id.dialog_title);

        level_1_text = $(R.id.level_1_text);
        level_2_text = $(R.id.level_2_text);
        level_3_text = $(R.id.level_3_text);
        level_4_text = $(R.id.level_4_text);
        level_5_text = $(R.id.level_5_text);
        level_6_text = $(R.id.level_6_text);

        level_1_check_img = $(R.id.level_1_check_img);
        level_2_check_img = $(R.id.level_2_check_img);
        level_3_check_img = $(R.id.level_3_check_img);
        level_4_check_img = $(R.id.level_4_check_img);
        level_5_check_img = $(R.id.level_5_check_img);
        level_6_check_img = $(R.id.level_6_check_img);
    }

    /**
     * 设置监听事件
     */
    private void setOnClickListener(){

        level_1_text.setOnClickListener(this);
        level_2_text.setOnClickListener(this);
        level_3_text.setOnClickListener(this);
        level_4_text.setOnClickListener(this);
        level_5_text.setOnClickListener(this);
        level_6_text.setOnClickListener(this);

    }
}
