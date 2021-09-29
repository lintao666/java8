package com.clt.java8.nio;

import org.junit.Test;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;

import static java.nio.file.Files.newByteChannel;

/**
 * @date 2019/5/15 18:32
 */
public class NioTest {
    private Path filePath= Paths.get("C:\\Users\\lin\\Desktop\\profession\\架构设计\\消息队列");
    private Path vedioPath= Paths.get("C:\\Users\\lin\\Videos\\干煸长豆角.mp4");

    /**
     *  test Path
     */
    @Test
    public void test0(){

        Path filePath2= Paths.get("");
        Path filePath3= FileSystems.getDefault().getPath("C:\\Users\\lin\\Desktop\\profession\\架构设计\\消息队列");
        System.out.println(FileSystems.getDefault());
        System.out.println(filePath);
        System.out.println("目录层数："+filePath.getNameCount());
        System.out.println(filePath.getName(0));
        System.out.println(filePath.getFileName());//文件/目录名
        System.out.println(filePath.getRoot());//根路径

        System.out.println(filePath2);
        System.out.println(filePath2.isAbsolute());//是否为相对路径
        //将相对路径解析为绝对路径
        System.out.println(filePath2.resolve(Paths.get("")));
        filePath2.toAbsolutePath();//返回绝对路径

    }

    /**
     * test Files 用于操作文件或目录的工具类
     */
    @Test
    public void test1() throws IOException {
        //目录的大小为0
        long size=Files.size(filePath);
        //是目录
        boolean isDir=Files.isDirectory(filePath);
        //存在
        boolean exists=Files.exists(filePath);
        //探测文件内容类型
        String contentType=Files.probeContentType(vedioPath);

        //获取与指定文件的通道
        SeekableByteChannel seekableByteChannel= Files.newByteChannel(vedioPath);

        //打开path指定的目录
        DirectoryStream directoryStream=Files.newDirectoryStream(filePath);
        System.out.println(contentType);
    }

    /**
     * test pipe 管道是两个线程之间的单向数据连接。
     *  pipe有一个source通道，一个sink通道，数据会被写到sink通道，从source通道读取。
     */
    @Test
    public void test2()throws IOException{
        String str="测试数据";
        //管道创建
        Pipe pipe=Pipe.open();
        //向管道写数据
        Pipe.SinkChannel sinkChannel=pipe.sink();
        //通过SinkChannel的write()方法写数据
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        byteBuffer.clear();
        byteBuffer.put(str.getBytes());
        byteBuffer.flip();//position置为0
        while (byteBuffer.hasRemaining()){
            sinkChannel.write(byteBuffer);
        }


        //从管道读取数据
        Pipe.SourceChannel sourceChannel=pipe.source();
        //通过SourceChannel的read()方法读取数据
        sourceChannel.read(byteBuffer);

    }
}
