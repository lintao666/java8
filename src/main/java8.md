1. **方法引用**		语法:  目标引用（类名）：：方法名称

   `File::isHidden`

   方法引用可以被看作 仅仅调用特定方法的Lambda的一种快捷写法。

   事实上方法引用就是根据已有的方法实现来创建Lambda表达式。

   可以把方法引用看作针对仅仅涉及单一方法的Lambda的语法糖。

   方法引用` Apple::getWeight `就是Lambda表达式`（Apple a） -> a.getWeight()`的快捷写法。

   方法引用主要有三类：

   1. 指向静态方法的引用

   2. 指向任意类型实例方法的方法引用

   3. 指向现有对象的实例方法的方法引用
      指在Lambda中调用一个已经存在的外部对象中的方法。`() -> obj.getValue();` 等价于 `obj::getValue` 

   4. 构造函数引用：
      利用默认构造创建Apple的Lambda表达式` Supplier<Apple> c1=() -> new Apple();` 等价于`Supplier<Apple> c1=Apple::new; //Apple a=c1.get(); `
      利用有参构造创建Apple的Lambda表达式` Function<Integer,Apple> c2=(weight) ->new Apple(weight);`等价于`c2=Apple::new; //Apple b=c2.apply(100);`

   5. 数组构造函数

      ```
      Function<Integer,String[]> fun=String[]::new;
      String[] strArr=fun.apply(4);
      ```

   6. 父类调用

2. **Lambda(匿名函数)**

   `(int i) -> i+1`

   * 在哪里使用Lambda?
     * 在函数式接口上使用

3. **函数式接口** ：只定义一个抽象方法的接口

   常见的函数式接口：

   ```
   Comparator
   Runnable
   Callable
   java.util.function.*
   ```

4. **行为参数化**

   通过使用Lambda把代码传递给方法。

5. **流处理,以及并行处理流元素**

   流的优点：

   1. 声明性 -更简洁，更易读
   2. 可复合 -更灵活
   3. 可并行 -性能更好

   流操作

   * 创建流：
   * 中间操作：返回另一个流
   * 终端操作：消费流，并产生结果

   `flatMap`:把一个流中的每个值都换成另一个流

6. **接口中的默认方法**

7. **Optional代替null**

   一个容器类，代表一个值存在或不存在

8. `CompletableFuture`：组合式异步编程

   * Future接口的局限性：

     * 难以表示Future结果之间的依赖性

   * `ComletableFuture`的特点：

     * 将两个异步计算合并为一个(两个异步计算相互独立，同时第二个依赖于第一个的结果)：

       ```
       public <U> CompletableFuture<U> thenCompose(
               Function<? super T, ? extends CompletionStage<U>> fn) {
               return uniComposeStage(null, fn);
           }
       ```

     * 将两个异步计算合并为一个(两个异步计算相互独立)：

       ```
       public <U,V> CompletableFuture<V> thenCombine(
               CompletionStage<? extends U> other,
               BiFunction<? super T,? super U,? extends V> fn) {
               return biApplyStage(null, other, fn);
           }
       
           public <U,V> CompletableFuture<V> thenCombineAsync(
               CompletionStage<? extends U> other,
               BiFunction<? super T,? super U,? extends V> fn) {
               return biApplyStage(asyncPool, other, fn);
           }
       
           public <U,V> CompletableFuture<V> thenCombineAsync(
               CompletionStage<? extends U> other,
               BiFunction<? super T,? super U,? extends V> fn, Executor executor) {
               return biApplyStage(screenExecutor(executor), other, fn);
           }
       ```

       

     * 等待Future集合中的所有任务都完成

       ```
       public static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs) {
               return andTree(cfs, 0, cfs.length - 1);
       }
       ```

     * 仅等待Future集合中最快结束的任务完成，并返回结果

       ```
       public static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs) {
               return orTree(cfs, 0, cfs.length - 1);
           }
       ```

     * 通过编程方式完成一个Future任务的执行(即以手工设定异步操作结果的方式，同步转异步)

       ```
       public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
               return asyncSupplyStage(asyncPool, supplier);
        }
           
       public static CompletableFuture<Void> runAsync(Runnable runnable,
                                                          Executor executor) {
               return asyncRunStage(screenExecutor(executor), runnable);
           }
       ```

     * 应对Future的完成事件(当Future的完成事件发生时会受到通知，并能使用Future计算的结果进行下一步的操作，不仅仅阻塞等待操作的结果)

       ```
       public CompletableFuture<Void> thenAccept(Consumer<? super T> action) {
               return uniAcceptStage(null, action);
           }
       public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action) {
               return uniAcceptStage(asyncPool, action);
           }
       
           public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action,
                                                          Executor executor) {
               return uniAcceptStage(screenExecutor(executor), action);
           }
       ```

       

9. 新的时间和日期`API`

10. 重构部分设计模式：

    * 策略模式
    * 模板方法
    * 观察者模式
    * 责任链模式
    * 工厂模式