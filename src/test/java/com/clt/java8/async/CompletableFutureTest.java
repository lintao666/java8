package com.clt.java8.async;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.*;


/**
 * 线程
 *
 * 普通线程：当一个普通线程在执行时，Java程序无法终止或者退出，所以最后剩下的那个线程会由于一直等待无法发生的事件而引发问题。
 *
 * 守护线程：程序退出时它也会被回收。
 *
 * @date 2019/4/27 13:22
 */

/**
 * 1.如何提供异步API
 * 2.如何让使用了同步API的代码变为非阻塞代码。使用 流水线将两个接续的异步操作合并为一个异步计算操作。
 * 3.如何以响应式的方式处理异步操作的完成事件。
 */
public class CompletableFutureTest {

    private ThreadLocal<Long> start=new ThreadLocal<>();

    @Before
    public void before(){
        start.set(System.nanoTime());
    }
    @After
    public void after(){
        System.out.println("Done in: "+(System.nanoTime()-start.get())/1_000_000);
    }

    //1009777298   10位
    @Test
    public void test0(){
        Shop shop=new Shop("BestPrice");
        shop.getPrice("iphone");
    }
    //105
    //1110
    //使用普通CompletableFuture
    @Test
    public void test1(){
        Shop shop=new Shop("shop2");
        Future<Double> futurePrice=shop.getPriceAsync("name");
        System.out.println("Invacation returned after: "+(System.nanoTime()-start.get())/1_000_000);
        //执行更多任务
        doSomethingElse();
        try {
            double price=futurePrice.get();
            System.out.printf("Price is %.2f%n",price);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //4101
    //测试原生顺序执行且会发生阻塞
    @Test
    public void test2(){
        QueryClientTest qc=new QueryClientTest();
        List<String> price=qc.findPrices("iphone");
        System.out.println(price);
    }


    /**
     * CompletableFuture VS 并行流 parallelStream
     *
     * 1.它们内部采用的是同样的通用线程池，默认都使用固定数目的线程。
     *  具体线程数取决于Runtime.getRuntime().availableProcessors();
     * 2.但是，ComputureFuture允许你对执行器（Executor）进行配置，尤其是线程池的大小，让它以更适合应用需求的方式进行配置。
     *
     * 总结：
     *  如果是 计算密集型的操作，并且没有I/O 推荐使用Stream(实现简单，效率可能是最高（没有必要创建比处理器核数更多的线程）)
     *  反之，并行的工作单元涉及等待I/O的操作（包括网络连接等待），使用CompletableFuture灵活性更好。还有处理流的流水线中如果发生I/O等待，
     *      流的延迟特性会让我们很难判断到底什么时候触发了等待
     */
    //1100  4个商店
    //2108  5个商店
    //3135  9个商店
    //测试并行流
    @Test
    public void test3(){
        QueryClientTest qc=new QueryClientTest();
        List<String> price=qc.findPricesParallel("iphone");
        System.out.println(price);
    }

    //2184 4个商店
    //2112 5个商店
    //3149 9个商店
    //使用CompletableFuture的supplyAsync
    @Test
    public void test4(){
        QueryClientTest qc=new QueryClientTest();
        List<String> price=qc.findPricesByCompletableFuture("iphone");
        System.out.println(price);
    }

    //1123 9个商店
    //使用CompletableFuture的supplyAsync +定制的执行器
    @Test
    public void test5(){
        QueryClientTest qc=new QueryClientTest();
        List<String> price=qc.findPricesByCompletableFutureAndExecutor("iphone");
        System.out.println(price);
    }

    private void doSomethingElse() {
        try {
            Thread.sleep(800L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void test6(){
        //获取通用线程池
        int threadsNum=Runtime.getRuntime().availableProcessors();
        System.out.println(threadsNum);//4
    }

    /**
     *  对多个异步任务进行流水线操作的
     *
     *  1.thenCompose 方法允许对两个 异步操作进行流水线，第一个操作完成时，将其结果作为参数传递给第二个操作。
     *      stream.map(future->future.thenCompose(obj -> CompletableFuture.supplyAsync(()->method(),executor)))
     *
     *  2.thenCombine 需要将两个完全不相干的ComputureFuture对象的结果整合起来，也不希望等到第一个任务完全结束才开始第二个任务。
     *      使用thenCombine方法 ComputureFuture.supplyAsync(task1).thenCombine(ComputureFuture.supplyAsync(task2),(price,rate)->price*rate);
     *
     */

    /**
     * 响应CompletableFuture的completion事件
     *  1.thenAccept 定义如何处理CompletableFuture的返回结果 接收CompletableFuture执行完毕后的返回值作为参数。
     *      返回一个CompletableFuture<Void>对象。
     *  2.ComletureFuture.allOf(CompletureFuture[]);  数组中的所有对象执行完毕
     *
     *  3.ComletureFuture.anyOf(CompletureFuture[]);  返回由第一个执行完毕的返回值构成的CompletureFuture<Object>.
     */
}
