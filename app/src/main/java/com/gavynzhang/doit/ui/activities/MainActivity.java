package com.gavynzhang.doit.ui.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.BaseActivity;
import com.gavynzhang.doit.app.state.LoginContext;
import com.gavynzhang.doit.app.state.SignInState;
import com.gavynzhang.doit.model.db.MyDatabaseHelper;
import com.gavynzhang.doit.model.entities.Event;
import com.gavynzhang.doit.model.entities.MyUser;
import com.gavynzhang.doit.ui.decorator.DayDisplayDecorator;
import com.gavynzhang.doit.utils.LogUtils;
import com.gavynzhang.doit.utils.TimeUtils;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobDate;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MaterialCalendarView mCalendarView;
    private RecyclerView eventListRecyclerView;
    private MyDatabaseHelper dbHelper = MyDatabaseHelper.getMyDatabasesHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "9f9400b18ebbb85039231d8bd0cf24d2");

        mCalendarView = $(R.id.calendar_view);
        eventListRecyclerView = $(R.id.event_list);

//        LogUtils.d("MainActivity", "nowDate: "+TimeUtils.getCurTimeString());
//        long nowDayLong = TimeUtils.string2Milliseconds(TimeUtils.getCurTimeString());
//        LogUtils.d("MainActivity", "date: "+nowDayLong);
//        String nowDay = TimeUtils.getCurTimeString().substring(0,10)+" 00:00:00";
//
//        long nowDayBegin = TimeUtils.string2Milliseconds(nowDay);
//        LogUtils.d("MainActivity", "nowdaybegin:"+nowDayBegin);

//        mCalendarView.setBackgroundDrawable(MyApplication.getContext().getResources().getDrawable(R.drawable.check));

        mCalendarView.addDecorator(new DayDisplayDecorator(queryEvents()));

        final Toolbar toolbar = $(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = $(R.id.fab);
        fab.setImageResource(R.drawable.plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出选择框dialog
                NewEventActivity.actionStart(MainActivity.this);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.source_agreement) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 获取需要显示的事件
     *
     * @param date
     */
    private List<Event> getDisplayEvents(Date date){

        List<Event> displayEvents = new ArrayList<>();

        String nowDayBegin = TimeUtils.getCurTimeString().substring(0,10)+" 00:00:00";
        String nowDayEnd = TimeUtils.getCurTimeString().substring(0,10)+" 24:00:00";
        long nowDayBeginMill = TimeUtils.string2Milliseconds(nowDayBegin);
        long nowDayEndMill = TimeUtils.string2Milliseconds(nowDayEnd);
        LogUtils.d("MainActivity", "nowDayBegin:"+nowDayBegin);

        List<Event> allEvents = queryEvents();

        for (int i = 0; i < allEvents.size(); i++){
            Event event = allEvents.get(i);

            long eventStartTimeMill = TimeUtils.string2Milliseconds(event.getStartTime().getDate());
            long eventEndTimeMill = TimeUtils.string2Milliseconds(event.getEndTime().getDate());

            if (nowDayBeginMill < eventStartTimeMill && eventStartTimeMill < nowDayEndMill){
                displayEvents.add(event);
            }else if (nowDayBeginMill < eventEndTimeMill && nowDayBeginMill > eventStartTimeMill){
                displayEvents.add(event);
            }
        }
        return displayEvents;
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
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int mode = cursor.getInt(cursor.getColumnIndex("mode"));
                String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                String remindTime = cursor.getString(cursor.getColumnIndex("remindTime"));
                int pri = cursor.getInt(cursor.getColumnIndex("pri"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String remarks = cursor.getString(cursor.getColumnIndex("remarks"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                int isFinish = cursor.getInt(cursor.getColumnIndex("isFinish"));


                event.setUserName(username);
                event.setName(name);
                event.setMode(mode);
                event.setStartTime(new BmobDate(TimeUtils.string2Date(startTime)));
                event.setEndTime(new BmobDate(TimeUtils.string2Date(endTime)));
                event.setRemindTime(new BmobDate(TimeUtils.string2Date(remindTime)));
                event.setPri(pri);
                event.setAddress(address);
                event.setRemarks(remarks);
                event.setTag(tag);
                event.setFinish(isFinish);

                events.add(event);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }
}
