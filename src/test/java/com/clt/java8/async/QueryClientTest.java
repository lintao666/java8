package com.clt.java8.async;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static java.util.stream.Collectors.toList;

/**
 * @date 2019/4/27 19:08
 */
public class QueryClientTest {
    List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
            new Shop("product2"), new Shop("product3"),
            new Shop("product4"), new Shop("product5")
            , new Shop("product6"), new Shop("product7"),
            new Shop("product8"), new Shop("product9")
    );
    //使用定制的执行器
    private final Executor executor =
            Executors.newFixedThreadPool(Math.min(shops.size(), 100),//创建一个线程池，线程数目为100和商店数目二者中较小的一个值
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setDaemon(true);//使用守护线程---这种方式不会阻止程序的关停
                            return t;
                        }
                    });

    //根据产品名 获取一个字符串列表（商店名称，该商店中指定商品的价格）
    public List<String> findPrices(String product) {
        return shops.stream().map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());

    }

    //根据产品名 获取一个字符串列表（商店名称，该商店中指定商品的价格）
    public List<String> findPricesParallel(String product) {
        return shops.parallelStream().map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }

    //使用CompletableFuture
    public List<String> findPricesByCompletableFuture(String product) {
        //使用CompletableFuture以异步的方式计算每种商品的价格
        List<CompletableFuture<String>> priceFutures =
                shops.stream().map(shop -> CompletableFuture.supplyAsync(() -> shop.getName() + "price is" + shop.getPrice(product)))
                        .collect(toList());
        //等待所有异步操作结束
        return priceFutures.stream().map(CompletableFuture::join).collect(toList());
    }
    //使用CompletableFuture 和定制的执行器
    public List<String> findPricesByCompletableFutureAndExecutor(String product) {
        //使用CompletableFuture以异步的方式计算每种商品的价格
        List<CompletableFuture<String>> priceFutures =
                shops.stream().map(shop -> CompletableFuture.supplyAsync(() -> shop.getName() + "price is" + shop.getPrice(product),executor))
                        .collect(toList());
        //等待所有异步操作结束
        return priceFutures.stream().map(CompletableFuture::join).collect(toList());
    }

}
