package com.clt.java8;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

/**
 * @date 2019/4/29 18:42
 */
public class StringTest {
    @Rule
    public final StandardOutputStreamLog log=new StandardOutputStreamLog();
    //连接多个字符串
    @Test
    public void test0(){
        String authors=String.join(", ","张三","李四","王麻子","老王");
        System.out.println(authors);
    }

    @Test
    public void test1(){
        String s="abc";
        Assert.assertEquals("abcdef",s.concat("def"));
        Assert.assertEquals("defabc","def".concat(s));
    }

    @Test
    public void test2(){
        System.out.println("test");
        Assert.assertEquals("test\r\n",log.getLog());
    }

}
