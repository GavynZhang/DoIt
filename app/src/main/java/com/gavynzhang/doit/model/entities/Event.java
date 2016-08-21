package com.gavynzhang.doit.model.entities;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by GavynZhang on 2016/8/17.
 */
public class Event extends BmobObject{

    private MyUser user;
    private String name;
    private Number mode;
    private BmobDate startTime;
    private BmobDate endTime;
    private Number pri;
    private BmobDate remindTime;
    private String address;
    private String remarks;
    private String tag;
    private Boolean isFinish;
    private Number tomatoNum;

    public Number getPri() {
        return pri;
    }

    public void setPri(Number pri) {
        this.pri = pri;
    }

    public Number getMode() {
        return mode;
    }

    public void setMode(Number mode) {
        this.mode = mode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BmobDate getEndTime() {
        return endTime;
    }

    public void setEndTime(BmobDate endTime) {
        this.endTime = endTime;
    }

    public Boolean getFinish() {
        return isFinish;
    }

    public void setFinish(Boolean finish) {
        isFinish = finish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BmobDate getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(BmobDate remindTime) {
        this.remindTime = remindTime;
    }

    public BmobDate getStartTime() {
        return startTime;
    }

    public void setStartTime(BmobDate startTime) {
        this.startTime = startTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Number getTomatoNum() {
        return tomatoNum;
    }

    public void setTomatoNum(Number tomatoNum) {
        this.tomatoNum = tomatoNum;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
