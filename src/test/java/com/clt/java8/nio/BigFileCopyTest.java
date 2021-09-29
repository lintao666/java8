package com.clt.java8.nio;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

/**
 * @date 2019/5/27 23:35
 * <p>
 * 多线程复制大文件 3G+
 * 将文件n等分，分成n部分，交由n个线程去复制。
 * 在进行文件写入时按照分块写入，所以必须规定fromFile(源文件),toFile(目标文件),start(起始位置),end(结束位置)
 */
public class BigFileCopyTest {

    private long start;
    private long end;

    public String sourceFilePath = "H:\\迅雷下载\\CentOS-7-x86_64-DVD-1708.iso";
    public String targetFilePath = "H:\\temp\\log\\xxabc.iso";
    public int times = 100;

    //多线程复制大文件

    public static void main(String[] args) {
        BigFileCopyTest test = new BigFileCopyTest();
        test.test();
    }


    public void test() {
        System.out.println("-----------------------开始复制-------------------------");
        start = System.currentTimeMillis();

        //1.分块读入  InputStream.skip()
        //2.分块写入 RandomAccessFile.seek()
        File sourceFile = new File(sourceFilePath);
        File targetFile = new File(targetFilePath);
        //sourceFileSize=nMb*times+yushu
        long sourceFileSize = sourceFile.length();
        long nMb = sourceFileSize / times;
        long yushu = sourceFileSize % times;
        for (int i = 0; i <= times; i++) {
            new Thread(new FileThread(sourceFile, targetFile, i * nMb, i * nMb + nMb)).start();
        }

        System.out.println("-----------------------复制完成-------------------------");
        end = System.currentTimeMillis();
        System.out.println("耗费时间为：" + (end - start) + "ms");
    }


    class FileThread implements Runnable {
        private File sourceFile;
        private File targetFile;
        private long start;
        private long end;

        public FileThread(File sourceFile, File targetFile, long start, long end) {
            this.sourceFile = sourceFile;
            this.targetFile = targetFile;
            this.start = start;
            this.end = end;
        }

        public void run() {
            int size = 1024 * 1024;//1MB
            long count = end - start;
            byte[] dst = new byte[size];

            try (BufferedInputStream sourcebis = new BufferedInputStream(new FileInputStream(sourceFile));
                 RandomAccessFile targetraf = new RandomAccessFile(targetFile, "rw")
            ) {
                sourcebis.skip(start);
                targetraf.seek(start);
                for (int i = 0; i <= count / size; i++) {//1MB*100
                    //1.读取文件块
                    sourcebis.read(dst);
                    //1.写入文件块
                    targetraf.write(dst, 0, dst.length);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

