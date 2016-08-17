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
     * name :事件名称
     * startTime: 事件开始时间
     * endTime: 事件结束时间
     * remindTime: 事件提醒时间
     * address: 事件发生地址
     * remarks: 事件备注
     * tag: 事件标签
     * isFinish: 是否完成
     * tomatoNum: 事件所用番茄钟
     * */
    public static final String CREATE_EVENT = "create table event(" +
            "id integer primary key autoincrement," +
            "username text, " +
            "name, text" +
            "startTime text," +
            "endTime text," +
            "remindTime text" +
            "address text," +
            "remarks text," +
            "tag text," +
            "isFinish integer)";

//    /**
//     * username: 所属用户
//     * eventName: 事件名称
//     * deadline: 事件截止日期
//     * eventId: 事件对应Id
//     * isFinish: 事件是否完成
//     * eventRemarks: 事件备注
//     * eventTag: 事件标签
//     * */
//    public static final String CREATE_DEADLINE_EVENT = "create table deadlineEvent(" +
//            "id integer primary key autoincrement," +
//            "username text," +
//            "eventName text," +
//            "deadline text," +
//            "eventId integer," +
//            "isFinish integer," +
//            "eventRemarks text," +
//            "eventTag text)";

    /**
     * eventId: 番茄所对应的事件
     * startTime: 番茄开始时间
     * isBroken: 番茄是否完成
     * */
    public static final String CREATE_TOMATO = "create table tomato(" +
            "id integer primary key autoincrement," +
            "eventId text," +
            "startTime text," +
            "isBroken integer," +
            "brokenReason text)" ;

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
        db.execSQL(CREATE_EVENT);
        db.execSQL(CREATE_TOMATO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
