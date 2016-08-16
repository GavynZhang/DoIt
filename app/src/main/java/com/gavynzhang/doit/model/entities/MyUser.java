package com.gavynzhang.doit.model.entities;

import cn.bmob.v3.BmobUser;

/**
 * Created by GavynZhang on 2016/8/17.
 */
public class MyUser extends BmobUser {

    private Integer workDuration;
    private Integer restDuration;

    public Integer getRestDuration() {
        return restDuration;
    }

    public void setRestDuration(Integer restDuration) {
        this.restDuration = restDuration;
    }

    public Integer getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(Integer workDuration) {
        this.workDuration = workDuration;
    }
}
