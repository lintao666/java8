package com.clt.java8.nio;

import org.junit.Test;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @date 2019/5/25 17:11
 * @Since java1.7
 * 7中Buffer boolean除外
 * ByteBuffer
 * CharBuffer
 * ShortBuffer
 * IntBuffer
 * LongBuffer
 * FloatBuffer
 * DoubleBuffer
 * 缓冲区的四个核心属性：
 * capacity:容量
 * limit：界限
 * position：位置
 * mark： 标记
 *
 *
 * 直接缓冲区与非直接缓冲区：
 *  非直接缓冲区：allocate()方法分配，将缓冲区建立在JVM的内存中。
 *  直接缓冲区：allocateDirect()方法分配，将缓冲区建立在物理内存中.FileChannel.map()
 *      直接缓冲区主要分配给那些易受基础系统的本机I/O操作影响的大型，持久的缓冲区。
 *      一般情况下，最好仅在直接缓冲区能在程序性能方面带来明显好处时分配它们.
 */
public class BufferTest {

    /**
     * byte char String
     */
    @Test
    public void test0() {
        //unicode(java的字符表示)一个中文字符占两个字节   UTF-8一个中文字符占三个字节
        String str = "床前明月光";
        String str1 = "abcd3";
        System.out.println(str.getBytes().length);//15
        System.out.println(str1.getBytes().length);//5
        System.out.println("操作系统默认的字符编码：" + Charset.defaultCharset());
        System.out.println("操作系统默认的文件字符编码：" + System.getProperty("file.encoding"));
    }

    @Test
    public void test1() {
        String str = "abcdef";
        System.out.println("-------------allocate()分配一个ByteBuffer------------------");
        ByteBuffer buf = ByteBuffer.allocate(1024);
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        System.out.println("-------------put()存入数据------------------");
        buf.put(str.getBytes());
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //Flips this buffer.  The limit is set to the current position and then
        //      the position is set to zero.  If the mark is defined then it is
        //     discarded.
        System.out.println("------------flip()-------------------");
        buf.flip();
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        System.out.println("------------get()-------------------");
        byte[] res=new byte[str.length()];
        buf.get(res);
        System.out.println(new String(res));
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        // The position is set to zero and the mark is
        //     * discarded.
        System.out.println("------------rewind() -------------------");
        buf.rewind();
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //clear()清空缓冲区，但是缓冲区的数据依然存在，但是处于“被遗忘"状态
        System.out.println("------------clear() -------------------");
        buf.clear();
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        System.out.println("------------mark()标记-----------------------------");
        buf.mark();
        System.out.println("------------恢复到mark标记-----------------------------");
        buf.reset();

        if(buf.hasRemaining()){
            //Returns the number of elements between the current position and the limit.
            buf.remaining();
        }

    }
}
