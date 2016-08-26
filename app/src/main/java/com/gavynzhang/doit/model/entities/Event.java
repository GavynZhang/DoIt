package com.gavynzhang.doit.model.entities;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by GavynZhang on 2016/8/17.
 */
public class Event extends BmobObject implements Serializable{

    private Number id;
    private String userName;
    private MyUser user;
    private String name;
    private Number mode;
    private BmobDate startTime;
    private Number startTimeMillSeconds;
    private BmobDate endTime;
    private Number endTimeMillSeconds;
    private Number pri;
    private BmobDate remindTime;
    private String address;
    private String remarks;
    private String tag;
    private Number isFinish;
    private Number tomatoNum;

    public Number getId() {
        return id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    public Number getEndTimeMillSeconds() {
        return endTimeMillSeconds;
    }

    public void setEndTimeMillSeconds(Number endTimeMillSeconds) {
        this.endTimeMillSeconds = endTimeMillSeconds;
    }

    public Number getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(Number isFinish) {
        this.isFinish = isFinish;
    }

    public Number getStartTimeMillSeconds() {
        return startTimeMillSeconds;
    }

    public void setStartTimeMillSeconds(Number startTimeMillSeconds) {
        this.startTimeMillSeconds = startTimeMillSeconds;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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
