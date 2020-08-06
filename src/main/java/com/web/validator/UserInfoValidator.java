package com.web.validator;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author sunwj
 */
public class UserInfoValidator {
    @NotEmpty(message = "真实姓名不能为空")
    private String realName;

    @Pattern(regexp = "^1[3-9][0-9]\\d{8}$", message = "手机号码格式不正确")
    private String phone;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
