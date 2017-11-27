package com.example.protocol;

import framework.transaction2.GenericRequest2;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/26
 *  @描述：    TODO
 */
public class LoginRobotRequest extends GenericRequest2<LoginRobotResponse>{

    public LoginRobotRequest() {
        super(LoginRobotResponse.class);
    }
    @Parameter
    public String username;
    @Parameter
    public String password;
    @Parameter
    public String platform = "android";

}
