package com.clt.java8.async;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;

/**
 *
 * java5引入
 * Future接口的局限性
 * 很难表述Future结果之间的依赖性
 * 比如：
 *  1.将两个异步计算合并为一个（两个异步计算相互独立，同时第二个又依赖于第一个的结果）
 *  2.等待Future集合中所有任务都完成
 *  3.仅等待Future集合中最快的任务完成（有可能因为它们试图通过不同的方式计算同一个值），并返回结果
 *  4.通过编程方式完成一个Future任务的执行。（即以手工设定异步操作结果的方式）
 *  5.应对Future的完成事件（即当Future的完成事件发生时会收到通知，并能使用Future计算的结果进行下一步的操作，不只是简单的阻塞等待操作的结果）
 */

/**
 * 线程池大小的优化
 * 1.如果线程池中的线程的数量过多，最终它们会竞争稀缺的CPU和内存资源，浪费大量的时间在上下文切换上。
 * 2.反之，如果线程的数目过少，处理器的一些核可能就无法充分利用。
 * <p>
 * 线程池大小与处理器的利用率之比可以使用下面的公式进行估算
 * <p>
 * N(threads)=N(cpu)*U(cpu)*(1+W/C)
 * <p>
 * N(cpu)是CPU的核的数目
 * U(cpu)是期望的CPU的利用率(该值应该介于0和1之间)
 * W/C是等待时间与计算时间的比率
 * <p>
 * 逻辑CPU：操作系统可以使用逻辑CPU来模拟出真实CPU的效果。
 * 当CPU没有开启超线程时，逻辑CPU的个数就是CPU的个数
 * 当超线程开启后，逻辑CPU的个数是核数的两倍，即Linux的/proc/cpuinfo中processor的数量
 *
 * @date 2019/4/27 10:57
 */
public class FutureTest {

    private ThreadLocal<Long> startTime=new ThreadLocal<>();

    public static void main(String[] args) {
        //CPU的核的数目 貌似==CPU的逻辑处理器个数
        int i = Runtime.getRuntime().availableProcessors();
        System.out.println(i);
    }

    @Test
    public void test0() {
        //1.创建ExecutorService，通过它你可以向线程池提交任务
        ExecutorService executorService = Executors.newCachedThreadPool();
        //2.向ExecutorService提交一个Callable对象
        Future<Double> future = executorService.submit(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return doSomeLongComputation();//以异步方式在新的线程中执行耗时的操作
            }
        });
        doSomethingElse();//异步操作进行的同时，可以做其他事情
        try {
            Double result = future.get(1, TimeUnit.SECONDS);//获取异步操作的结果，如果没有得到结果，最多等待1秒钟后退出
        } catch (InterruptedException e) {
            //当前线程在等待过程中被中断
            e.printStackTrace();
        } catch (ExecutionException e) {
            //计算抛出一个异常
            e.printStackTrace();
        } catch (TimeoutException e) {
            //在Future对象完成之间超时
            e.printStackTrace();
        }
    }

    private void doSomethingElse() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Double doSomeLongComputation() throws InterruptedException {
        Thread.sleep(2000L);//毫秒
        return 100.0;
    }

    @Before
    public void before(){
        startTime.set(System.currentTimeMillis());
    }
    @After
    public void after(){
        System.out.println("SPEND TIME:"+(System.currentTimeMillis()-startTime.get()));
    }


}
