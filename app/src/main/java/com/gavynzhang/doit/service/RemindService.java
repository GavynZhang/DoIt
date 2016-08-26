package com.gavynzhang.doit.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.gavynzhang.doit.R;
import com.gavynzhang.doit.app.state.MyApplication;
import com.gavynzhang.doit.model.entities.Event;
import com.gavynzhang.doit.receiver.AlarmReceiver;
import com.gavynzhang.doit.utils.GetEvents;
import com.gavynzhang.doit.utils.LogUtils;
import com.gavynzhang.doit.utils.TimeUtils;

import java.util.List;

/**
 * Created by GavynZhang on 2016/8/26 9:45.
 */
public class RemindService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, final int startId) {
        LogUtils.d("RemindService","onStartCommand");
        final String eventName = intent.getStringExtra("eventName");
        final String eventRemarks = intent.getStringExtra("eventRemarks");
        final long eventRemindTimeMills = intent.getLongExtra("eventRemindTimeMills",System.currentTimeMillis());
        new Thread(new Runnable() {
            @Override
            public void run() {
                //弹出通知
                NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                //Notification notification = new Notification(R.drawable.finish, eventName, eventRemindTimeMills);
                //notification.

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getContext());
//                builder.setContentIntent();
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.finish));
                builder.setContentTitle(eventName);
                builder.setContentText(eventRemarks);
                manager.notify(1, builder.build());
            }
        }).start();

        List<Event> remindEvents = GetEvents.getRemindEvents();
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        for (Event event : remindEvents){
            long triggerAtTime = TimeUtils.string2Milliseconds(event.getRemindTime().getDate());
            Intent i = new Intent(this, AlarmReceiver.class);
            i.putExtra("eventName", event.getName());
            i.putExtra("eventRemarks", event.getRemarks());
            i.putExtra("eventRemindTimeMills", triggerAtTime);
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
            manager.set(AlarmManager.RTC_WAKEUP, triggerAtTime, pi);
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
