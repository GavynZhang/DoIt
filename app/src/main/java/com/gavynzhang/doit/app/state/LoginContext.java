package com.gavynzhang.doit.app.state;

/**
 * Created by GavynZhang on 2016/8/17.
 */
public class LoginContext {

    //用户状态，默认为未登录状态
    UserState mState = new LogoutState();

    static LoginContext sLoginContext = new LoginContext();

    //单例
    private LoginContext(){

    }

    public static LoginContext getLoginContext(){
        return sLoginContext;
    }

    public void setState(UserState state){
        mState = state;
    }

}
