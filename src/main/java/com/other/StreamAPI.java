package com.other;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunwj
 */
public class StreamAPI {
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
