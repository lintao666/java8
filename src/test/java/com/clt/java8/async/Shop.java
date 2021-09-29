package com.clt.java8.async;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @date 2019/4/27 14:49
 */

public class Shop {
    private String product;
    private String name;
    private double price;

    public Shop(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public String getName() {
        return name;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Long price) {
        this.price = price;
    }



    //根据商品名称获取商品价格 （同步代码实现）
    public double getPrice(String product) {
        return calculatePrice(product);
    }
    //根据商品名称获取商品价格 （异步代码实现v1）
    public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);//如果价格计算正常结束，完成Future操作并设置商品价格
            } catch (Exception e) {//否则抛出导致失败的异常，完成这次Future操作
                futurePrice.completeExceptionally(e);
            }
        }).start();
        return futurePrice;
    }
    //根据商品名称获取商品价格 （异步代码实现v2）
    public Future<Double> getPriceAsync2(String product){
        return CompletableFuture.supplyAsync(()->calculatePrice(product));
    }


    public static void dely() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private double calculatePrice(String product) {
        dely();
        return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
    }

}
