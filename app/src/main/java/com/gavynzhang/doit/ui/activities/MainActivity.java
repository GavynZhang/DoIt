package com.gavynzhang.doit.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;


import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;
import com.gavynzhang.doit.app.state.LoginContext;
import com.gavynzhang.doit.app.state.SignInState;
import com.gavynzhang.doit.model.db.MyDatabaseHelper;
import com.gavynzhang.doit.model.entities.Event;
import com.gavynzhang.doit.model.entities.MyUser;
import com.gavynzhang.doit.service.RemindService;
import com.gavynzhang.doit.ui.adapter.EventItemAdapter;
import com.gavynzhang.doit.ui.decorator.DayDisplayDecorator;
import com.gavynzhang.doit.utils.LogUtils;
import com.gavynzhang.doit.utils.TimeUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobDate;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnDateSelectedListener,OnMonthChangedListener {

    public static MainActivity sMainActivity;

    private static final int ONE_DAY_SEC = 86400;

    private String tmpSelectDate;

    private MaterialCalendarView mCalendarView;
    private RecyclerView eventListRecyclerView;
    private MyDatabaseHelper dbHelper = MyDatabaseHelper.getMyDatabasesHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);
        sMainActivity = this;
        LogUtils.d("MainActivity", "onCreate");
        Bmob.initialize(this, "9f9400b18ebbb85039231d8bd0cf24d2");

        mCalendarView = $(R.id.calendar_view);
        eventListRecyclerView = $(R.id.event_list);

        mCalendarView.addDecorator(new DayDisplayDecorator(queryEvents()));
        mCalendarView.setOnDateChangedListener(this);
        //设置初始进入界面时选择的是今天
        mCalendarView.setSelectedDate(TimeUtils.getCurTimeDate());
        List<Event> displayEvents = getEventsByDate(TimeUtils.getCurTimeDate());
        initData(displayEvents);

