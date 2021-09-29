package com.clt.java8.nio;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @date 2019/5/27 9:52
 * <p>
 * 通道(Channel)：用于源节点与目标节点的连接。 NIO中负责缓冲区数据的传输。
 * <p>
 * 通道的主要实现类
 * java.nio.channels.Channel 接口
 * |--FileChannel:
 * |--SocketChannel:
 * |--ServerSocketChannel:
 * |--DatagramChannel:
 * 获取通道：
 * 1. java针对支持通道的类提供了getChannel()方法
 * 本地IO:
 * FileInputStream/FileOutputStream
 * RandomAccessFile
 * 网络IO：
 * Socket
 * ServerSocket
 * DatagramSocket
 * 2.jdk1.7中的NIO2针对各个通道提供了静态方法open()
 * 3.jdk1.7中的NIO2的Files工具类的newByteChannel()
 * <p>
 * <p>
 * 通道之间的数据传输
 * transferFrom()
 * transferTo()
 * 分散(Scatter)和聚集(Gather)
 * 分散读取(Scattering Reads)：将通道中的数据分散到多个缓冲区中.
 * 聚集写入(Gathering Writes)：将多个缓冲区的数据聚集到通道中
 */
public class ChannelTest {
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
        System.out.println("耗费时间为：" + (end - start)+"ms");
    }

    /**
     * 使用通道+缓冲区复制文件(非直接缓冲区)
     */
    @Test
    public void testFileCopy() throws IOException {
        String sourceFilePath = "H:\\迅雷下载\\CentOS-7-x86_64-DVD-1708.iso";//4.21 GB (4,521,459,712 字节)
        String targetFilePath = "H:\\迅雷下载\\test.iso";

        try (
                FileInputStream fis = new FileInputStream(sourceFilePath);
                FileChannel infc = fis.getChannel();
                FileOutputStream fos = new FileOutputStream(targetFilePath);
                FileChannel outfc = fos.getChannel();
        ) {
            //分配缓冲区
            ByteBuffer dst = ByteBuffer.allocate(1024);
            //将通道中的数据存入缓冲区
            while (infc.read(dst) != -1) {
                dst.flip();//缓冲区切换为读模式 ,position =0
                //将缓冲区的数据写入通道
                outfc.write(dst);
                dst.clear();//清空缓存区，position=0,limit=0
            }
        }
    }


    /**
     * 使用通道+缓冲区复制文件(直接缓冲区[只支持byteBuffer])
     *
     * @throws IOException
     */
    @Test
    public void testFileCopy2() throws IOException {
        String sourceFilePath = "H:\\BaiduNetdiskDownload\\7. 尚硅谷_佟刚_JavaWEB案例_查看图书详细信息.zip";//4.21 GB (4,521,459,712 字节) GB-MB-KB-B(byte)
        String targetFilePath = "H:\\迅雷下载\\test.zip";

        try (
                //获取文件通道
                //ByteChannel infc=Files.newByteChannel(Paths.get(sourceFilePath));
                FileChannel infc = FileChannel.open(Paths.get(sourceFilePath));
                FileChannel outfc = FileChannel.open(Paths.get(targetFilePath), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        ) {
            //内存映射缓冲区(不能超过Integer.MAX_VALUE)
            //ByteBuffer dst=ByteBuffer.allocateDirect(1024);
            MappedByteBuffer inBuf = infc.map(FileChannel.MapMode.READ_ONLY, 0, infc.size());
            MappedByteBuffer outBuf = outfc.map(FileChannel.MapMode.READ_WRITE, 0, infc.size());
            //将通道中的数据存入缓冲区
            /*while (infc.read(dst) !=-1){
                dst.flip();//缓冲区切换为读模式 ,position =0
                //将缓冲区的数据写入通道
                outfc.write(dst);
                dst.clear();//清空缓存区，position=0,limit=0
            }*/
            byte[] dst = new byte[inBuf.limit()];
            inBuf.get(dst);
            outBuf.put(dst);
        }
    }


    //分散和聚集
    @Test
    public void testScatterAndGather() throws IOException {
        //String sourceFilePath = "C:\\Users\\lin\\Documents\\Tencent Files\\756447868\\FileRecv\\vietnam_importer_2017.csv";
        String sourceFilePath = "H:\\迅雷下载\\CentOS-7-x86_64-DVD-1708.iso";
        String targetFilePath = "H:\\test\\test.iso";
        //1.获取通道
        FileChannel sourceFc = FileChannel.open(Paths.get(sourceFilePath), StandardOpenOption.READ);
        FileChannel targetFc=FileChannel.open(Paths.get(targetFilePath),StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE_NEW);
        //2.分配多个指定大小的缓冲区
        ByteBuffer bf0 = ByteBuffer.allocate(1024*1024*10);//10MB
        ByteBuffer bf1 = ByteBuffer.allocate(1024*1024*10);//
        ByteBuffer bf2 = ByteBuffer.allocate(1024*1024*10);//
        ByteBuffer bf3 = ByteBuffer.allocate(1024*1024*10);//
        ByteBuffer bf4 = ByteBuffer.allocate(1024*1024*10);//
        ByteBuffer bf5 = ByteBuffer.allocate(1024*1024*10);//
        ByteBuffer[] bfs = {bf0, bf1,bf2,bf3,bf4,bf5};
        //3.分散读取
        while(sourceFc.read(bfs) !=-1){
            //4.聚集写入
            bf0.flip();
            bf1.flip();
            bf2.flip();
            bf3.flip();
            bf4.flip();
            bf5.flip();
            targetFc.write(bfs);
            bf0.clear();
            bf1.clear();
            bf2.clear();
            bf3.clear();
            bf4.clear();
            bf5.clear();
            /*for (ByteBuffer bf:bfs){
                bf.flip();
                //System.out.println(new String(bf.array(),0,bf.limit()));
                bf.clear();
            }*/
        }
        sourceFc.close();
        targetFc.close();
    }


    @Test
    public void testFile(){
        String sourceFilePath = "H:\\迅雷下载\\test2.txt";
        String targetFilePath = "H:\\迅雷下载\\result0.txt";
        int lineSize=100;
        try {
            long count=Files.lines(Paths.get(sourceFilePath)).count();//4430223
            for(int i=0;i<=count;i=i+lineSize){
                Files.lines(Paths.get(sourceFilePath)).skip(i).limit(lineSize).forEach(System.out::println);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        System.out.println(Integer.MAX_VALUE + "字节B");//2147483647
        System.out.println(Integer.MAX_VALUE / 1024 / 1024 / 1024 + "GB");
        System.out.println(1.04*1024/97.06 );
    }

}
