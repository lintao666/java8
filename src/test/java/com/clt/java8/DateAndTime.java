package com.clt.java8;

import org.junit.Test;

import java.io.DataInput;
import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.time.zone.ZoneRules;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @date 2019/4/28 15:58
 */
public class DateAndTime {

    /**
     * LocalDate是一个不可变对象，它只提供了简单的日期，并不含当天的时间信息。也不附带任何与时区相关的信息
     */
    @Test
    public void test0() {
        //1.从系统时钟中获取当前的日期
        LocalDate toDay = LocalDate.now();
        //2.
        //LocalDate date=LocalDate.of(2013,1,1);
        //3.
        LocalDate date = LocalDate.parse("2013-10-11");
        int year = date.getYear();
        Month month = date.getMonth();
        int dayOfMonth = date.getDayOfMonth();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int monthLen = date.lengthOfMonth();
        boolean leap = date.isLeapYear();
        System.out.println(year);//2013
        System.out.println(month);//JANUARY
        System.out.println(dayOfMonth);//1
        System.out.println(dayOfWeek);//TUESDAY
        System.out.println(monthLen);//31
        System.out.println(leap);//false

        //操纵时间
        LocalDate date4 = date.withYear(1995);
        LocalDate date5 = date.with(ChronoField.DAY_OF_WEEK, 1);
        System.out.println(date4);
        System.out.println(date5);

        LocalDate date6 = date5.minusYears(1);
        System.out.println(date6);
        LocalDate date7 = date5.plusMonths(3);
        System.out.println(date7);//2014-01-07

        LocalDate date8 = date7.with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
        LocalDate date9 = date7.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate date10 = LocalDate.parse("2017-01-05").with(TemporalAdjusters.previousOrSame(DayOfWeek.THURSDAY));
        LocalDate date11 = LocalDate.parse("2017-01-05").with(TemporalAdjusters.previous(DayOfWeek.THURSDAY));
        //下一个工作日
        LocalDate date12 = LocalDate.parse("2017-01-05").with(temporal -> {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
            if (dow == DayOfWeek.SUNDAY) dayToAdd = 3;
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        });
        System.out.println(date8);
        System.out.println(date9);
        System.out.println(date10);//2017-01-05
        System.out.println(date11);//2016-12-29
        System.out.println(date12);//2017-01-06
    }

    @Test
    public void test1() {
        LocalTime time = LocalTime.now();
        LocalTime time1 = LocalTime.of(10, 12);
        LocalTime time2 = LocalTime.of(13, 12, 50);
        LocalTime time3 = LocalTime.parse("23:00:01");
        System.out.println(time);
        System.out.println(time1);
        System.out.println(time2);
        System.out.println(time3);
    }

    /**
     * LocalDateTime 表示时间和日期，不带有时区信息
     */
    @Test
    public void test2() {

    }

    /**
     * 机器的日期和时间格式 （UTC时区）
     * Instant
     */
    @Test
    public void test3() {
        Instant instant0 = Instant.ofEpochSecond(3);
        Instant instant1 = Instant.ofEpochSecond(3, 0);
        Instant instant2 = Instant.ofEpochSecond(2, 1_000_000_000);
        Instant instant3 = Instant.ofEpochSecond(4, -1_000_000_000);
        Instant instant4 = Instant.now();
        System.out.println(instant0);
        System.out.println(instant1);
        System.out.println(instant2);
        System.out.println(instant3);
        System.out.println(instant4);
    }

