package com.gavynzhang.doit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gavynzhang.doit.service.RemindService;

/**
 * Created by GavynZhang on 2016/8/26 21:47.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String eventName = intent.getStringExtra("eventName");
        String eventRemarks = intent.getStringExtra("eventRemarks");

        Intent i = new Intent(context, RemindService.class);
        i.putExtra("eventName", eventName);
        i.putExtra("eventRemarks", eventRemarks);
        context.startService(i);
    }
}
