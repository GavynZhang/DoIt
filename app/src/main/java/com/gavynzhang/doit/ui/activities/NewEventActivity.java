package com.gavynzhang.doit.ui.activities;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;
import com.gavynzhang.doit.utils.TimeUtils;

import org.w3c.dom.Text;

import java.util.Calendar;

public class NewEventActivity extends BaseActivity implements View.OnClickListener{

    private static final String NORMAL_TEXT_1 = "无提醒";
    private static final String NORMAL_TEXT_2 = "事件发生时";
    private static final String NORMAL_TEXT_3 = "提前10分钟";
    private static final String NORMAL_TEXT_4 = "提前30分钟";
    private static final String NORMAL_TEXT_5 = "提前1小时";
    private static final String NORMAL_TEXT_6 = "提前一天";

    private static final int MODE_NORMAL = 0;
    private static final int MODE_TOMATO = 1;

    private int nowMode = MODE_NORMAL;

    //相关值
    private String startDay;
    private String startTime;
    private String endDay;
    private String endTime;
    private String eventNameString;
    private String eventPlaceString;
    private String eventRemarksString;
    private String eventTagString;

    private int priOrRemindLevel;   //pri: 优先级

    /**
     * 以下为控件
     * */
    private Toolbar mToolbar;

    private EditText editEventName;
    private EditText eventPlace;
    private EditText eventRemarks;
    private EditText eventTag;

    private TextView eventStartDay;
    private TextView eventStartTime;
    private TextView eventEndDay;
    private TextView eventEndTime;

    private TextView eventRemindTimePriText;

    private Switch eventModeSwitch;

    private ImageView eventStartDropDay;
    private ImageView eventStartDropTime;
    private ImageView eventEndDropDay;
    private ImageView eventEndDropTime;

    private ImageView eventRemindPriDrop;

    private RelativeLayout newEventRelativeStart;
    private ImageView newEventLine3;
    private ImageView eventRemindPriImg;
    private Button cancelSave;



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

        initViews();
        setOnClickListener();

