package com.clt.java8;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @date 2019/4/27 15:08
 */
public class OtherTest {

    @Test
    public void test0(){
    }

    @Test
    public void test1(){
        File file=new File("C:\\Users\\lin\\Downloads");
        List<String> list=Arrays.stream(file.listFiles())
                .sorted(Comparator.comparing(File::lastModified).reversed())
                .map(f->f.getName()+new Date(file.lastModified()))
                .limit(5)
                .collect(Collectors.toList());
        list.forEach(System.out::println);
    }

    @Test
    public void test2(){
        File file=new File("C:\\Users\\lin\\Downloads");
        if(file.isDirectory()) {
            List<File> list = Arrays.stream(file.listFiles())
                    .sorted(Comparator.comparing(File::lastModified).reversed())
                    //.map(f->f.getName()+f.lastModified())
                    .limit(5)
                    .collect(Collectors.toList());
            list.forEach(System.out::println);
        }
    }

}
