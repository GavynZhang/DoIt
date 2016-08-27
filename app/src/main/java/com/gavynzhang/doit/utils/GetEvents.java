package com.gavynzhang.doit.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gavynzhang.doit.model.db.MyDatabaseHelper;
import com.gavynzhang.doit.model.entities.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by GavynZhang on 2016/8/26 21:40.
 */
public class GetEvents {

    private static final int ONE_DAY_SEC = 86400;
    private static MyDatabaseHelper dbHelper = MyDatabaseHelper.getMyDatabasesHelper();

    /**
     * 获取需要显示的事件
     *
     * @param date ；日期
     */
    public static List<Event> getEventsByDate(Date date){

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
    public static List<String> getEventAllTags(){
        List<String> tags = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Event", null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                String tag;
                if (!(tag = cursor.getString(cursor.getColumnIndex("tag"))).equals("")){

                    tags.add(tag);
                    LogUtils.d("MainActivity", "tag: "+tag);
                }

            }while (cursor.moveToNext());
        }
        cursor.close();

        //去掉重复值
        for  ( int  i  =   0 ; i  <  tags.size()  -   1 ; i ++ )   {
            for  ( int  j  =  tags.size()  -   1 ; j  >  i; j -- )   {
                if  (tags.get(j).equals(tags.get(i)))   {
                    tags.remove(j);
                }
            }
        }
        return tags;
    }

    public static List<Event> getEventsByTag(String tmpTag){
        List<Event> events = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Event", null, null, null, null, null, null);

        if (cursor.moveToFirst()){
            do {

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

                if (tmpTag.equals(tag)){
                    events.add(event);
                }

            }while (cursor.moveToNext());
        }
        cursor.close();

        return  events;
    }

    /**
     * @return 需要提醒的事件List
     */
    public static List<Event> getRemindEvents(){
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


            }while (cursor.moveToNext());
        }
        cursor.close();

        return remindEvents;
    }

    /**
     * @return 所有事件的List集合
     *
     */
    public static List<Event> queryEvents(){

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
}
