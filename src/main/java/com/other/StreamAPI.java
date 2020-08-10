package com.other;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sunwj
 */
public class StreamAPI {
    @Test
    public void listByGroup(){
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        ArrayList<Map<String, Object>> newMaps = new ArrayList<>();
        HashMap<String, Object> map1 = new HashMap<>();
        HashMap<String, Object> map2 = new HashMap<>();
        HashMap<String, Object> map3 = new HashMap<>();
        HashMap<String, Object> map4 = new HashMap<>();
        map1.put("bankCardNo","6254526544556236");
        map1.put("amount",new BigDecimal("50"));
        map2.put("bankCardNo","6254526644556237");
        map2.put("amount",new BigDecimal("50"));
        map3.put("bankCardNo","6254526544559233");
        map3.put("amount",new BigDecimal("50"));
        map4.put("bankCardNo","6254526544556236");
        map4.put("amount",new BigDecimal("50"));
        maps.add(map1);
        maps.add(map2);
        maps.add(map3);
        maps.add(map4);
        System.out.println("合并前："+JSON.toJSONString(maps));
        collect(maps, newMaps);
        System.out.println("合并后："+JSON.toJSONString(newMaps));
    }

    private void collect(ArrayList<Map<String, Object>> maps, ArrayList<Map<String, Object>> newMaps) {
        for (Map<String,Object> tempMap1:maps){
            boolean state = false;
            for (Map<String,Object> tempMap2:newMaps){
                if (tempMap2.get("bankCardNo").equals(tempMap1.get("bankCardNo"))){
                    BigDecimal amount1 = (BigDecimal) tempMap1.get("amount");
                    BigDecimal amount2 = (BigDecimal) tempMap2.get("amount");
                    tempMap2.put("amount",amount1.add(amount2));
                    state=true;
                }
            }
            if (!state){
                newMaps.add(tempMap1);
            }
        }
    }

    @Test
    public void test(){
        List<String> list = new ArrayList<>();
        list.add("array");
        list.add("mack");
        list.add(null);
        try {
            list.stream().forEach(s -> compare(s));
        }catch (Exception e){
            System.out.println("出现异常："+e.getMessage());
        }
    }
    private void compare(String s){
        System.out.println(s.equals("john"));
    }
}
