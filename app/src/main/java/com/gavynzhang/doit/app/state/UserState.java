package com.gavynzhang.doit.app.state;

import com.gavynzhang.doit.model.entities.Event;
import com.gavynzhang.doit.model.entities.MyUser;

import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by GavynZhang on 2016/8/17.
 */
public interface UserState {

    /**
     * 用户状态
     * */

    public void saveEventData(Event event);


}

