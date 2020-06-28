package com.msyd;

import com.alibaba.fastjson.JSONObject;
import com.msyd.base.BaseControllerTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author sunwj
 */
public class LoginControllerTest extends BaseControllerTest {
    @Before
    public void login() {
        this.setRequestUrl("/login/userLogin");
        this.addParam("userName", "15622222222");
        this.addParam("password", "1qaz2wsx");
        this.addParam("sourceId", "XIAOMI-123456");
        this.addParam("sourceName", "XIAOMI-Android-4");

        String response = this.doRequest();
        JSONObject json = JSONObject.parseObject(response);
        System.out.println("登录接口返回信息========>"+json);
        String token = json.getJSONObject("cd").getString("token");
        this.addParam("token", token);
    }

    @Test
    public void getAccountMessage() {
        this.setRequestUrl("/myAccount/accountMessage");
        String response = this.doRequest();
        System.out.print("获取账户信息接口返回信息："+response);
    }

}
