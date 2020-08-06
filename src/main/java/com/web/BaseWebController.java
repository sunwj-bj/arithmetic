package com.web;

import com.alibaba.fastjson.JSONObject;
import com.web.validator.UserInfoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author sunwj
 */
@RestController
@RequestMapping("/anti-fraud")
public class BaseWebController {
    Logger logger = LoggerFactory.getLogger(BaseWebController.class);

    @RequestMapping(value = "/verify/checkFaceCache", method = RequestMethod.GET)
    public String test(@Validated UserInfoValidator userInfo, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            logger.info("入参错误：" + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("httpCode","200");
        map.put("errorCode","");
        map.put("errorDesc","");
        map.put("data","false");
        map.put("realName",userInfo.getRealName());
        map.put("phone",userInfo.getPhone());
        return JSONObject.toJSONString(map);
    }

}
