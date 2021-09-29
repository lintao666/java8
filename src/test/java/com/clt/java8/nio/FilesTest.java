package com.clt.java8.nio;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * @date 2019/4/29 19:04
 */
public class FilesTest {
    //Files.lines
    @Test
    public void test0() {
        File file = new File("C:\\Users\\lin\\Downloads\\bankflow.jsp");
        try {
            Files.lines(file.toPath()).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取目录中的文件(不是递归)，处理内容非常庞大的目录时非常有用 Files.list
    @Test
    public void test1() {
        File file = new File("C:\\Users\\lin\\Downloads");
        try {
            long count = Files.list(file.toPath())
                    .count();
            Files.list(file.toPath()).forEach(System.out::println);
            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Files.walk (递归获取目录下的所有文件，深度优先，可以设定递归深度)
    @Test
    public void test2() {
        File file = new File("C:\\Users\\lin\\Downloads");
        try {
            long count = Files.walk(file.toPath(), FileVisitOption.FOLLOW_LINKS).count();//434
            long count2 = Files.walk(file.toPath(), 1, FileVisitOption.FOLLOW_LINKS).count();//36
            Files.walk(file.toPath(), 1, FileVisitOption.FOLLOW_LINKS).forEach(System.out::println);
            System.out.println(count);
            System.out.println(count2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Files.find (通过递归地遍历一个目录找到符合条件的条目，并生成一个Stream<Path>)
    @Test
    public void test3() {
        File file = new File("C:\\Users\\lin\\Downloads");
        File file2 = new File("server.js");
        BasicFileAttributes basicFileAttributes = null;
        BiPredicate<Path, BasicFileAttributes> biPredicate = (path, basicFileAttributes1) ->
                path.endsWith(file2.getName()) && basicFileAttributes1.isRegularFile();
        try {
            long count = Files.find(file.toPath(), 2, biPredicate, FileVisitOption.FOLLOW_LINKS).count();
            Files.find(file.toPath(), 2, biPredicate, FileVisitOption.FOLLOW_LINKS).forEach(System.out::println);
            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //操作系统
    @Test
    public void testCopyBigFile0() {
        String sourceFilePath = "H:\\迅雷下载\\CentOS-7-x86_64-DVD-1708.iso";
        String targetFilePath = "H:\\temp\\log\\a.iso";
        try {
            Files.copy(Paths.get(sourceFilePath), Paths.get(targetFilePath), StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //159799ms
    @Test
    public void testCopyBigFile1() {
        String sourceFilePath = "H:\\迅雷下载\\CentOS-7-x86_64-DVD-1708.iso";
        String targetFilePath = "H:\\temp\\log\\b.iso";

        try (//1.获取通道
             FileChannel sourceFc = FileChannel.open(Paths.get(sourceFilePath), StandardOpenOption.READ);
             FileChannel targetFc = FileChannel.open(Paths.get(targetFilePath), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        ) {
            //2.分配多个指定大小的缓冲区
            ByteBuffer bf0 = ByteBuffer.allocate(1024 * 1024 * 20);//20MB
            ByteBuffer bf1 = ByteBuffer.allocate(1024 * 1024 * 20);//
            ByteBuffer bf2 = ByteBuffer.allocate(1024 * 1024 * 20);//
            ByteBuffer bf3 = ByteBuffer.allocate(1024 * 1024 * 20);//
            ByteBuffer bf4 = ByteBuffer.allocate(1024 * 1024 * 10);//
            ByteBuffer[] bfs = {bf0, bf1, bf2, bf3, bf4};
            //3.分散读取
            while (sourceFc.read(bfs) != -1) {
                //4.聚集写入
                bf0.flip();
                bf1.flip();
                bf2.flip();
                bf3.flip();
                bf4.flip();
                targetFc.write(bfs);
                bf0.clear();
                bf1.clear();
                bf2.clear();
                bf3.clear();
                bf4.clear();
            /*for (ByteBuffer bf:bfs){
                bf.flip();
                //System.out.println(new String(bf.array(),0,bf.limit()));
                bf.clear();
            }*/
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //通道+非直接缓冲区170172ms
    @Test
    public void testCopyBigFile2() {
        String sourceFilePath = "H:\\迅雷下载\\CentOS-7-x86_64-DVD-1708.iso";
        String targetFilePath = "H:\\temp\\log\\db.iso";

        try (//1.获取通道
             FileChannel sourceFc = FileChannel.open(Paths.get(sourceFilePath), StandardOpenOption.READ);
             FileChannel targetFc = FileChannel.open(Paths.get(targetFilePath), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        ) {
            //2.分配多个指定大小的缓冲区
            ByteBuffer bf0 = ByteBuffer.allocate(1024 * 1024 * 5);//10MB
            ByteBuffer bf1 = ByteBuffer.allocate(1024 * 1024 * 5);//
            ByteBuffer bf2 = ByteBuffer.allocate(1024 * 1024 * 5);//
            ByteBuffer bf3 = ByteBuffer.allocate(1024 * 1024 * 5);//
            ByteBuffer bf4 = ByteBuffer.allocate(1024 * 1024 * 5);//
            ByteBuffer bf5 = ByteBuffer.allocate(1024 * 1024 * 5);//
            ByteBuffer bf6 = ByteBuffer.allocate(1024 * 1024 * 5);//
            ByteBuffer bf7 = ByteBuffer.allocate(1024 * 1024 * 5);//
            ByteBuffer bf8 = ByteBuffer.allocate(1024 * 1024 * 5);//
            ByteBuffer[] bfs = {bf0, bf1, bf2, bf3, bf4};
            //3.分散读取
            while (sourceFc.read(bfs) != -1) {
                //4.聚集写入
                bf0.flip();
                bf1.flip();
                bf2.flip();
                bf3.flip();
                bf4.flip();
                bf5.flip();
                bf6.flip();
                bf7.flip();
                bf8.flip();
                targetFc.write(bfs);
                bf0.clear();
                bf1.clear();
                bf2.clear();
                bf3.clear();
                bf4.clear();
                bf5.clear();
                bf6.clear();
                bf7.clear();
                bf8.clear();
            /*for (ByteBuffer bf:bfs){
                bf.flip();
                //System.out.println(new String(bf.array(),0,bf.limit()));
                bf.clear();
            }*/
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //连接通道155121ms
    @Test
    public void testCopyBigFile3() {
        String sourceFilePath = "H:\\迅雷下载\\CentOS-7-x86_64-DVD-1708.iso";
        String targetFilePath = "H:\\temp\\log\\xyz.iso";

        try (//1.获取文件通道
             FileChannel sourceChannel = FileChannel.open(Paths.get(sourceFilePath), StandardOpenOption.READ);
             FileChannel targetChannel = FileChannel.open(Paths.get(targetFilePath), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        ) {
            //2.连接两个通道，并且从sourceChannel通道读取，然后写入targetChannel通道
            //long count=1024*1024*1024;
            System.out.println("文件大小："+sourceChannel.size()+"byte");
            System.out.println("文件大小："+sourceChannel.size()/1024/1024/1024+"GB");
            //sourceChannel.transferTo(0,sourceChannel.size(),targetChannel);
            targetChannel.transferFrom(sourceChannel,0,sourceChannel.size());
            System.out.println("目标文件大小："+targetChannel.size());

        }catch (IOException e){
            e.printStackTrace();
        }

    }


    //带缓存的IO流 151664ms
    @Test
    public void testCopyBigFile4() throws IOException {
        String sourceFilePath = "H:\\迅雷下载\\CentOS-7-x86_64-DVD-1708.iso";
        String targetFilePath = "H:\\temp\\log\\axcsd.iso";

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFilePath), 40 * 1024 * 1024);//40MB
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFilePath), 40 * 1024 * 1024);) {
            byte[] dst = new byte[1024];//1KB
            while (bis.read(dst) != -1) {
                bos.write(dst);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long start;
    private long end;

    @Before
    public void before() {
        System.out.println("-----------------------开始复制-------------------------");
        start = System.currentTimeMillis();
    }

    @After
    public void end() {
        System.out.println("-----------------------复制完成-------------------------");
        end = System.currentTimeMillis();
        System.out.println("耗费时间为：" + (end - start) + "ms");
    }
}
