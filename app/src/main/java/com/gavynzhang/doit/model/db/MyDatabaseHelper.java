package com.gavynzhang.doit.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by GavynZhang on 2016/8/17.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper{

    /**
     * username :所属用户
     * eventName :事件名称
     * eventStartTime: 事件开始时间
     * eventEndTime: 事件结束时间
     * eventAddress: 事件发生地址
     * eventRemindTime: 事件提醒时间
     * eventRemarks: 事件备注
     * */
    public static final String CREATE_NORMAL_EVENT = "create table normalEvent(" +
            "id integer primary key autoincrement," +
            "username text, " +
            "eventName, text" +
            "eventStartTime text," +
            "eventEndTime text," +
            "eventAddress text," +
            "eventRemindTime text" +
            "eventRemarks text)";

    /**
     * username: 所属用户
     * eventName: 事件名称
     * deadline: 事件截止日期
     * eventId: 事件对应Id
     * isFinish: 事件是否完成
     * eventRemarks: 事件备注
     * */
    public static final String CREATE_DEADLINE_EVENT = "create table deadlineEvent(" +
            "id integer primary key autoincrement," +
            "username text," +
            "eventName text," +
            "deadline text," +
            "eventId integer," +
            "isFinish integer," +
            "eventRemarks text)";

    /**
     * username: 所属用户
     * startTime: 番茄开始时间
     * isBroken: 番茄是否完成
     * eventId: 番茄所对应的事件
     * */
    public static final String CREATE_TOMATO = "create table tomato(" +
            "id integer primary key autoincrement," +
            "username text," +
            "startTime text," +
            "isBroken integer," +
            "eventId integer)";

    private Context mContext;

    /**
     * @param context ：容器
     * @param name :数据库名
     * @param factory :返回自定义的Cursor
     * @param version :当前数据库的版本号
     * */
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NORMAL_EVENT);
        db.execSQL(CREATE_DEADLINE_EVENT);
        db.execSQL(CREATE_TOMATO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
