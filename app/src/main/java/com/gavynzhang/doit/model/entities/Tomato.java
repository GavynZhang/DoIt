package com.gavynzhang.doit.model.entities;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by GavynZhang on 2016/8/17.
 */
public class Tomato extends BmobObject{

    private Event event;
    private BmobDate startTime;
    private Boolean isBroken;
    private String brokenReason;

    public String getBrokenReason() {
        return brokenReason;
    }

    public void setBrokenReason(String brokenReason) {
        this.brokenReason = brokenReason;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Boolean getBroken() {
        return isBroken;
    }

    public void setBroken(Boolean broken) {
        isBroken = broken;
    }

    public BmobDate getStartTime() {
        return startTime;
    }

    public void setStartTime(BmobDate startTime) {
        this.startTime = startTime;
    }
}
