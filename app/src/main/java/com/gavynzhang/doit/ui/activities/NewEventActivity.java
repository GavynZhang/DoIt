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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.gavynzhang.doit.app.state.LoginContext;
import com.gavynzhang.doit.app.state.LogoutState;
import com.gavynzhang.doit.app.state.MyApplication;
import com.gavynzhang.doit.model.entities.Event;
import com.gavynzhang.doit.utils.LogUtils;
import com.gavynzhang.doit.utils.TimeUtils;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobDate;

public class NewEventActivity extends BaseActivity implements View.OnClickListener {

    private static final int HOUR_SEC = 3600;

    private static final int REMIND_LEVEL3_SEC = 600;
    private static final int REMIND_LEVEL4_SEC = 1800;
    private static final int REMIND_LEVEL5_SEC = HOUR_SEC;
    private static final int REMIND_LEVEL6_SEC = 86400;

    private static final String NORMAL_TEXT_1 = "无提醒";
    private static final String NORMAL_TEXT_2 = "事件发生时";
    private static final String NORMAL_TEXT_3 = "提前10分钟";
    private static final String NORMAL_TEXT_4 = "提前30分钟";
    private static final String NORMAL_TEXT_5 = "提前1小时";
    private static final String NORMAL_TEXT_6 = "提前一天";

    private static final int MODE_NORMAL = 0;
    private static final int MODE_TOMATO = 1;

    private int nowMode = MODE_NORMAL;

    private Event mEvent = new Event();

    //相关值
    private String startDay;
    private String startTime;
    private String endDay;
    private String endTime;

    private String finalStartTime;
    private String finalEndTime;

    private String eventNameString;
    private String eventPlaceString;
    private String eventRemarksString;
    private String eventTagString;

    private int priOrRemindLevel;   //pri: 优先级

    /**
     * 以下为控件
     */
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
    private Button saveEvent;


    /**
     * 用于启动本Activity
     *
     * @param context
     */
    public static void actionStart(Context context) {
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

        saveEvent.setClickable(false);

        editEventName.addTextChangedListener(new TextWatcher() {
            private CharSequence tmp;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                tmp = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                //当名称输入框有数据时，设置保存按钮可点击及文字颜色
                if (tmp.length() > 0) {
                    saveEvent.setTextColor(getResources().getColor(R.color.mint_green));
                    saveEvent.setClickable(true);
                } else {
                    saveEvent.setTextColor(getResources().getColor(R.color.md_grey_400));
                    saveEvent.setClickable(false);
                }
            }
        });

        //获取当前时间戳
        long curTimeMills = TimeUtils.getCurTimeMills();

        //设置默认开始时间为当前时间后半小时（未来将此处放置于设置中）
        long defaultBeginTimeMills = curTimeMills + HOUR_SEC*1000/2 ;
        //设置默认开始时间
        BmobDate defaultBeginDate = new BmobDate(TimeUtils.milliseconds2Date(defaultBeginTimeMills));
        mEvent.setStartTime(defaultBeginDate);
        //毫秒时间戳转字符串
        String defaultBeginDateString = TimeUtils.milliseconds2String(defaultBeginTimeMills);
        //事件默认开始日期
        String defaultStartDay = defaultBeginDateString.substring(0,10);
        eventStartDay.setText(defaultStartDay + "周" + TimeUtils.getWeek(defaultStartDay));
        //事件默认开始时间
        String defaultStartTime = defaultBeginDateString.substring(11,16);
        eventStartTime.setText(defaultStartTime);


        //默认结束时间为开始时间后一小时
        long defaultEndTimeMills = defaultBeginTimeMills + HOUR_SEC * 1000;
        //设置默认结束时间
        BmobDate defaultEndDate = new BmobDate(TimeUtils.milliseconds2Date(defaultEndTimeMills));
        mEvent.setEndTime(defaultEndDate);
        //设置显示文字
        String defaultEndDateString = TimeUtils.milliseconds2String(defaultEndTimeMills);
        String defaultEndDay = defaultEndDateString.substring(0,10);
        eventEndDay.setText(defaultEndDay + "周" + TimeUtils.getWeek(defaultEndDay));
        String defaultEndTime = defaultEndDateString.substring(11,16);
        eventEndTime.setText(defaultEndTime);


        eventModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //设置为番茄事件布局并给nowMode赋值
                    setTomatoEventLayout();
                    nowMode = MODE_TOMATO;
                    setSavedLevelText();
                } else {
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
    private void initViews() {
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
        //底部保存按钮
        saveEvent = $(R.id.save_event);
    }

    /**
     * 设置监听
     */
    private void setOnClickListener() {

        //时间选择下拉箭头
        eventStartDropDay.setOnClickListener(this);
        eventStartDropTime.setOnClickListener(this);
        eventEndDropDay.setOnClickListener(this);
        eventEndDropTime.setOnClickListener(this);

        //提醒，优先级选择
        eventRemindPriDrop.setOnClickListener(this);

        //取消，保存
        cancelSave.setOnClickListener(this);
        //保存
        saveEvent.setOnClickListener(this);
    }

    /**
     * 设置为番茄事件布局
     */
    private void setTomatoEventLayout() {
        newEventRelativeStart.setVisibility(View.GONE);
        newEventLine3.setVisibility(View.GONE);
        eventRemindPriImg.setImageResource(R.drawable.pri);
        //在写完Dialog后更改
        eventRemindTimePriText.setText("优先级 1");
    }

    /**
     * 设置为普通布局
     */
    private void setNormalEventLayout() {
        newEventRelativeStart.setVisibility(View.VISIBLE);
        newEventLine3.setVisibility(View.VISIBLE);
        eventRemindPriImg.setImageResource(R.drawable.ic_alarm_black_24dp);
        //在写完Dialog后更改
        eventRemindTimePriText.setText("提前10分钟");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.save_event:
                eventNameString = editEventName.getText().toString().trim();
                eventPlaceString = eventPlace.getText().toString();
                eventRemarksString = eventRemarks.getText().toString();
                eventTagString = eventTag.getText().toString();
                getEvent();

                LoginContext.getLoginContext().saveDate(mEvent);
                break;
            default:
                break;
        }
    }

    /**
     * 组装Event
     */
    private void getEvent() {
        //事件模式
        mEvent.setMode(nowMode);
        //事件名称
        mEvent.setName(eventNameString);
        //事件发生地点
        LogUtils.w("NewEventActivity", "eventPlaceString"+eventPlaceString);
        if (eventPlaceString == null) {
            mEvent.setAddress("unKnown");
        }else {
            mEvent.setAddress(eventPlaceString);
        }
        //事件备注
        if (eventRemarksString == null) {
            mEvent.setRemarks("unKnown");
        }else {
            mEvent.setRemarks(eventRemarksString);
        }
        //事件标签
        if (eventTagString == null){
            mEvent.setTag("unknown");
        }else {
            mEvent.setTag(eventTagString);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date tmpStartDate = null;
        BmobDate startDate = null;
        /**
         * 用户设置的开始时间不为空
         * */
        if (startDay != null && startTime != null) {
            finalStartTime = startDay + " " + startTime;
            tmpStartDate = TimeUtils.string2Date(finalStartTime, format);

            startDate = new BmobDate(tmpStartDate);
            //事件开始时间
            mEvent.setStartTime(startDate);
        }

        BmobDate endDate = null;

        if (endDay != null && endTime != null) {
            finalEndTime = endDay + " " + endTime;
            endDate = new BmobDate(TimeUtils.string2Date(finalEndTime, format));
            //事件结束时间
            mEvent.setEndTime(endDate);
        }


        if (nowMode == MODE_NORMAL) {
            mEvent.setPri(-1);
            LogUtils.d("NewEventActivity","priOrRemindLevel: "+priOrRemindLevel);
            long tmpStartMilliseconds;

            //初始化tmpStartMilliseconds
            if (startDay != null && startTime != null) {
                tmpStartMilliseconds = TimeUtils.date2Milliseconds(tmpStartDate);
            }else{
                tmpStartMilliseconds = TimeUtils.string2Milliseconds(mEvent.getStartTime().getDate());
            }



                switch (priOrRemindLevel) {
                    case 1:
                        //not to remind
                        mEvent.setRemindTime(null);
                        break;
                    case 2:
                        //When event happen, remind user
                        mEvent.setRemindTime(startDate);
                        break;
                    case 3:
                        //事件发生前10分钟提醒
                        long tmpRemindMillisecondsLevel3 = tmpStartMilliseconds - REMIND_LEVEL3_SEC * 1000;
                        Date tmpRemindDateLevel3 = TimeUtils.milliseconds2Date(tmpRemindMillisecondsLevel3);
                        mEvent.setRemindTime(new BmobDate(tmpRemindDateLevel3));
                        break;
                    case 4:
                        //事件发生前30分钟提醒
                        long tmpRemindMillisecondsLevel4 = tmpStartMilliseconds - REMIND_LEVEL4_SEC * 1000;
                        Date tmpRemindDateLevel4 = TimeUtils.milliseconds2Date(tmpRemindMillisecondsLevel4);
                        mEvent.setRemindTime(new BmobDate(tmpRemindDateLevel4));

                        break;
                    case 5:
                        //事件发生前1小时提醒
                        long tmpRemindMillisecondsLevel5 = tmpStartMilliseconds - REMIND_LEVEL5_SEC * 1000;
                        Date tmpRemindDateLevel5 = TimeUtils.milliseconds2Date(tmpRemindMillisecondsLevel5);
                        mEvent.setRemindTime(new BmobDate(tmpRemindDateLevel5));

                        break;
                    case 6:
                        //事件发生前一天提醒
                        long tmpRemindMillisecondsLevel6 = tmpStartMilliseconds - REMIND_LEVEL6_SEC * 1000;
                        Date tmpRemindDateLevel6 = TimeUtils.milliseconds2Date(tmpRemindMillisecondsLevel6);
                        mEvent.setRemindTime(new BmobDate(tmpRemindDateLevel6));

                        break;
                    default:
                        break;
                }


        } else {
            if (endDay != null && endTime != null) {
                //番茄事件设置提醒时间（不提醒，防NullPointer）
                mEvent.setRemindTime(endDate);
            }
            if (priOrRemindLevel != 0) {
                //优先级
                mEvent.setPri(priOrRemindLevel);
            }
        }


    }

    private void setDefaultEndDayWhenSetStartDay(){
        endDay = startDay;
    }

    /**
     * 获取Dialog返回的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    priOrRemindLevel = data.getIntExtra("event_level", 1);
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
    private void setSavedLevelText() {
        SharedPreferences pref = getSharedPreferences("level_tmp", MODE_PRIVATE);
        int tmpNormalLevel = pref.getInt("normal_mode_level", 3);
        int tmpTomatoLevel = pref.getInt("tomato_mode_level", 1);

        if (nowMode == MODE_NORMAL) {
            String defaultStartDateString = mEvent.getStartTime().getDate();
            Date defaultStartDate = TimeUtils.string2Date(defaultStartDateString);
            long defaultStartDateMills = TimeUtils.date2Milliseconds(defaultStartDate);
            long defaultRemindTime;
            BmobDate remindDate;

            switch (tmpNormalLevel) {
                case 1:
                    eventRemindTimePriText.setText(NORMAL_TEXT_1);
                    mEvent.setRemindTime(null);
                    break;
                case 2:
                    eventRemindTimePriText.setText(NORMAL_TEXT_2);
                    //设置为事件默认开始时间
                    BmobDate defaultStartBmobDate = new BmobDate(defaultStartDate);
                    mEvent.setRemindTime(defaultStartBmobDate);
                    break;
                case 3:
                    eventRemindTimePriText.setText(NORMAL_TEXT_3);
                    defaultRemindTime = defaultStartDateMills - REMIND_LEVEL3_SEC*1000;
                    remindDate = new BmobDate(TimeUtils.milliseconds2Date(defaultRemindTime));
                    mEvent.setRemindTime(remindDate);
                    break;
                case 4:
                    eventRemindTimePriText.setText(NORMAL_TEXT_4);
                    defaultRemindTime = defaultStartDateMills - REMIND_LEVEL4_SEC*1000;
                    remindDate = new BmobDate(TimeUtils.milliseconds2Date(defaultRemindTime));
                    mEvent.setRemindTime(remindDate);
                    break;
                case 5:
                    eventRemindTimePriText.setText(NORMAL_TEXT_5);
                    defaultRemindTime = defaultStartDateMills - REMIND_LEVEL5_SEC*1000;
                    remindDate = new BmobDate(TimeUtils.milliseconds2Date(defaultRemindTime));
                    mEvent.setRemindTime(remindDate);
                    break;
                case 6:
                    eventRemindTimePriText.setText(NORMAL_TEXT_6);
                    defaultRemindTime = defaultStartDateMills - REMIND_LEVEL6_SEC*1000;
                    remindDate = new BmobDate(TimeUtils.milliseconds2Date(defaultRemindTime));
                    mEvent.setRemindTime(remindDate);
                    break;
                default:
            }
        } else {
            eventRemindTimePriText.setText("优先级 " + tmpTomatoLevel);
            mEvent.setPri(tmpTomatoLevel);
        }
    }

    /**
     * 设置提醒时间或优先级文字
     */
    private void setLevelText() {

        if (nowMode == MODE_NORMAL) {
            switch (priOrRemindLevel) {
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
        } else {
            eventRemindTimePriText.setText("优先级 " + priOrRemindLevel);
        }

    }

    private void showDatePickerDialogAndSetStartDay() {

        Calendar c = Calendar.getInstance();
        new DatePickerDialog(NewEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String monthString;
                String dayOfMonthString;
                month = month + 1;
                if (month < 10){
                    monthString = "0"+month;
                }else {
                    monthString = String.valueOf(month);
                }
                if (dayOfMonth < 10){
                    dayOfMonthString = "0"+dayOfMonth;
                }else{
                    dayOfMonthString = String.valueOf(dayOfMonth);
                }
                startDay = year + "-" + month + "-" + dayOfMonth;
                eventStartDay.setText(year+ "-" + monthString + "-" + dayOfMonthString + "周" + TimeUtils.getWeek(startDay));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showDatePickerDialogAndSetEndDay() {

        Calendar c = Calendar.getInstance();
        new DatePickerDialog(NewEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String monthString;
                String dayOfMonthString;
                month += 1;
                if (month < 10){
                    monthString = "0"+month;
                }else{
                    monthString = String.valueOf(month);
                }
                if (dayOfMonth < 10){
                    dayOfMonthString = "0"+dayOfMonth;
                }else{
                    dayOfMonthString = String.valueOf(dayOfMonth);
                }
                endDay = year + "-" + month + "-" + dayOfMonth;
                eventEndDay.setText(year+"-" + monthString + "-" + dayOfMonthString + "周" + TimeUtils.getWeek(endDay));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void showTimePickerDialogAndSetStartTime() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(NewEventActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        String hourOfDayString = "";
                        String minuteString = "";
                        if (hourOfDay < 10) {
                            hourOfDayString = "0" + String.valueOf(hourOfDay);
                        } else {
                            hourOfDayString = String.valueOf(hourOfDay);
                        }
                        if (minute < 10) {
                            minuteString = "0" + String.valueOf(minute);
                        } else {
                            minuteString = String.valueOf(minute);
                        }
                        eventStartTime.setText(hourOfDayString + ":" + minuteString);
                        startTime = hourOfDayString + ":" + minuteString;
                    }
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE), true).show();
    }

    private void showTimePickerDialogAndSetEndTime() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(NewEventActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        String hourOfDayString = "";
                        String minuteString = "";
                        if (hourOfDay < 10) {
                            hourOfDayString = "0" + String.valueOf(hourOfDay);
                        } else {
                            hourOfDayString = String.valueOf(hourOfDay);
                        }
                        if (minute < 10) {
                            minuteString = "0" + String.valueOf(minute);
                        } else {
                            minuteString = String.valueOf(minute);
                        }
                        eventEndTime.setText(hourOfDayString + ":" + minuteString);
                        endTime = hourOfDayString + ":" + minuteString;
                    }
                },
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE), true).show();
    }
}
