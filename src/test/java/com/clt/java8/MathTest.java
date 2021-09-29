package com.clt.java8;

import org.junit.Test;

import java.util.Map;

/**
 * @date 2019/4/29 22:40
 */
public class MathTest {
    //Math.addExact加,Math.subtractExact减,multipleExact乘,incrementExact +1,decrementExact -1,negateExact 取反,
    @Test
    public void test0(){
        int a=(int)(Math.pow(2,31)-1);//32位-1位（0符号位）2147483647
        int b=1;
        System.out.println(a);
        System.out.println(a+b);//溢出，计算错误
        System.out.println(Math.addExact(a,b));//溢出，抛出异常
    }
    @Test
    public void test1(){
        int a=(int)(Math.pow(2,31)*-1);//-2147483648
        int b=1;
        System.out.println(a);
        System.out.println(a-b);//溢出，计算错误
        System.out.println(Math.subtractExact(a,b));//溢出，抛出异常
    }
    @Test
    public void test2(){
        int i=Math.incrementExact(2);//+1
        int i1=Math.decrementExact(2);//-1
        int i2=Math.negateExact(10);//取反
        System.out.println(i);
        System.out.println(i1);
        System.out.println(i2);
    }
    @Test
    public void test3(){
        int i0=Math.floorMod(10,3);//取模
        int i1=Math.floorDiv(11,3);//除法(没有余数)
        double i2=Math.nextDown(3.0);//相邻的浮点数 负无穷
        double i3=Math.nextUp(3.0);//相邻的浮点数 正无穷
        long l=(long)Math.pow(2,31)-1;//64/2  2147483647
        int i4= Math.toIntExact(l+1);//long转换成int 溢出，抛出异常
        System.out.println(i0);
        System.out.println(i1);
        System.out.println(i2);
        System.out.println(i3);
        System.out.println(i4);
    }

    @Test
    public void test4(){
        System.out.println((int)(Math.pow(2,31)-1));
        System.out.println((long)Math.pow(2,31)-1);
    }
}
