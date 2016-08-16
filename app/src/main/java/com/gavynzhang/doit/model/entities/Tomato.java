package com.gavynzhang.doit.model.entities;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by GavynZhang on 2016/8/17.
 */
public class Tomato extends BmobObject{

    private MyUser user;
    private BmobDate startTime;
    private Boolean isBroken;
    private DeadlineEvent eventBelongs;

    public DeadlineEvent getEventBelongs() {
        return eventBelongs;
    }

    public void setEventBelongs(DeadlineEvent eventBelongs) {
        this.eventBelongs = eventBelongs;
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

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
