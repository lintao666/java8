package com.clt.java8.juc.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池传递线程上下文信息 工具类
 * 1. 通过TtlRunable.get()或者 TtlCallable.get()修饰用户自定义task。
 * 2. 还可以修饰线程池或者使用Java Agent修饰JDK线程池实现类的方式实现TTL功能
 * * 启动参数中需要加上 -javaagent:path/transmittable-thread-local-x.x.x.jar
 * * 例如：-javaagent:C:\Users\linta\.m2\repository\com\alibaba\transmittable-thread-local\2.14.3\transmittable-thread-local-2.14.3.jar
 *
 * 应用：
 * @see com.ruoyi.common.core.context.SecurityContextHolder.java
 */
public class TransmittableTreadLocalExample {
    public static void main(String[] args) {
        testInheritableThreadLocal();
        testInheritableThreadLocal2();
        testTransmiitableThreadLocal();
        testTransmiitableThreadLocal2();
        testTransmittableThreadLocal3();
    }

    /**
     * 线程池在submit任务时会伴随着（线程池工作）线程的创建，会继承当前线程的InhertableThreadLocal，所以子线程中可以获取到父线程中存的值。
     */
    public static void testInheritableThreadLocal() {
        final InheritableThreadLocal<String> parent = new InheritableThreadLocal<>();
        parent.set("InheritableThreadLocal的值");

        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> System.out.println(Thread.currentThread().getName() + "获取父线程InheritableThreadLocal中的值为:" + parent.get()));
        //输出结果为： pool-1-thread-1获取父线程InheritableThreadLocal中的值为:InheritableThreadLocal的值
    }


    /**
     * 因为创建线程时，当前线程并没有inhritableThreadLocals，所以线程池中的子线程无法获取父线程中的parent的值。
     * 这种场景下如何才能获取parent的值呢？TransmittableThreadLocal(TTL) 解决了这个问题
     */
    public static void testInheritableThreadLocal2() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> System.out.println("预先创建工作线程，然后再给 父线程设置值！"));
        final InheritableThreadLocal<String> parent = new InheritableThreadLocal<>();
        parent.set("InheritableThreadLocal的值");

        executor.submit(() -> System.out.println(Thread.currentThread().getName() + "获取父线程InheritableThreadLocal中的值为:" + parent.get()));
        //输出结果为： pool-1-thread-1获取父线程InheritableThreadLocal中的值为:null
    }

    /**
     * TTL 测试
     * 使用TtlRunable.get()修饰 Task
     * 使用TtlCallable.get()修改Callable
     */
    public static void testTransmiitableThreadLocal() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> System.out.println("预先创建工作线程，然后再给 父线程设置值！"));
        //使用TTL
        final InheritableThreadLocal<String> parent = new TransmittableThreadLocal<>();
        parent.set("TransmittableThreadLocal的值");

        //将Runable接口通过TtlRunnable.get()封装， 或者TtlCallable.get()封装Callable接口
        executor.submit(TtlRunnable.get(() -> System.out.println(Thread.currentThread().getName() + "获取父线程TransmittableThreadLocal中的值为:" + parent.get())));
        //输出结果为： pool-1-thread-1获取父线程InheritableThreadLocal中的值为:TransmittableThreadLocal的值
    }

    /**
     * TTL 测试2
     * 修饰线程池省去每次修饰Runnable和Callable
     * * 使用TtlExecutors.getTtlExecutor(Executor)修饰 Executor接口
     * * 使用TtlExecutors.getTtlExecutorService(ExecutorService)修饰 ExecutorService接口
     * * 使用TtlExecutors.getTtlScheduledExecutorService(ScheduledExecutorService)修饰 ScheduledExecutorService接口
     */
    public static void testTransmiitableThreadLocal2() {
        ExecutorService originalExecutor = Executors.newFixedThreadPool(1);
        ExecutorService executor = TtlExecutors.getTtlExecutorService(originalExecutor);
        executor.submit(() -> System.out.println("预先创建工作线程，然后再给 父线程设置值！"));
        //使用TTL
        final InheritableThreadLocal<String> parent = new TransmittableThreadLocal<>();
        parent.set("TransmittableThreadLocal的值");

        executor.submit(() -> System.out.println(Thread.currentThread().getName() + "获取父线程TransmittableThreadLocal中的值为:" + parent.get()));
        //输出结果为： pool-1-thread-1获取父线程InheritableThreadLocal中的值为:TransmittableThreadLocal的值
    }


    /**
     * TTL 测试2
     * 使用Java Agent来修饰JDK线程池实现类。这种方式，实现线程池的传递是透明的。代码中没有修饰Runnable或是线程池的代码。
     * <p>
     * 启动参数中需要加上 -javaagent:path/transmittable-thread-local-x.x.x.jar
     * 本例中使用：-javaagent:C:\Users\linta\.m2\repository\com\alibaba\transmittable-thread-local\2.14.3\transmittable-thread-local-2.14.3.jar
     */
    public static void testTransmittableThreadLocal3() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> System.out.println("预先创建工作线程，然后再给 父线程设置值！"));
        //使用TTL
        final InheritableThreadLocal<String> parent = new TransmittableThreadLocal<>();
        parent.set("TransmittableThreadLocal的值");

        executor.submit(() -> System.out.println(Thread.currentThread().getName() + "获取父线程TransmittableThreadLocal中的值为:" + parent.get()));
        //输出结果为： pool-1-thread-1获取父线程InheritableThreadLocal中的值为:TransmittableThreadLocal的值
    }
}