    /**
     * Duration 和 Period
     * <p>
     * Duration用于以秒和纳秒衡量时间的长短.
     * <p>
     * Period 以年、月、日的方式对多个时间建模.
     */
    @Test
    public void test4() {
        //Temporal接口定义了如何读取和操纵为时间建模的对象的值

        //duration
        LocalTime time0 = LocalTime.of(10, 11);
        LocalTime time1 = LocalTime.parse("11:00");
        Duration duration = Duration.between(time0, time1);
        Duration threeMinutes = Duration.ofMinutes(3);
        Duration threeMinutes1 = Duration.of(3, ChronoUnit.MINUTES);
        System.out.println(duration.toMinutes());
        System.out.println(duration.toMillis());
        System.out.println(threeMinutes);
        System.out.println(threeMinutes1);

        //period
        LocalDate date0 = LocalDate.parse("2018-01-01");
        LocalDate date1 = LocalDate.parse("2017-01-01");
        Period period = Period.between(date0, date1);
        Period tenDays = Period.ofDays(10);
        Period period1 = Period.of(2, 3, 4);
        System.out.println(period);
        System.out.println(tenDays);
        System.out.println(period1);
    }

    /**
     * 时间日期格式化
     */
    @Test
    public void test5() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate date0 = LocalDate.parse("20140101", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate date1 = LocalDate.parse("11/1/2014", dateTimeFormatter);
        String date2 = LocalDate.now().format(dateTimeFormatter);
        String date3 = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println(date0);
        System.out.println(date1);
        System.out.println(date2);
        System.out.println(date3);

        //创建一个本地化的DateTimeFormat
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.US);//June 1(st), 1976
        System.out.println(date0.format(formatter1));
    }

    /**
     * 处理不同的时区和历法
     * ZoneId代替TimeZone 每个特定的ZoneId对象都由一个地区ID标识。 地区ID都为“{区域}/{城市}”.
     * <p>
     * ZoneRules包含了40个时区
     * 夏令时（DST）
     */
    @Test
    public void test6() {
        //处理夏令时（DST）
        //ZoneRules
        //获取指定时区的规则。 每个特定的ZoneId对象都由一个地区ID标识。 地区ID都为“{区域}/{城市}”
        ZoneId romeZone = ZoneId.of("Europe/Rome");//罗马
        System.out.println(romeZone);

        //将一个老的时区对象转换为ZoneId
        ZoneId zoneId = TimeZone.getDefault().toZoneId();
        System.out.println(TimeZone.getDefault());
        System.out.println(zoneId);

        LocalDate date = LocalDate.of(2015, 11, 11);
        //将LocalDate与ZoneId整合为 ZonedDateTime对象代表相对于指定时区的时间点
        ZonedDateTime zdt0 = date.atStartOfDay(romeZone);
        System.out.println(zdt0);

        LocalDateTime dateTime = LocalDateTime.now();
        ZonedDateTime zdt1 = dateTime.atZone(romeZone);
        ZonedDateTime zdt2 = dateTime.atZone(zoneId);
        System.out.println(zdt1);
        System.out.println(zdt2);

        Instant instant=Instant.now();
        ZonedDateTime zdt3=instant.atZone(zoneId);
        System.out.println(zdt3);

        //Instant instant1=dateTime.toInstant(ZoneOffset.UTC);
        //LocalDateTime dateTime1=LocalDateTime.ofInstant(instant,zoneId);

        //利用当前时区和UTC/格林尼治的固定偏差 表达时区 例如(纽约落后与伦敦5小时)  ZoneOffset
        ZoneOffset newYorkOffset=ZoneOffset.of("-05:00");
        LocalDateTime dateTime2=LocalDateTime.now();
        //OffsetDateTime使用ISO-8601的历法系统，以相对于UTC/格林尼治时间的偏差方式表示日期时间
        OffsetDateTime dateTimeInNewYork=dateTime2.atOffset(newYorkOffset);
        System.out.println(dateTimeInNewYork);
    }

    /**
     * 使用其他日历系统（non-ISO calendaring）
     */
    @Test
    public  void test7(){
        Chronology japaneseChronology=Chronology.ofLocale(Locale.JAPAN);
        ChronoLocalDate japanNow=japaneseChronology.dateNow();
        System.out.println(japanNow);
    }
}
