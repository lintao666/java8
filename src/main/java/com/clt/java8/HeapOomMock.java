package com.clt.java8;

import java.util.LinkedList;
import java.util.List;

/**
 * @date 2019/5/28 10:23
 */
public class HeapOomMock {
    public static void main(String[] args) {
        List<byte []> list=new LinkedList<>();
        int i=0;
        boolean flag=true;
        while (flag){
            try {
                i++;
                list.add(new byte[1024*1024]);//1MB
            } catch (Throwable e) {
                flag=false;
                System.out.println("count:"+i);
                e.printStackTrace();
            }

        }
    }
}
