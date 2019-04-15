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

   原子性解决方案：```volatile```、```synchronized```,```final```

2. ###### 原子性

   一个操作或多个操作，要么全部执行并且执行的过程不会被任何因素打扰，要么就都不执行

   原子性解决方案：```synchronized```

3. ###### 有序性

   指执行的顺序按照代码的先后顺序执行

   原子性解决方案：```volatile```、```synchronized```

##### JMM内存模型

------



##### volatile

###### 轻量级的锁

解决可见性、防止指令重排

```java
public class Volatile_001 {
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
           a = 1;
           x = b;
        });

        Thread t2 = new Thread(()->{
           b = 1;
           y = a;
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("x = " + x + ", y = " + y);
    }
}
```

###### 预期结果

```x = 0, y = 1```,```x = 1, y = 1```,```x = 1, y = 0```

编译器重排序可能会出现(重排序导致)

```x = 0, y = 0```



##### CPU层面的内存屏障

###### store barrier 写屏障

强制所有在storestore内存屏障之前的所有指令先执行、发送缓存失效的信号；所有在storestore内存屏障指令之后的store指令，必须在storestore内存屏障之前的指令执行完之后再执行

###### load barrier 读屏障

loadload 



###### full barrier 全屏障

作用：

防止指令之间的重排序

保证数据的可见性



###### ```volatile```保证可见性

###### 对一个volatile变量的读，总是能看到（任意线程）对这个volatile变量最后的写入

```java
public class Volatile_002 {
    private static volatile boolean flag = false;
    private static int i;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            while (!flag) {
                i++;
            }
        });
        t1.start();
        Thread.sleep(1000);
        flag = true;
        System.out.println(i);
    }
}
```

##### ```volatile```不能保证原子性

###### 对任意单个volatile变量的读/写具有原子性，但类似于volatile++这种复合操作不具有原子性。

```

对一个原子递增的操作，会分为三个步骤：1.读取volatile变量的值到local；2.增加变量的值；3.把local的值写回让
其他线程可见
```

> ​        假如某个时刻变量inc的值为10，
>  　　线程1对变量进行自增操作，线程1先读取了变量inc的原始值，然后线程1被阻塞了；
>  　　然后线程2对变量进行自增操作，线程2也去读取变量inc的原始值，由于线程1只是对变量inc进行读取操作，而没有对变量进行修改操作，所以不会导致线程2的工作内存中缓存变量inc的缓存行无效，所以线程2会直接去主存读取inc的值，发现inc的值时10，然后进行加1操作，并把11写入工作内存，最后写入主存。
>  　　然后线程1接着进行加1操作，由于已经读取了inc的值，注意此时在线程1的工作内存中inc的值仍然为10，所以线程1对inc进行加1操作后inc的值为11，然后将11写入工作内存，最后写入主存。
>  　　那么两个线程分别进行了一次自增操作后，inc只增加了1。

> 
>
> ------
>
> 

##### synchronized

###### 加锁方式

1. 修饰实例方法

   当前实例加锁

2. 静态方法

   当前类加锁

3. 修饰代码块

   对指定对象加锁



###### synchronized括号后面的对象

​	synchronized扩后后面的对象是一把锁，在java中任意一个对象都可以成为锁，简单来说，我们把object比喻是一个key，拥有这个key的线程才能执行这个方法，拿到这个key以后在执行方法过程中，这个key是随身携带的，并且只有一把。如果后续的线程想访问当前方法，因为没有key所以不能访问只能在门口等着，等之前的线程把key放回
去。所以，synchronized锁定的对象必须是同一个，如果是不同对象，就意味着是不同的房间的钥匙，对于访问者
来说是没有任何影响的。

###### synchronized的锁的原理

​	jdk1.6以后对synchronized锁进行了优化，包含偏向锁、轻量级锁、重量级锁; 在了解synchronized锁之前，我们需要了解两个重要的概念，一个是对象头、另一个是monitor

###### Java对象头