//        eventListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (!recyclerView.canScrollVertically(-1)) {
//                    onScrolledToTop();
//                } else if (!recyclerView.canScrollVertically(1)) {
//                    onScrolledToBottom();
//                } else if (dy < 0) {
//                    onScrolledUp();
//                } else if (dy > 0) {
//                    onScrolledDown();
//                }
//            }
//
//            public void onScrolledUp() {
//                LogUtils.d("MainActivity", "onScrolledUp");
//            }
//
//            public void onScrolledDown() {
//                LogUtils.d("MainActivity", "onScrolledDown");
//                mCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
//            }
//
//            public void onScrolledToTop() {
//                LogUtils.d("MainActivity", "onScrolledToTop");
//                mCalendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
//            }
//
//            public void onScrolledToBottom() {
//                LogUtils.d("MainActivity", "onScrolledToBottom");
//            }
//        });

        final Toolbar toolbar = $(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = $(R.id.fab);
        fab.setImageResource(R.drawable.plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewEventActivity.class);
                startActivityForResult(intent, 1);
//                NewEventActivity.actionStart(MainActivity.this);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        Intent intent = new Intent(this, RemindService.class);
//        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (tmpSelectDate == null) {
            mCalendarView.setSelectedDate(TimeUtils.getCurTimeDate());
            List<Event> displayEvents = getEventsByDate(TimeUtils.getCurTimeDate());
            initData(displayEvents);
        }else {
            //加载选择的这一天的数据
            mCalendarView.setSelectedDate(TimeUtils.string2Date(tmpSelectDate));
            List<Event> displayEvents = getEventsByDate(TimeUtils.string2Date(tmpSelectDate));
            initData(displayEvents);
        }

    }

    private boolean isLogin(){
        MyUser user = MyUser.getCurrentUser(MyUser.class);
        if (user != null){
            LoginContext.getLoginContext().setState(new SignInState());
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.login) {
            SignInActivity.actionStart(MainActivity.this);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }  else if (id == R.id.source_agreement) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    LogUtils.i("MainActivity", data.getStringExtra("isSave"));
                    mCalendarView.addDecorator(new DayDisplayDecorator(queryEvents()));
                }
        }
    }

    private void initData(List<Event> events){
        eventListRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        eventListRecyclerView.setAdapter(new EventItemAdapter(events));
    }

    /**
     * 获取需要显示的事件
     *
     * @param date ；日期
     */
    private List<Event> getEventsByDate(Date date){

        List<Event> displayEvents = new ArrayList<>();

        TimeUtils.date2Milliseconds(date);

        long dayBeginMill = TimeUtils.date2Milliseconds(date);
        long dayEndMill = dayBeginMill+ONE_DAY_SEC*1000;

        List<Event> allEvents = queryEvents();

        for (int i = 0; i < allEvents.size(); i++){
            Event event = allEvents.get(i);

            long eventStartTimeMill = TimeUtils.string2Milliseconds(event.getStartTime().getDate());
            long eventEndTimeMill = TimeUtils.string2Milliseconds(event.getEndTime().getDate());

            if (dayBeginMill < eventStartTimeMill && eventStartTimeMill < dayEndMill){
                displayEvents.add(event);
            }else if (dayBeginMill < eventEndTimeMill && dayBeginMill > eventStartTimeMill){
                displayEvents.add(event);
            }
        }
        return displayEvents;
    }

    /**
     * @return 标签List
     */
    private List<String> getEventAllTags(){
        List<String> tags = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Event", null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                String tag;
                if ((tag = cursor.getString(cursor.getColumnIndex("tag"))) != null){

                    if (!tags.isEmpty()) {

                        for (String s : tags) {
                            if (tag.equals(s)) {

                            } else {
                                tags.add(tag);
                                LogUtils.d("MainActivity", "tag: "+tag);
                            }
                        }

                    }else{
                        tags.add(tag);
                        LogUtils.d("MainActivity", "tag: "+tag);
                    }
                }

            }while (cursor.moveToNext());
        }
        cursor.close();
        return tags;
    }

    /**
     * @return 需要提醒的事件List
     */
    private List<Event> getRemindEvents(){
        List<Event> remindEvents = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Event", null, null, null, null, null, null);

        if (cursor.moveToFirst()){
            do {
                Event event = new Event();

                if (cursor.getInt(cursor.getColumnIndex("mode")) == 0){
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String remarks = cursor.getString(cursor.getColumnIndex("remarks"));
                    String remindTime = cursor.getString(cursor.getColumnIndex("remindTime"));

                    event.setId(id);
                    event.setName(name);
                    event.setRemarks(remarks);
                    event.setRemindTime(new BmobDate(TimeUtils.string2Date(remindTime)));

                    remindEvents.add(event);
                }

//                long remindTimeMillSec = TimeUtils.string2Milliseconds(cursor.getString(cursor.getColumnIndex("remindTime")));


            }while (cursor.moveToNext());
        }
        cursor.close();

        return remindEvents;
    }

    /**
     * @return 所有事件的List集合
     *
     */
    private List<Event> queryEvents(){

        List<Event> events = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Event",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                Event event = new Event();
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int mode = cursor.getInt(cursor.getColumnIndex("mode"));
                String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                long startTimeMillSeconds = cursor.getLong(cursor.getColumnIndex("startTimeMillSeconds"));
                String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                long endTimeMillSeconds = cursor.getLong(cursor.getColumnIndex("endTimeMillSeconds"));
                String remindTime = cursor.getString(cursor.getColumnIndex("remindTime"));
                int pri = cursor.getInt(cursor.getColumnIndex("pri"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String remarks = cursor.getString(cursor.getColumnIndex("remarks"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                int isFinish = cursor.getInt(cursor.getColumnIndex("isFinish"));

                event.setId(id);
                LogUtils.d("MainActivity", "Event Id: "+id);
                event.setUserName(username);
                event.setName(name);
                event.setMode(mode);
                event.setStartTime(new BmobDate(TimeUtils.string2Date(startTime)));
                event.setStartTimeMillSeconds(startTimeMillSeconds);
                event.setEndTime(new BmobDate(TimeUtils.string2Date(endTime)));
                event.setEndTimeMillSeconds(endTimeMillSeconds);
                event.setRemindTime(new BmobDate(TimeUtils.string2Date(remindTime)));
                event.setPri(pri);
                LogUtils.d("MainActivity", "pri: "+pri);
                event.setAddress(address);
                event.setRemarks(remarks);
                event.setTag(tag);
                event.setIsFinish(isFinish);

                events.add(event);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        LogUtils.d("MainActivity","OnDateSelect");
        if (date == null){
            LogUtils.w("MainActivity","date == null");
        }else {
            int year = date.getYear();
            int month = date.getMonth()+1;
            int day = date.getDay();

            String monthString = "";
            String dayString = "";

            if (month < 10){
                monthString = "0"+String.valueOf(month);
            }else {
                monthString = String.valueOf(month);
            }

            if (day < 10){
                dayString = "0"+String.valueOf(day);
            }else{
                dayString = String.valueOf(day);
            }
            String dateString = year+"-"+monthString+"-"+dayString+" "+"00:00:00";
            //保存选择的日期，在onResume时加载这一天的数据
            tmpSelectDate = dateString;
            List<Event> displayEvents = getEventsByDate(TimeUtils.string2Date(dateString));
            initData(displayEvents);
        }
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
    }


}