        mToolbar = $(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setTitle("新事件");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.md_grey_100));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        eventModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    //设置为番茄事件布局并给nowMode赋值
                    setTomatoEventLayout();
                    nowMode = MODE_TOMATO;
                    setSavedLevelText();
                }else{
                    //设置为普通事件布局并且给nowMode赋值
                    setNormalEventLayout();
                    nowMode = MODE_NORMAL;
                    setSavedLevelText();
                }
            }
        });

        setSavedLevelText();
    }

    /**
     * 初始化视图
     */
    private void initViews(){
        newEventRelativeStart = $(R.id.new_event_rt_start);
        newEventLine3 = $(R.id.new_event_line_3);

        //模式切换
        eventModeSwitch = $(R.id.event_mode_switch);
        //提醒和优先级图标
        eventRemindPriImg = $(R.id.event_remind_pri_img);
        //EditText
        editEventName = $(R.id.edit_event_name);
        eventPlace = $(R.id.event_place);
        eventRemarks = $(R.id.event_remarks);
        eventTag = $(R.id.event_tag);
        //时间选择
        eventStartDropDay = $(R.id.new_event_start_drop_day);
        eventStartDropTime = $(R.id.new_event_start_drop_time);
        eventEndDropDay = $(R.id.new_event_end_drop_day);
        eventEndDropTime = $(R.id.new_event_end_drop_time);
        //选择时间展示
        eventStartDay = $(R.id.new_event_start_day);
        eventStartTime = $(R.id.new_event_start_time);
        eventEndDay = $(R.id.new_event_end_day);
        eventEndTime = $(R.id.new_event_end_time);
        //提醒，优先级选择
        eventRemindPriDrop = $(R.id.event_remind_pri_drop);
        //提醒时间及优先级显示文本
        eventRemindTimePriText = $(R.id.event_remind_time_pri_text);
        //底部取消按钮
        cancelSave = $(R.id.cancel_save);
    }

    /**
     * 设置监听
     */
    private void setOnClickListener(){

        //时间选择下拉箭头
        eventStartDropDay.setOnClickListener(this);
        eventStartDropTime.setOnClickListener(this);
        eventEndDropDay.setOnClickListener(this);
        eventEndDropTime.setOnClickListener(this);

        //提醒，优先级选择
        eventRemindPriDrop.setOnClickListener(this);

        //取消，保存
        cancelSave.setOnClickListener(this);
    }

    /**
     * 设置为番茄事件布局
     */
    private void setTomatoEventLayout(){
        newEventRelativeStart.setVisibility(View.GONE);
        newEventLine3.setVisibility(View.GONE);
        eventRemindPriImg.setImageResource(R.drawable.pri);
        //在写完Dialog后更改
        eventRemindTimePriText.setText("优先级 1");
    }

    /**
     * 设置为普通布局
     */
    private void setNormalEventLayout(){
        newEventRelativeStart.setVisibility(View.VISIBLE);
        newEventLine3.setVisibility(View.VISIBLE);
        eventRemindPriImg.setImageResource(R.drawable.ic_alarm_black_24dp);
        //在写完Dialog后更改
        eventRemindTimePriText.setText("提前10分钟");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.new_event_start_drop_day:
                showDatePickerDialogAndSetStartDay();
                break;
            case R.id.new_event_start_drop_time:
                showTimePickerDialogAndSetStartTime();
                break;
            case R.id.new_event_end_drop_day:
                showDatePickerDialogAndSetEndDay();
                break;
            case R.id.new_event_end_drop_time:
                showTimePickerDialogAndSetEndTime();
                break;
            case R.id.event_remind_pri_drop:
                Intent intent = new Intent(NewEventActivity.this, EventRemindTimePriDialogActivity.class);
                //将事件模式传递给Dialog
                intent.putExtra("event_mode", nowMode);
                startActivityForResult(intent, 1);
                break;
            case R.id.cancel_save:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取Dialog返回的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    priOrRemindLevel = data.getIntExtra("event_level", 1);
//                    Toast.makeText(NewEventActivity.this, "选择的数据："+priOrRemindLevel, Toast.LENGTH_SHORT).show();
                    setLevelText();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置保存的提醒时间或优先级文字
     */
    private void setSavedLevelText(){
        SharedPreferences pref = getSharedPreferences("level_tmp", MODE_PRIVATE);
        int tmpNormalLevel = pref.getInt("normal_mode_level", 1);
        int tmpTomatoLevel = pref.getInt("tomato_mode_level", 1);

        if (nowMode == MODE_NORMAL){
            switch (tmpNormalLevel){
                case 1:
                    eventRemindTimePriText.setText(NORMAL_TEXT_1);
                    break;
                case 2:
                    eventRemindTimePriText.setText(NORMAL_TEXT_2);
                    break;
                case 3:
                    eventRemindTimePriText.setText(NORMAL_TEXT_3);
                    break;
                case 4:
                    eventRemindTimePriText.setText(NORMAL_TEXT_4);
                    break;
                case 5:
                    eventRemindTimePriText.setText(NORMAL_TEXT_5);
                    break;
                case 6:
                    eventRemindTimePriText.setText(NORMAL_TEXT_6);
                    break;
                default:
            }
        }else{
            eventRemindTimePriText.setText("优先级 "+tmpTomatoLevel);
        }
    }

    /**
     * 设置提醒时间或优先级文字
     */
    private void setLevelText(){

        if (nowMode == MODE_NORMAL){
            switch (priOrRemindLevel){
                case 1:
                    eventRemindTimePriText.setText(NORMAL_TEXT_1);
                    break;
                case 2:
                    eventRemindTimePriText.setText(NORMAL_TEXT_2);
                    break;
                case 3:
                    eventRemindTimePriText.setText(NORMAL_TEXT_3);
                    break;
                case 4:
                    eventRemindTimePriText.setText(NORMAL_TEXT_4);
                    break;
                case 5:
                    eventRemindTimePriText.setText(NORMAL_TEXT_5);
                    break;
                case 6:
                    eventRemindTimePriText.setText(NORMAL_TEXT_6);
                    break;
                default:
                    break;
            }
        }else{
            eventRemindTimePriText.setText("优先级 "+priOrRemindLevel);
        }

    }

    private void showDatePickerDialogAndSetStartDay(){

        Calendar c = Calendar.getInstance();
        new DatePickerDialog(NewEventActivity.this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month +1;
                startDay = year+"/"+month+"/"+dayOfMonth;
                eventStartDay.setText(startDay+"周"+TimeUtils.getWeek(startDay));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showDatePickerDialogAndSetEndDay(){

        Calendar c = Calendar.getInstance();
        new DatePickerDialog(NewEventActivity.this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month += 1;
                endDay = year+"/"+month+"/"+dayOfMonth;
                eventEndDay.setText(endDay+"周"+TimeUtils.getWeek(endDay));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePickerDialogAndSetStartTime(){
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(NewEventActivity.this,
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        String hourOfDayString = "";
                        String minuteString = "";
                        if (hourOfDay < 10){
                            hourOfDayString = "0"+String.valueOf(hourOfDay);
                        }else{
                            hourOfDayString = String.valueOf(hourOfDay);
                        }
                        if (minute < 10){
                            minuteString = "0"+String.valueOf(minute);
                        } else {
                            minuteString = String.valueOf(minute);
                        }
                        eventStartTime.setText(hourOfDayString+":"+minuteString);
                        startTime = hourOfDayString+":"+minuteString;
                    }
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),true).show();
    }

    private void showTimePickerDialogAndSetEndTime(){
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(NewEventActivity.this,
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        String hourOfDayString = "";
                        String minuteString = "";
                        if (hourOfDay < 10){
                            hourOfDayString = "0"+String.valueOf(hourOfDay);
                        }else{
                            hourOfDayString = String.valueOf(hourOfDay);
                        }
                        if (minute < 10){
                            minuteString = "0"+String.valueOf(minute);
                        } else {
                            minuteString = String.valueOf(minute);
                        }
                        eventEndTime.setText(hourOfDayString+":"+minuteString);
                        endTime = hourOfDayString+":"+minuteString;
                    }
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),true).show();
    }
}
