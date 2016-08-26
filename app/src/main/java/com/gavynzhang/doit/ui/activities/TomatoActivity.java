package com.gavynzhang.doit.ui.activities;

import android.app.Service;
import android.content.Intent;
import android.content.res.ObbInfo;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;
import com.gavynzhang.doit.app.state.LoginContext;
import com.gavynzhang.doit.model.entities.Event;
import com.gavynzhang.doit.model.entities.Tomato;
import com.gavynzhang.doit.utils.LogUtils;
import com.gavynzhang.doit.utils.TimeUtils;

import java.sql.Time;

import cn.bmob.v3.datatype.BmobDate;
import cn.iwgang.countdownview.CountdownView;

public class TomatoActivity extends BaseActivity implements CountdownView.OnCountdownEndListener{

    private static final long TOMATO_WORK_MILL_SEC = 10 * 1000;
    private static final long TOMATO_REST_MILL_SEC = 10 * 1000;

    private static final int MODE_UNSTART = -1;
    private static final int MODE_WORK = 0;
    private static final int MODE_REST = 1;

    private int nowMode = MODE_UNSTART;

    private Event mEvent;

    private int tomatoCount = 0;

    private TextView tomatoEventName;
    private CountdownView mWorkCountdownView;
    private CountdownView mRestCountdownView;
    private TextView promoptText;
    private Button startWork;

    private Vibrator mVibrator;

    private String startTime;

    private Tomato tomato = new Tomato();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tomato);

        //手机震动
        mVibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);

        mEvent = (Event)getIntent().getSerializableExtra("event");

        tomato.setEventId(mEvent.getId().intValue());

        initViews();

        tomatoEventName.setText(mEvent.getName());

        mWorkCountdownView.setTag("work");
        mRestCountdownView.setTag("rest");

        mWorkCountdownView.setOnCountdownEndListener(this);
        mRestCountdownView.setOnCountdownEndListener(this);

        startWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nowMode == MODE_UNSTART){
                    promoptText.setText("工作时间剩余：");
                    mWorkCountdownView.start(TOMATO_WORK_MILL_SEC);
                    nowMode = MODE_WORK;

                    startTime = TimeUtils.getCurTimeString();
                    tomato.setStartTime(new BmobDate(TimeUtils.string2Date(startTime)));

                    startWork.setTextColor(getResources().getColor(R.color.md_red_700));
                    startWork.setText("停止");
                } else if (nowMode == MODE_REST) {

                    nowMode = MODE_UNSTART;

                    Intent intent = new Intent(TomatoActivity.this, IsFinishDialogActivity.class);
                    intent.putExtra("tomatoNum", tomatoCount);
                    intent.putExtra("event", mEvent);
                    startActivityForResult(intent, 1);

                }else if (nowMode == MODE_WORK){
                    //show dialog
                    Intent intent = new Intent(TomatoActivity.this, TomatoBreakDialogActivity.class);
                    intent.putExtra("tomato", tomato);
                    startActivityForResult(intent, 1);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
//                    priOrRemindLevel = data.getIntExtra("event_level", 1);
//                    setLevelText();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initViews(){
        tomatoEventName = $(R.id.tomato_event_name);
        mWorkCountdownView = $(R.id.work_count_down_view);
        mRestCountdownView = $(R.id.rest_count_down_view);
        promoptText = $(R.id.prompt_text);
        startWork = $(R.id.tomato_start);
    }

    @Override
    public void onEnd(CountdownView cv) {
        Object nameTag = cv.getTag();
        if (nameTag != null){
            LogUtils.d("TomatoActivity", "nameTag: "+nameTag.toString());
        }
        if (nameTag.toString().equals("work")){
            nowMode = MODE_REST;
            mVibrator.vibrate(2000);
            tomatoCount += 1;

            //每次工作时间完成后保存数据
            //tomato.setStartTime(new BmobDate(TimeUtils.string2Date(startTime)));
            tomato.setIsBroken(0);
            tomato.setBrokenReason("已完成");
            //保存到数据库
            LoginContext.getLoginContext().saveTomatoDate(tomato);

            promoptText.setText("休息时间剩余：");
            mRestCountdownView.setVisibility(View.VISIBLE);
            mWorkCountdownView.setVisibility(View.GONE);
            mRestCountdownView.start(TOMATO_REST_MILL_SEC);
        }else if (nameTag.toString().equals("rest")){
            nowMode = MODE_WORK;
            startTime = TimeUtils.getCurTimeString();
            tomato.setStartTime(new BmobDate(TimeUtils.string2Date(startTime)));
            promoptText.setText("工作时间剩余：");
            mWorkCountdownView.setVisibility(View.VISIBLE);
            mRestCountdownView.setVisibility(View.GONE);
            mWorkCountdownView.start(TOMATO_WORK_MILL_SEC);
        }else{
            LogUtils.d("ToamtoActivity", "onEnd OUT");
        }
    }
}
