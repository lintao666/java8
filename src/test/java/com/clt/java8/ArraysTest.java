package com.clt.java8;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.IntConsumer;

/**
 * @date 2019/4/30 11:29
 */
public class ArraysTest {

    //Arrays.parallelSort -->以并发的方式对指定的数组进行排序（自然顺序，Comparator）
    @Test
    public void test0() {
        int[] a = {12, 54, 8749, 833, 45, 47, 65, 749, 34};
        //Arrays.sort(a);
        Arrays.parallelSort(a);//正序
        Integer[] b = {12, 54, 8749, 833, 45, 47, 65, 749, 34};
        Arrays.parallelSort(b, Comparator.reverseOrder());//反序
    }

    //setAll和 parallelSetAll ->以顺序/并发的方式 使用提供的函数计算每一个元素的值
    @Test
    public void test1() {
        int[] a = {12, 54, 8749, 833, 45, 47, 65, 749, 34};
        //接受元素的索引,返回处理后的值（a[]发生改变）
        Arrays.setAll(a, i -> ++a[i]);

        //函数需要无任何副作用
        int[] b = {12, 54, 8749, 833, 45, 47, 65, 749, 34};
        Arrays.parallelSetAll(b,i->b[i]+2);
    }
    //parallelPrefix ->以并发的方式用给定的二进制操作符对给定数组中的每个元素进行积累计算。
    @Test
    public void test2(){
        int[] a = {12, 54, 8749, 833, 45, 47, 65, 749, 34};
        //Arrays.fill(a,2);
        //第一个元素不变（从第二个元素开始），第二个元素==第一个元素+（原来第二个元素），第三个元素==第二个元素+（原来第三个元素）
        Arrays.parallelPrefix(a,(left, right) -> {
            System.out.println("第一个left:"+left);
            System.out.println(right);
            return left+right;
        });
    }

    @Test
    public void testArrayMethodReference(){
        Function<Integer,String[]> fun= x ->new String[x];
        String[] str=fun.apply(3);
        System.out.println(str.length);
        Function<Integer,String[]> fun2=String[]::new;
        String[] str2=fun.apply(4);
        System.out.println(str2.length);
    }

    @Test
    public void testSuperMethodReference(){

    }

}
