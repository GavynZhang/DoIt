package com.gavynzhang.doit.app.state;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.gavynzhang.doit.model.db.MyDatabaseHelper;
import com.gavynzhang.doit.model.entities.Event;
import com.gavynzhang.doit.utils.LogUtils;

/**
 * Created by GavynZhang on 2016/8/17 15:19.
 */
public class LogoutState implements UserState {

    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(MyApplication.getContext(), "LocalEvent.db", null, 1);

    @Override
    public void saveEventData(Event event) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put("name", event.getName());
            values.put("startTime", event.getStartTime().getDate());

            LogUtils.d("LogoutState", "startTime "+event.getStartTime().getDate());

            values.put("endTime", event.getEndTime().getDate());
            values.put("pri", event.getPri().intValue());
            values.put("mode", event.getMode().intValue());
            values.put("remindTime", event.getRemindTime().getDate());
            values.put("address", event.getAddress());
            values.put("remarks", event.getRemarks());
            values.put("tag", event.getTag());
//            values.put("isFinish", event.getFinish());
            db.insert("Event", null, values);

            Toast.makeText(MyApplication.getContext(),event.getRemindTime().getDate(), Toast.LENGTH_SHORT).show();

            values.clear();

        }catch (Exception e){
            Toast.makeText(MyApplication.getContext(), "Save Fail!!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public Event getEventData() {
        return null;
    }
}
