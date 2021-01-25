package com.other;

import com.msyd.base.StringUtil;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

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
        Date date = new Date(1610627927);
        System.out.println(date);
    }

    /**
     * 测试正则表达式
     */
    @Test
    public void testPattern(){
        String content = "234234234234";
        System.out.println(Pattern.matches("^[0-9]*$",content));
    }

    /**
    * 二进制计算
    * @author      sunwenjie
    * @return
    * @exception
    * @date        2021/1/14 21:03
    * @Version:    1.0
    */
    @Test
    public void binaryCompute(){
        //对象初始化boolean值默认为false
        BooleanFlag booleanFlag = new BooleanFlag();
        System.out.println(1<<9);
        System.out.println(booleanFlag.isFlag());
    }

    class BooleanFlag{
        boolean flag;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }

   /**
     * 测试正则表达式
     */
    @Test
    public void testNum(){
        String content = "80.10";
        System.out.println(StringUtil.isNumeric(content));
        System.out.println(Double.valueOf(content).intValue());
    }

}
