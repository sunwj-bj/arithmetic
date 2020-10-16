package com.other;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

/**
 * @author sunwj
 *
 * LocalDate、LocalTime、LocalDateTime、Instant为不可变对象，
 * 修改这些对象对象会返回一个副本
 */
public class LocalDateTimeTest {

    @Test
    public void testDateTime(){
        //获取当前年月日
        LocalDate localDate = LocalDate.now();
        //获取当前时分秒
        LocalTime localTime = LocalTime.now();
        //创建LocalDateTime对象
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.of(2019, Month.SEPTEMBER, 10, 14, 46, 56);
        LocalDateTime localDateTime2 = LocalDateTime.of(localDate, localTime);
        LocalDateTime localDateTime3 = localDate.atTime(localTime);
        LocalDateTime localDateTime4 = localTime.atDate(localDate);
        //从localDateTime获取LocalDate、LocalTime
        LocalDate localDate2 = localDateTime.toLocalDate();
        LocalTime localTime2 = localDateTime.toLocalTime();

        //Instant操作，获取毫秒和秒
        Instant instant = Instant.now();
        long currentSecond = instant.getEpochSecond();
        //和System.currentTimeMillis()输出的一样
        long currentMilli = instant.toEpochMilli();
        System.out.println("秒："+currentSecond);
        System.out.println("毫秒："+currentMilli);

        /**
         * 增加、减少年数、月数、天数等
         */
        //增加一年
        localDateTime = localDateTime.plusYears(1);
        localDateTime = localDateTime.plus(1, ChronoUnit.YEARS);
        //减少一个月
        localDateTime = localDateTime.minusMonths(1);
        localDateTime = localDateTime.minus(1, ChronoUnit.MONTHS);
        //修改年为2019
        localDateTime = localDateTime.withYear(2020);
        //修改为2022
        localDateTime = localDateTime.with(ChronoField.YEAR, 2022);

        //比如有些时候想知道这个月的最后一天是几号、下个周末是几号，通过提供的时间和日期API可以很快得到答案
        LocalDate localDate3 = LocalDate.now();
        LocalDate localDate1 = localDate3.with(firstDayOfYear());

        //格式化时间
        DateTimeFormatter dateTimeFormatter =   DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String s3 = localDate.format(dateTimeFormatter);
        //解析时间
        LocalDate localDate4 = LocalDate.parse("20190910", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate localDate5 = LocalDate.parse("2019-09-10", DateTimeFormatter.ISO_LOCAL_DATE);






    }

}
