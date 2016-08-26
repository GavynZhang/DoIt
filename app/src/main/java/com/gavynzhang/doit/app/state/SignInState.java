package com.gavynzhang.doit.app.state;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.gavynzhang.doit.model.db.MyDatabaseHelper;
import com.gavynzhang.doit.model.entities.Event;
import com.gavynzhang.doit.model.entities.MyUser;
import com.gavynzhang.doit.model.entities.Tomato;
import com.gavynzhang.doit.utils.LogUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by GavynZhang on 2016/8/17 15:15.
 */
public class SignInState implements UserState {

    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(MyApplication.getContext(),"LocalEvent.db", null, 1);

    private MyUser getUser(){
        MyUser user = MyUser.getCurrentUser(MyUser.class);
        return user;
    }

    @Override
    public void saveEventData(Event event) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put("username", getUser().getUsername());
            values.put("name", event.getName());
            values.put("startTime", event.getStartTime().getDate());
            values.put("endTime", event.getEndTime().getDate());
            values.put("pri", event.getPri().intValue());
            values.put("mode", event.getMode().intValue());
            values.put("remindTime", event.getRemindTime().getDate());
            values.put("address", event.getAddress());
            values.put("remarks", event.getRemarks());
            values.put("tag", event.getTag());
            values.put("isFinish", event.getIsFinish().intValue());
            db.insert("Event", null, values);
            LogUtils.i("SignInState","Save to local Ok: "+" username: "+getUser().getUsername()+" eventName: "+event.getName());

        }catch (Exception e){
            LogUtils.w("SignInState", "Save Fail!!!"+ e.toString());
            e.printStackTrace();
        }

        event.setUser(getUser());
        event.setUserName(getUser().getUsername());
        event.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    Toast.makeText(MyApplication.getContext(),"Save to bmob OK!!!", Toast.LENGTH_SHORT).show();
                }else {
                    LogUtils.w("SignInState", e.toString());
                }
            }
        });
    }

    @Override
    public Event getEventData() {
        return null;
    }

    @Override
    public void saveTomatoData(Tomato tomato) {

    }
}
