package com.gavynzhang.doit.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by GavynZhang on 2016/8/17.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper{

    public static final String CREATE_NORMAL_EVENT = "create table normalEvent(" +
            "id integer primary key autoincrement," +
            "username text, " +
            "eventName, text" +
            "eventStartTime text," +
            "eventEndTime text," +
            "eventAddress text," +
            "eventRemindTime text" +
            "eventRemarks text)";

    public static final String CREATE_DEADLINE_EVENT = "create table deadlineEvent(" +
            "id integer primary key autoincrement," +
            "username text," +
            "eventName text," +
            "deadline text," +
            "eventId integer," +
            "isFinish integer," +
            "eventRemarks text)";

    public static final String CREATE_TOMATO = "create table tomato(" +
            "id integer primary key autoincrement," +
            "username text," +
            "startTime text," +
            "isBroken integer," +
            "eventId integer)";

    private Context mContext;

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
