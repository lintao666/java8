package com.clt.java8;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * @date 2019/4/26 11:00
 */
public class OptionalTest {
    /**
     * 用Optional封装可能为null的值
     */
    @Test
    public void test0(){
        Map<String,Object> map=new HashMap<>();
        Optional<Object> value=Optional.ofNullable(map.get("key"));
    }

    /**
     * 异常与Optional的对比
     */
    @Test
    public void test1(){
        Optional<Integer> intValue=stringToInt("aasdf");
    }

    public static Optional<Integer> stringToInt(String s){
        try{//如果String可以转换为对应的Integer
            return Optional.of(Integer.parseInt(s));
        }catch (NumberFormatException e){//不能转换返回一个空的Optional对象
            return Optional.empty();
        }
    }

    /**
     *
     */
    @Test
    public void test2(){
        Properties props=new Properties();
        props.setProperty("a","5");
        props.setProperty("b","true");
        props.setProperty("c","-3");
        TestCase.assertEquals(5,readDuration(props,"a"));
        TestCase.assertEquals(0,readDuration(props,"b"));
        TestCase.assertEquals(0,readDuration(props,"c"));
        TestCase.assertEquals(0,readDuration(props,"d"));

    }

    /**
     * 从属性中获取一个值，该值是以秒为单位计量的一段时间。一段时间必须为正数
     * @param properties
     * @param name
     * @return
     */
    private int readDuration(Properties properties, String name){
        Optional<Integer> res=Optional.ofNullable(properties.getProperty(name))
                .flatMap(OptionalTest::stringToInt)
                .filter(i->i>0);
        return res.orElse(0);
    }

}
