package com.gavynzhang.doit.app.state;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gavynzhang.doit.model.db.MyDatabaseHelper;
import com.gavynzhang.doit.model.entities.Event;

/**
 * Created by GavynZhang on 2016/8/17 15:19.
 */
public class LogoutState implements UserState {

    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(MyApplication.getContext(), "LocalEvent.db", null, 1);

    @Override
    public void saveEventData(Event event) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
    }
}
