package com.clt.java8.nio;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Set;
import java.util.SortedMap;

/**
 * @date 2019/5/27 14:22
 */
public class CharsetTest {

    @Test
    public void test0(){
        String csn = Charset.defaultCharset().name();
        System.out.println(csn);
        SortedMap<String,Charset> charMap=Charset.availableCharsets();
        Set<String> keys=charMap.keySet();
        for (String str:keys
             ) {
            System.out.println(str+"=="+charMap.get(str));
        }
    }
}
