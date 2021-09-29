package com.clt.java8;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @date 2019/4/24 23:18
 */
public class StreamTest {

    /**
     * 给定列表[1,2,3],[3,4]并返回[(1,3)(1,4)(2,3)(2,4)(3,3)(3,4)]
     */
    @Test
    public void test1() {
        List<Integer> a = Arrays.asList(1, 2, 3);
        List<Integer> b = Arrays.asList(3, 4);
        List<int[]> c = a.stream().flatMap(i -> b.stream().map(j -> new int[]{i, j})).collect(toList());
        System.out.print("[");
        c.forEach(i -> System.out.print("(" + i[0] + "," + i[1] + ")"));
        System.out.print("]");
    }

    /**
     * 给定列表[1,2,3],[3,4]并返回总和能被3整除的数对[(2,4)(3,3)]
     */
    @Test
    public void test2() {
        List<Integer> a = Arrays.asList(1, 2, 3);
        List<Integer> b = Arrays.asList(3, 4);
        List<int[]> c = a.stream().flatMap(i -> b.stream()
                .filter(j -> (j + i) % 3 == 0)
                .map(j -> new int[]{i, j}))
                //.filter(i -> i[0]+i[1]==6)
                .collect(toList());
        System.out.print("[");
        c.forEach(i -> System.out.print("(" + i[0] + "," + i[1] + ")"));
        System.out.print("]");
    }

    /**
     * 勾股数 a^2+b^2=c^2
     */
    @Test
    public void test3() {
        Stream<double[]> pythagoreanTriples2 =
                IntStream.rangeClosed(1, 100).boxed()
                        .flatMap(i ->
                                IntStream.rangeClosed(i, 100)
                                        .mapToObj(j -> new double[]{i, j, Math.sqrt(i * i + j * j)})
                                        .filter(z -> z[2] % 1 == 0));//.forEach(w-> System.out.println(w[0]+","+w[1]+","+w[2]))
        pythagoreanTriples2.forEach(w ->
                System.out.println((int) w[0] + "," + (int) w[1] + "," + (int) w[2]));

    }

    /**
     * 给定一个列表{1,4,9} 构造一个List<List<Integer>>它的成员都是类表{1,4,9}的子集 {1,4,9},{1,4},{}...
     */
    @Test
    public void test4() {
        List<Integer> list = Arrays.asList(1, 4, 9, 12);
        List<List<Integer>> result = subSets(list);
        result.forEach(System.out::println);
        TestCase.assertTrue(result.size()==Math.pow(2,list.size()));
    }

    /**
     * 求给定list的所有子集
     * @param list
     * @return
     */
    private List<List<Integer>> subSets(List<Integer> list) {
        if (list.isEmpty()) {
            List<List<Integer>> result = new ArrayList<>();
            result.add(list);
            return result;
        }
        //包含第1个
        int first=list.get(0);
        //不包含第1个
        List<Integer> subList = list.subList(1, list.size());

        List<List<Integer>> subans= subSets(subList);
        List<List<Integer>> subSet = insertAll(first, subans);

        return concat(subans,subSet);
    }

    private List<List<Integer>> insertAll(Integer value,List<List<Integer>> list) {
        List<List<Integer>> result=new ArrayList<>();
        List<Integer> temp;
        for(int i=0;i<list.size();i++){
            temp=new ArrayList<>();
            temp.add(value);
            temp.addAll(list.get(i));
            result.add(temp);
        }
        return result;
    }
    //纯粹的函数式
    private List<List<Integer>> concat(List<List<Integer>> list1,List<List<Integer>> list2){
        List<List<Integer>> result=new ArrayList<>(list1);
        result.addAll(list2);
        return result;
    }

}
