package com.gavynzhang.doit.ui.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.ActivityCollector;
import com.gavynzhang.doit.app.BaseActivity;
import com.gavynzhang.doit.model.db.MyDatabaseHelper;
import com.gavynzhang.doit.model.entities.Event;
import com.gavynzhang.doit.utils.LogUtils;
import com.gavynzhang.doit.utils.TimeUtils;

import cn.bmob.v3.datatype.BmobDate;

public class EventDetailsActivity extends BaseActivity {

    private static final String NORMAL_TEXT_1 = "无提醒";
    private static final String NORMAL_TEXT_2 = "事件发生时";
    private static final String NORMAL_TEXT_3 = "提前10分钟";
    private static final String NORMAL_TEXT_4 = "提前30分钟";
    private static final String NORMAL_TEXT_5 = "提前1小时";
    private static final String NORMAL_TEXT_6 = "提前一天";

    private static final int HOUR_SEC = 3600;

    private static final int REMIND_LEVEL3_SEC = 600;
    private static final int REMIND_LEVEL4_SEC = 1800;
    private static final int REMIND_LEVEL5_SEC = HOUR_SEC;
    private static final int REMIND_LEVEL6_SEC = 86400;


    private int pri;

    private Toolbar mToolbar;
    private Event mEvent;
    private TextView eventName;
    private TextView eventTag;
    private TextView eventRemark;
    private TextView eventAddress;
    private TextView eventTime;
    private TextView eventPriOrRemind;
    private ImageView eventRemindPriDrop;
    private Button startTomato;

    private RelativeLayout eventPriOrRemindRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_event_details);

        mEvent = (Event)getIntent().getSerializableExtra("event");

//        //获取从isFinishDialogActivity启动本Activity的Event
//        if (getIntent().getSerializableExtra("eventFromIsFinishDialog") != null){
//            LogUtils.d("EventDetailsActivity", "获取从isFinishDialogActivity启动本Activity的Event成功");
//            mEvent = (Event)getIntent().getSerializableExtra("eventFromIsFinishDialog");
//        }

        pri = mEvent.getPri().intValue();

        LogUtils.d("EventDetailsActivity", "mEvent.getName(): "+mEvent.getName());

        initViews();
        setText();

        if (mEvent.getMode().intValue() == 1){
            startTomato.setVisibility(View.VISIBLE);

            //如果该事件已完成，设置按钮文字，颜色及不可点击
            if (mEvent.getIsFinish().intValue() == 1){
                startTomato.setText("已完成");
                startTomato.setEnabled(false);
                startTomato.setTextColor(getResources().getColor(R.color.md_grey_400));
            }

        }

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

        eventRemindPriDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.d("EventDetailsActivity","drop clicked");
                Intent intent = new Intent(EventDetailsActivity.this, EventRemindTimePriDialogActivity.class);
                intent.putExtra("detail_event_mode",mEvent.getMode().intValue());
                intent.putExtra("detail_event_remind_pri_level", mEvent.getPri());

                startActivityForResult(intent, 1);
            }
        });

        startTomato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDetailsActivity.this, TomatoActivity.class);
                intent.putExtra("event",mEvent);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

