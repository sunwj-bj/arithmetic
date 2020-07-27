package com.web;

import com.alibaba.fastjson.JSONObject;
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

    @RequestMapping(value = "/verify/checkFaceCache", method = RequestMethod.GET)
    public String test(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("httpCode","200");
        map.put("errorCode","");
        map.put("errorDesc","");
        map.put("data","false");
        return JSONObject.toJSONString(map);
    }

}