​	在Hotspot虚拟机中，对象在内存中的布局分为三块区域：对象头、实例数据和对齐填充；Java对象头是实现
synchronized的锁对象的基础，一般而言，synchronized使用的锁对象是存储在Java对象头里。它是轻量级锁和偏
向锁的关键

###### Mawrk Word

略

###### Monitor

略

##### synchronized的锁升级和获取过程

锁的级别从低到高逐步升级， 无锁->偏向锁->轻量级锁->重量级锁

##### 自旋锁（CAS）

​	自旋锁就是让不满足条件的线程等待一段时间，而不是立即挂起。看持有锁的线程是否能够很快释放锁。怎么自旋呢？其实就是一段没有任何意义的循环。
​	虽然它通过占用处理器的时间来避免线程切换带来的开销，但是如果持有锁的线程不能在很快释放锁，那么自旋的线程就会浪费处理器的资源，因为它不会做任何有意义的工作。所以，自旋等待的时间或者次数是有一个限度的，如果自旋超过了定义的时间仍然没有获取到锁，则该线程应该被挂起。

##### 偏向锁

​	大多数情况下，锁不仅不存在多线程竞争，而且总是由同一线程多次获得，为了让线程获得锁的代价更低而引入了偏向锁。当一个线程访问同步块并获取锁时，会在对象头和栈帧中的锁记录里存储锁偏向的线程ID，以后该线程在进入和退出同步块时不需要进行CAS操作来加锁和解锁，只需简单地测试一下对象头的Mark Word里是否存储着指
向当前线程的偏向锁。如果测试成功，表示线程已经获得了锁。如果测试失败，则需要再测试一下Mark Word中偏
向锁的标识是否设置成1（表示当前是偏向锁）：如果没有设置，则使用CAS竞争锁；如果设置了，则尝试使用CAS
将对象头的偏向锁指向当前线程

##### 轻量级锁

​	引入轻量级锁的主要目的是在没有多线程竞争的前提下，减少传统的重量级锁使用操作系统互斥量产生的性能消耗。当关闭偏向锁功能或者多个线程竞争偏向锁导致偏向锁升级为轻量级锁，则会尝试获取轻量级锁

##### 重量级锁

​	重量级锁通过对象内部的监视器（monitor）实现，其中monitor的本质是依赖于底层操作系统的Mutex Lock实
现，操作系统实现线程之间的切换需要从用户态到内核态的切换，切换成本非常高。

------



##### ```wait```和```notify```

```java
//notify 唤醒线程
public class NotifyDemo extends Thread{
    private Object lock;

    public NotifyDemo(Object lock) {
        this.lock = lock;
    }
    @Override
    public void run() {
        synchronized (lock){
            System.out.println("开始执行 thread notify");
            lock.notify();
            System.out.println("执行结束 thread notify");
        }
    }
}
```

```java
//wait 使线程等待
public class WaitDemo extends Thread{
    private Object lock;
    public WaitDemo(Object lock) {
        this.lock = lock;
    }
    @Override
    public void run() {
        synchronized (lock){
            System.out.println("开始执行 thread wait");
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("执行结束 thread wait");
        }
    }
    public static void main(String[] args) {
        Object lock = new Object();
        new WaitDemo(lock).start();
        new NotifyDemo(lock).start();
    }
}
```

> ###### 执行结果:
>
> 开始执行 thread wait
> 开始执行 thread notify
> 执行结束 thread notify
> 执行结束 thread wait

##### ```wait```和```notify```的原理

​	调用```wait```方法，首先会获取监视器锁，获得成功以后，会让当前线程进入等待状态进入等待队列并且释放锁；然后当其他线程调用```notify```或者```notifyall```以后，会选择从等待队列中唤醒任意一个线程，而执行完```notify```方法以后，并不会立马唤醒线程，原因是当前的线程仍然持有这把锁，处于等待状态的线程无法获得锁。必须要等到当前的线程执行完按```monitorexit```指令以后，也就是锁被释放以后，处于等待队列中的线程就可以开始竞争锁了。