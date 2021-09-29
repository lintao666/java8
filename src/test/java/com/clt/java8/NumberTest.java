package com.clt.java8;

import org.junit.Test;

/**
 * @date 2019/4/29 23:29
 */
public class NumberTest {
    //  x<y  <==> >0
    @Test
    public void test0(){
        //处理无符号数？
        System.out.println(Integer.compareUnsigned(10,10));//0
        System.out.println(Integer.compareUnsigned(-10,10));//1
        System.out.println(Integer.compareUnsigned(10,-10));//-1
        System.out.println(Integer.compareUnsigned(10,-100));//-1
        System.out.println(Integer.compareUnsigned(-10,100));//1
        System.out.println(Integer.compareUnsigned(-100,10));//1

        //与上面的相反 x>y <==> >0
        System.out.println(Integer.compare(-100,10));//-1
        System.out.println(Integer.compare(100,10));//1
    }
}
