package com.gavynzhang.doit.model.entities;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by GavynZhang on 2016/8/17.
 */
public class Tomato extends BmobObject implements Serializable{

    private Number eventId;
    private BmobDate startTime;
    private Number isBroken;
    private String brokenReason;

    public String getBrokenReason() {
        return brokenReason;
    }

    public void setBrokenReason(String brokenReason) {
        this.brokenReason = brokenReason;
    }

    public Number getEventId() {
        return eventId;
    }

    public void setEventId(Number eventId) {
        this.eventId = eventId;
    }

    public Number getIsBroken() {
        return isBroken;
    }

    public void setIsBroken(Number isBroken) {
        this.isBroken = isBroken;
    }

    public BmobDate getStartTime() {
        return startTime;
    }

    public void setStartTime(BmobDate startTime) {
        this.startTime = startTime;
    }
}
