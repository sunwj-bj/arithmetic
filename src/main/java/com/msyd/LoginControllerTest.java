package com.msyd;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * @author sunwj
 */
public class LoginControllerTest extends BaseControllerTest{
    @Before
    public void login() {
        this.setRequestUrl("/login/userLogin");
        this.addParam("userName", "18710121576");
        this.addParam("password", "qwer1234");
        this.addParam("sourceId", "XIAOMI-123456");
        this.addParam("sourceName", "XIAOMI-Android-4");

        String response = this.doRequest();
        JSONObject json = JSONObject.parseObject(response);
        String token = json.getJSONObject("cd").getString("token");
        this.addParam("token", token);
    }

    @Test
    public void getLoanListTest() {
        this.setRequestUrl("/myAccount/accountMessage");
        String response = this.doRequest();
        System.out.print(response);
    }

}