//        //获取从isFinishDialogActivity启动本Activity的Event
//        if (getIntent().getSerializableExtra("eventFromIsFinishDialog") != null){
//            LogUtils.d("EventDetailsActivity", "获取从isFinishDialogActivity启动本Activity的Event成功");
//            mEvent = (Event)getIntent().getSerializableExtra("eventFromIsFinishDialog");
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:

                if (resultCode == RESULT_OK) {

                    int level = data.getIntExtra("event_level", 1);
                    mEvent.setPri(level);

                    if (mEvent.getMode().intValue() == 0){

                        switch (level){
                            case 1:
                                //mEvent.setRemindTime(new BmobDate(TimeUtils.milliseconds2Date(1)));
                                eventPriOrRemind.setText(NORMAL_TEXT_1);
                                break;
                            case 2:
                                //mEvent.setRemindTime(mEvent.getStartTime());
                                eventPriOrRemind.setText(NORMAL_TEXT_2);
                                break;
                            case 3:
                                //mEvent.setRemindTime(new BmobDate(TimeUtils.milliseconds2Date(mEvent.getStartTimeMillSeconds().longValue() - REMIND_LEVEL3_SEC*1000)));
                                eventPriOrRemind.setText(NORMAL_TEXT_3);
                                break;
                            case 4:
                                //mEvent.setRemindTime(new BmobDate(TimeUtils.milliseconds2Date(mEvent.getStartTimeMillSeconds().longValue() - REMIND_LEVEL4_SEC*1000)));
                                eventPriOrRemind.setText(NORMAL_TEXT_4);
                                break;
                            case 5:
                                //mEvent.setRemindTime(new BmobDate(TimeUtils.milliseconds2Date(mEvent.getStartTimeMillSeconds().longValue() - REMIND_LEVEL5_SEC*1000)));
                                eventPriOrRemind.setText(NORMAL_TEXT_5);
                                break;
                            case 6:
                                //mEvent.setRemindTime(new BmobDate(TimeUtils.milliseconds2Date(mEvent.getStartTimeMillSeconds().longValue() - REMIND_LEVEL6_SEC*1000)));
                                LogUtils.d("EventDetailsActivity", "setLevel_Normal_is  6 ");
                                eventPriOrRemind.setText(NORMAL_TEXT_6);
                                break;
                        }
                    }else{
                        mEvent.setPri(level);
                        eventPriOrRemind.setText("优先级 "+level);
                    }
                    updateToDatabases();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 将做出的数据更改添加到数据库
     */
    private void updateToDatabases(){
        SQLiteDatabase db = MyDatabaseHelper.getMyDatabasesHelper().getWritableDatabase();
        ContentValues values = new ContentValues();

        int id = mEvent.getId().intValue();
        int tmpPri = mEvent.getPri().intValue();
        LogUtils.d("EventDetailsActivity", "id: "+id);

        values.put("pri",tmpPri);
        LogUtils.d("EventDetailsActivity", "pri: "+tmpPri);

        if (mEvent.getMode().intValue() == 0){
            switch (tmpPri){
                case 1:
                    values.put("remindTime", TimeUtils.milliseconds2String(1));
                    break;
                case 2:
                    values.put("remindTime", TimeUtils.milliseconds2String(mEvent.getStartTimeMillSeconds().longValue()));
                    break;
                case 3:
                    values.put("remindTime", TimeUtils.milliseconds2String(mEvent.getStartTimeMillSeconds().longValue() - REMIND_LEVEL3_SEC * 1000));
                    break;
                case 4:
                    values.put("remindTime", TimeUtils.milliseconds2String(mEvent.getStartTimeMillSeconds().longValue() - REMIND_LEVEL4_SEC * 1000));
                    break;
                case 5:
                    values.put("remindTime", TimeUtils.milliseconds2String(mEvent.getStartTimeMillSeconds().longValue() - REMIND_LEVEL5_SEC * 1000));
                    break;
                case 6:
                    values.put("remindTime", TimeUtils.milliseconds2String(mEvent.getStartTimeMillSeconds().longValue() - REMIND_LEVEL6_SEC * 1000));
                    break;

            }
        }

        db.update("Event", values, "id = ?", new String[]{String.valueOf(id)});

    }

    private void initViews(){
        mToolbar = $(R.id.toolbar);
        eventName = $(R.id.event_detail_name);
        eventTag = $(R.id.event_details_tag_text);
        eventRemark = $(R.id.event_details_remark_text);
        eventAddress = $(R.id.event_details_address_text);
        eventTime = $(R.id.event_details_time_text);
        eventPriOrRemind = $(R.id.event_details_remind_pri_text);
        eventPriOrRemindRelativeLayout = $(R.id.event_details_remind_pri_relative);
        eventRemindPriDrop = $(R.id.event_details_remind_pri_drop);
        startTomato = $(R.id.start_tomato_activity);
    }

    private void setText(){
        eventName.setText(mEvent.getName());

        if (mEvent.getTag() != null) {
            eventTag.setText(mEvent.getTag());
        }else{
            eventTag.setText("");
        }

        if (mEvent.getRemarks() != null) {
            eventRemark.setText(mEvent.getRemarks());
        }else{
            eventRemark.setText("");
        }

        if (mEvent.getAddress() != null) {
            eventAddress.setText(mEvent.getAddress());
        }else {
            eventAddress.setText("");
        }

        if (mEvent.getMode().intValue() == 0){
            eventTime.setText(mEvent.getStartTime().getDate().substring(5,16) +" —— "+mEvent.getEndTime().getDate().substring(5,16));

//            eventPriOrRemind.setText(mEvent.getRemindTime().getDate());

            switch (pri){
                case 1:
                    eventPriOrRemind.setText(NORMAL_TEXT_1);
                    break;
                case 2:
                    eventPriOrRemind.setText(NORMAL_TEXT_2);
                    break;
                case 3:
                    eventPriOrRemind.setText(NORMAL_TEXT_3);
                    break;
                case 4:
                    eventPriOrRemind.setText(NORMAL_TEXT_4);
                    break;
                case 5:
                    eventPriOrRemind.setText(NORMAL_TEXT_5);
                    break;
                case 6:
                    eventPriOrRemind.setText(NORMAL_TEXT_6);
                    break;
            }
        }else{
            eventTime.setText(mEvent.getEndTime().getDate()+"截止");
//            eventPriOrRemindRelativeLayout.setVisibility(View.INVISIBLE);
            eventPriOrRemind.setText("优先级 "+pri);
        }
    }
}
