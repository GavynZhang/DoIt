package com.gavynzhang.doit.app.state;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.gavynzhang.doit.model.db.MyDatabaseHelper;
import com.gavynzhang.doit.model.entities.Event;
import com.gavynzhang.doit.model.entities.Tomato;
import com.gavynzhang.doit.service.RemindService;
import com.gavynzhang.doit.utils.LogUtils;

/**
 * Created by GavynZhang on 2016/8/17 15:19.
 */
public class LogoutState implements UserState {

    private MyDatabaseHelper dbHelper = MyDatabaseHelper.getMyDatabasesHelper();

    @Override
    public void saveEventData(Event event) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put("username", "local");
            values.put("name", event.getName());
            values.put("startTime", event.getStartTime().getDate());
            values.put("endTime", event.getEndTime().getDate());
            values.put("startTimeMillSeconds", event.getStartTimeMillSeconds().longValue());
            values.put("endTimeMillSeconds", event.getEndTimeMillSeconds().longValue());
            values.put("pri", event.getPri().intValue());
            values.put("mode", event.getMode().intValue());
            values.put("remindTime", event.getRemindTime().getDate());
            values.put("address", event.getAddress());
            values.put("remarks", event.getRemarks());
            values.put("tag", event.getTag());
            values.put("isFinish", event.getIsFinish().intValue());
            db.insert("Event", null, values);

            //Toast.makeText(MyApplication.getContext(),"startTime: "+event.getStartTime().getDate(), Toast.LENGTH_SHORT).show();

            values.clear();

        }catch (Exception e){
            Toast.makeText(MyApplication.getContext(), "Save Fail!!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

//        Intent intent = new Intent(MyApplication.getContext(), RemindService.class);
//        MyApplication.getContext().startService(intent);
    }

    @Override
    public Event getEventData() {
        return null;
    }

    @Override
    public void saveTomatoData(Tomato tomato) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();

        try{
            values.put("eventId", tomato.getEventId().intValue());
            values.put("startTime", tomato.getStartTime().getDate());
            values.put("isBroken", tomato.getIsBroken().intValue());
            values.put("brokenReason", tomato.getBrokenReason());

            db.insert("Tomato", null, values);
        }catch (Exception e){
            Toast.makeText(MyApplication.getContext(), "Save Tomato Fail!!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
