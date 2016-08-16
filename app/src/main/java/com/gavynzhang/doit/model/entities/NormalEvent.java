package com.gavynzhang.doit.model.entities;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by GavynZhang on 2016/8/17.
 */
public class NormalEvent extends BmobObject{

    private MyUser user;
    private String eventName;
    private BmobDate eventStartTime;
    private BmobDate eventEndTime;
    private String eventAddress;
    private BmobDate eventRemindTime;
    private String eventRemarks;

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public BmobDate getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(BmobDate eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventRemarks() {
        return eventRemarks;
    }

    public void setEventRemarks(String eventRemarks) {
        this.eventRemarks = eventRemarks;
    }

    public BmobDate getEventRemindTime() {
        return eventRemindTime;
    }

    public void setEventRemindTime(BmobDate eventRemindTime) {
        this.eventRemindTime = eventRemindTime;
    }

    public BmobDate getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(BmobDate eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
