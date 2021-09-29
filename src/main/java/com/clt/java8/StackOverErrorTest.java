package com.clt.java8;

/**
 * @date 2019/5/28 10:07
 */
public class StackOverErrorTest {
    private static int index=1;
    public void call(){
        index++;
        call();
    }

    public static void main(String[] args) {
        StackOverErrorTest test=new StackOverErrorTest();
        try {
            test.call();
        }catch (Throwable e){
            System.out.println("Stack deep: "+index);//24257 ,23807 ,22931
            e.printStackTrace();
        }
    }

}
