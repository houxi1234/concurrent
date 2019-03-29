# 并发编程

##### 什么是多线程

解决进程中多任务的实时性问题，及解决阻塞问题。

##### 使用场景

1. 通过并行计算提高效率	
2. 需要等待的网络
3. IO操作异步线程减少阻塞
4. ...

##### 使用多线程

1. 继承```Thread```

   ~~~java
   public class MyThread extends Thread{
       @Override
       public void run() {
           System.out.println("多线程-->继承方式");
       }
       public static void main(String[] args) {
           new MyThread().start();
           new MyThread().start();
       }
   }
   ~~~

   

2. 实现```Runable```

   ~~~
   public class MyThread_002 implements Runnable {
       @Override
       public void run() {
           System.out.println("多线程-->实现方式");
       }
       public static void main(String[] args) {
          new Thread(new MyThread_002()).start();
          new Thread(new MyThread_002()).start();
       }
   }
   ~~~

   

3. ```Callable```

   ~~~java
   public class MyThread_003 implements Callable<String> {
       @Override
       public String call() throws Exception {
           return "多线程-->Callable实现";
       }
       public static void main(String[] args) throws ExecutionException, InterruptedException {
           ExecutorService executor = Executors.newFixedThreadPool(1);
   
           Callable<String> callable = new MyThread_003();
           Future<String> submit = executor.submit(callable);
           //带有返回值的线程创建方式
           String s = submit.get();
           System.out.println(s);
           executor.shutdown();
       }
   }
   ~~~

   

##### 线程的状态(6种)

1. ###### NEW

   初始状态，线程被创建，但是还没有调用start方法

2. ###### RUNABLE

   运行状态（就绪和运行中的统称）

3. ###### BLOCKED

   阻塞状态，表示线程进入等待状态，线程因某种原因放弃了CPU的使用权

   - 等待阻塞：运行的线程执行了```wait```方法，jvm会把当前线程放入到等待队列
   - 同步阻塞：运行的线程在获取对象的同步锁时，若该同步锁被其他线程锁占用，那么jvm会把当前的线程放入锁池中
   - 其他阻塞：运行的线程执行了```Thread.sleep``` 或```.join```方法，或者发出了IO请求，jvm会把线程设置为阻塞状态。当```sleep```结束、```join```线程终止、io处理完毕则线程恢复

4. ###### WAITING

   ```Object.wait()```、```Object.join()```、```LockSupport.park() ```进入等待状态

   ```Object.notify()```、```Object.notifyAll()```、```LockSupport.unpark(Thread)``` 取消等待

5. ###### TIME_WAITING

   超时等待状态Thread.sleep

6. ###### TERMINATED

   终止状态，标示线程已经执行完毕

##### 线程的终止

1. ###### 不建议使用```stop```去终止

   ```stop```方法在结束一个线程时，并不会保证线程的资源正常释放

2. ###### 请使用```interrupt```停止线程

   当前线程调用```interrupt```方法时，其实就相当于告诉线程将中断执行，具体中断时间，取决于当前线程自己

   - ```interrupt()```方法：中断当前线程

     ```java
     public class Interrupt_001 {
     
         private static int i;
     
         public static void main(String[] args) throws InterruptedException {
             Thread thread = new Thread(()->{
     
                System.out.println("isInterrupted默认值：" + 
                                   Thread.currentThread().isInterrupted());
     
                while (!Thread.currentThread().isInterrupted()){
                    i++;
                }
                System.out.println("num:" + i);
             }, "demo");
             thread.start();
             TimeUnit.SECONDS.sleep(1);
             thread.interrupt();
         }
     }
     ```

   - ```isInterrupted()```：判断线程是否被中断

   - ```Thread.interrupted()```:  对当前线程进行复位

     ~~~java
     public class Interrupt_002 {
         public static void main(String[] args) throws InterruptedException {
             Thread thread = new Thread(()->{
                 while (true) {
                     boolean flag = Thread.currentThread().isInterrupted();
                     if (flag){
                         System.out.println("复位前：" + flag);
                         Thread.interrupted();
                         System.out.println("复位后：" +
                                 Thread.currentThread().isInterrupted());
     
                     }
                 }
             }, "demo");
             thread.start();
             TimeUnit.SECONDS.sleep(1);
             thread.interrupt();
         }
     }
     ~~~

   - 抛```InterruptedException```：在```InterruptedException```抛出异常之前，JVM会把线程的中断标示清除。因为因为`InterruptedException`被认为是异常事件，其他人试图从外部阻止线程。

     ~~~java
     public class Interrupt_003 {
         public static void main(String[] args) throws InterruptedException {
             Thread thread = new Thread(()->{
                 while (true) {
                     try{
                         Thread.sleep(10000L);
                     } catch (InterruptedException e){
                         e.printStackTrace();
                     }
                 }
             }, "demo");
             thread.start();
             TimeUnit.SECONDS.sleep(1);
             thread.interrupt();
             TimeUnit.SECONDS.sleep(1);
             System.out.println(thread.isInterrupted());
         }
     }
     ~~~

3. 使用 ```volatile ```修饰的成员变量，来终止线程

   ```java
   public class Interrupt_004 {
   
       private volatile static boolean stop = true;
       private static int i;
   
       public static void main(String[] args) throws InterruptedException {
           Thread thread = new Thread(()->{
               while (stop) {
                   i++;
               }
               System.out.println(i);
           }, "demo");
           thread.start();
           TimeUnit.SECONDS.sleep(1);
           stop = false;
       }
   }
   ```

##### 线程安全

1. ###### 可见性

   指多个线程访问同一个变量时，一个线程修改了这个变量的值，其他线程能够立即看得到修改的值

2. ###### 原子性

   一个操作或多个操作，要么全部执行并且执行的过程不会被任何因素打扰，要么就都不执行

3. ###### 有序性

   指执行的顺序按照代码的先后顺序执行



 



