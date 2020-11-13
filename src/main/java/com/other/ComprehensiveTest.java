package com.other;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author sunwj
 * 综合验证测试类
 */
public class ComprehensiveTest {
    public static void main(String[] args) {
        int i=1;
        for (int j = 0; j <5 ; j++) {
            System.out.println(++i);
        }
    }

    @Test
    public void testDate() throws ParseException {
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        Date parse = yyyyMMdd.parse("20201112");
        System.out.println(parse);
        Calendar c = Calendar.getInstance();
        c.setTime(parse);
        System.out.println(c.getTime());


    }
}
