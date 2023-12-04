package com.clt.java8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * flatMap: 流的扁平化： 将多个流合并成一个流
 */
public class FlatMapExample {
    /**
     * 问题： 对于一张单词表，如何返回一张列表，列出里面各不相同的 字符呢
     */
    public static void flatMapTest() {
        String[] words = {"Hello", "World"};
        List<String> collect = Arrays.stream(words).map(word -> word.split("")).flatMap(Arrays::stream).distinct().collect(Collectors.toList());
        System.out.println(collect);
    }

    public static void main(String[] args) {
        FlatMapExample.flatMapTest();
    }
}
