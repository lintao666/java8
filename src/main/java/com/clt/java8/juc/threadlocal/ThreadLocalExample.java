package com.clt.java8.juc.threadlocal;

import java.util.Map;

public class ThreadLocalExample {
    public static void main(String[] args) {
        testThreadLocalInitValue();
        testThreadLocalInitValue2();
        testThreadLocalInitValue3();
    }

    public static void testThreadLocalInitValue() {
        final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();
        Map<String, Object> map = threadLocal.get();
        System.out.println(map);//null
        System.out.println(map.entrySet());//NullPointerException
    }

    public static void testThreadLocalInitValue2() {
        final ThreadLocal<String> threadLocal = new ThreadLocal<String>() {
            @Override
            protected String initialValue() {
                return "thread local init value";
            }
        };
        String str = threadLocal.get();
        System.out.println(str);//thread local init value!
    }

    public static void testThreadLocalInitValue3() {
        final ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "thread local init value!");
        String str = threadLocal.get();
        System.out.println(str);//thread local init value!
    }

}
