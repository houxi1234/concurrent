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
> 　　线程1对变量进行自增操作，线程1先读取了变量inc的原始值，然后线程1被阻塞了；
> 　　然后线程2对变量进行自增操作，线程2也去读取变量inc的原始值，由于线程1只是对变量inc进行读取操作，而没有对变量进行修改操作，所以不会导致线程2的工作内存中缓存变量inc的缓存行无效，所以线程2会直接去主存读取inc的值，发现inc的值时10，然后进行加1操作，并把11写入工作内存，最后写入主存。
> 　　然后线程1接着进行加1操作，由于已经读取了inc的值，注意此时在线程1的工作内存中inc的值仍然为10，所以线程1对inc进行加1操作后inc的值为11，然后将11写入工作内存，最后写入主存。
> 　　那么两个线程分别进行了一次自增操作后，inc只增加了1。

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



#### 同步锁

#### Lock

Lock是一个接口，核心的两个方法lock和unlock，有ReentrantLock、ReentrantReadWriteLock实现方法

#### ReentrantLock

重入锁，表示支持重新进入的锁。如果当前线程t1通过调用lock方法获取了锁之后，再次调用lock，是
不会再阻塞去获取锁的，直接增加重试次数就行了。

```java
public class LockDemo {

    private static int count = 0;
    static Lock lock = new ReentrantLock();

    public static void inc() {
        lock.lock();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                LockDemo.inc();
            }).start();
        }
        Thread.sleep(3000);
        System.out.println("result:" + count);
    }
}
```

分类：

- ```非公平的重入锁```

  先请求不一定先获取锁，就是不公平的

  #### ```NonfairSync.lock```

  ```java
  final void lock() {
  	if (compareAndSetState(0, 1)) //这是跟公平锁的主要区别,一上来就试探锁是否空闲,如果可以插队，则设置获得锁的线程为当前线程
  //exclusiveOwnerThread属性是AQS从父类AbstractOwnableSynchronizer中继承的属性，用来保存当前占用同步状态的线程
  	setExclusiveOwnerThread(Thread.currentThread());
  	else
  		acquire(1); //尝试去获取锁
  }
  
  ```

  ```compareAndSetState```，这个方法在前面提到过了，再简单讲解一下，通过cas算法去改变state的值，而这个state是什么呢？ 在AQS中存在一个变量```state```，对于```ReentrantLock```来说，如果state=0表示无锁状态、如果state>0表示有锁状态。
  所以在这里，是表示当前的state如果等于0，则替换为1，如果替换成功表示获取锁成功了
  由于ReentrantLock是可重入锁，所以持有锁的线程可以多次加锁，经过判断加锁线程就是当前持有锁的线程时（即```exclusiveOwnerThread==Thread.currentThread()）```，即可加锁，每次加锁都会将state的值+1，state等于几，就代表当前持有锁的线程加了几次锁;

  解锁时每解一次锁就会将``state```减1，```state```减到0后，锁就被释放掉，这时其它线程可以加锁；

  #### AbstractQueuedSynchronizer.acquire

  如果CAS操作未能成功，说明state已经不为0，此时继续acquire(1)操作,acquire是AQS中的方法 当多个线程同时进入这个方法时，首先通过cas去修改state的状态，如果修改成功表示竞争锁成功，竞争失败的，tryAcquire会返回false
  这个方法的主要作用是
  Ø 尝试获取独占锁，获取成功则返回，否则
  Ø 自旋获取锁，并且判断中断标识，如果中断标识为true，则设置线程中断
  Ø addWaiter方法把当前线程封装成Node，并添加到队列的尾部

  。。。

  。。。

  。。。

- ```公平的重入锁```:先对锁进行获取的请求一定先被满足获得锁，这就是公平锁

- 区别：

  锁的公平性是相对于获取锁的顺序而言的，如果是一个公平锁，那么锁的获取顺序就应该符合请求的绝对时间顺序，也就是FIFO。 在上面分析的例子来说，只要CAS设置同步状态成功，则表示当前线程获取了锁，而公平锁则不一样，差异点有两个，非公平锁在获取锁的时候，会先通过CAS进行抢占，而公平锁则不会

#### ReentrantReadWriteLock

```读写锁```，同一时刻允许多个线程访问，但是线程在进行写的时候，所有的读或写都会被阻塞。

```java
public class LockDemo_2 {
    
    static Map<String, Object> cacheMap = new HashMap<>();
    static ReadWriteLock rw = new ReentrantReadWriteLock();
    static Lock read = rw.readLock();
    static Lock write = rw.writeLock();
    
    public static final Object get(String key) {
        System.out.println("开始读取数据");
        //读锁
        read.lock(); 
        try {
            return cacheMap.get(key);
        } finally {
            read.unlock();
        }
    }

    public static final Object put(String key, Object value) {
        //写锁
        write.lock();
        System.out.println("开始写数据");
        try {
            return cacheMap.put(key, value);
        } finally {
            write.unlock();
        }
    }
}
```

 ###  Lock与synchronized对比

| Lock                             | synchronized                                             |
| -------------------------------- | -------------------------------------------------------- |
| 类                               | 关键词                                                   |
| 灵活性高，可以控制锁的释放和获取 | 被动释放，出现异常或者同步代码块执行完成之后，才会释放锁 |
| 可判断状态                       | 无法判断状态                                             |
| 可实现公平锁、非公平锁           | 只有非公平锁                                             |
| 需手动unlock                     | 被动释放                                                 |

#### AQS

Lock之所以能实现线程安全的锁，主要的核心是AQS(AbstractQueuedSynchronizer),AbstractQueuedSynchronizer提供了一个FIFO队列，可以看做是一个用来实现锁以及其他需要同步功能的框架。这里简称该类为AQS。AQS的使用依靠继承来完成，子类通过继承自AQS并实现所需的方法来管理同步状态。例如常见的ReentrantLock，CountDownLatch等AQS的两种功能

从使用上来说，AQS的功能可以分为两种：独占和共享。

独占锁模式下，每次只能有一个线程持有锁，比如前面给大家演示的ReentrantLock就是以独占方式实现的互斥锁

共享锁模式下，允许多个线程同时获取锁，并发访问共享资源，比如ReentrantReadWriteLock。

很显然，独占锁是一种悲观保守的加锁策略，它限制了读/读冲突，如果某个只读线程获取锁，则其他读线程都只能等待，这种情况下就限制了不必要的并发性，因为读操作并不会影响数据的一致性。共享锁则是一种乐观锁，它放宽了加锁策略，允许多个执行读操作的线程同时访问共享资源

#### AQS的内部实现

同步器依赖内部的同步队列（一个FIFO双向队列）来完成同步状态的管理，当前线程获取同步状态失败时，同步器
会将当前线程以及等待状态等信息构造成为一个节点（Node）并将其加入同步队列，同时会阻塞当前线程，当同
步状态释放时，会把首节点中的线程唤醒，使其再次尝试获取同步状态。

```java
//Node的主要属性如下
static final class Node {
	int waitStatus; //表示节点的状态，包含cancelled（取消）；condition 表示节点在等待condition也就是在condition队列中
	Node prev; //前继节点
	Node next; //后继节点
	Node nextWaiter; //存储在condition队列中的后继节点
	Thread thread; //当前线程
}
```

AQS类底层的数据结构是使用双向链表，是队列的一种实现。包括一个```head```节点和一个```tail```节点，分别表示头结点和尾节点，其中头结点不存储```Thread```，仅保存```next```结点的引用

当一个线程成功地获取了同步状态（或者锁），其他线程将无法获取到同步状态，转而被构造成为节点并加入到同
步队列中，而这个加入队列的过程必须要保证线程安全，因此
同步器提供了一个基于```CAS```的设置尾节点的方法：```compareAndSetTail(Node expect,Nodeupdate)```，它需要传递当前线程“认为”的尾节点和当前节点，只有设置成功后，当前节点才正式与之前的尾节点建立关联。

同步队列遵循FIFO，首节点是获取同步状态成功的节点，首节点的线程在释放同步状态时，将会唤醒后继节点，而
后继节点将会在获取同步状态成功时将自己设置为首节点。

AQS中，除了本身的链表结构以外，还有一个很关键的功能，就是```CAS```，这个是保证在多线程并发的情况下保证线
程安全的前提下去把线程加入到AQS中的方法,可以简单理解为乐观锁



### Condition

任意一个Java对象，都拥有一组监视器方法（定义在java.lang.Object上），主要包括wait()、notify()以及notifyAll()方法，这些方法与synchronized同步关键字配合，可以实现等待/通知模式
JUC包提供了Condition来对锁进行精准控制，Condition是一个多线程协调通信的工具类，可以让某些线程一起等待某个条件（condition），只有满足条件时，线程才会被唤醒。

```Condition```的作用是对锁进行更精确的控制。Condition中的```await()```方法相当于Object的```wait()```方法，Condition中的```signal()```方法相当于Object的```notify()```方法，Condition中的```signalAll()```相当于Object的```notifyAll()```方法。不同的是，Object中的```wait()```,```notify()```,```notifyAll()``方法是和"同步锁"(```synchronized```关键字)捆绑使用的；而```Condition```是需要与"互斥锁"/"共享锁"捆绑使用的。

#### 示例

```java
public class ConditionDemoWait implements Runnable{
    private Lock lock;
    private Condition condition;
    public ConditionDemoWait(Lock lock, Condition condition){
        this.lock=lock;
        this.condition=condition;
    }
    @Override
    public void run() {
        System.out.println("begin -ConditionDemoWait");
        try {
            lock.lock();
            condition.await();
            System.out.println("end - ConditionDemoWait");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
```

```java
public class ConditionDemoSignal implements Runnable {
    private Lock lock;
    private Condition condition;

    public ConditionDemoSignal(Lock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        System.out.println("begin -ConditionDemoSignal");
        try {
            lock.lock();
            condition.signal();
            System.out.println("end - ConditionDemoSignal");
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(new ConditionDemoWait(lock, condition)).start();
        new Thread(new ConditionDemoSignal(lock, condition)).start();
    }
}
```

#### 结果

```
begin -ConditionDemoWait
begin -ConditionDemoSignal
end - ConditionDemoSignal
end - ConditionDemoWait
```

### 原子操作

基本类型对应：```AtomicBoolean、AtomicInteger、AtomicLong```
数组类型对应：```AtomicIntegerArray、AtomicLongArray、AtomicReferenceArray```
引用类型对应：```AtomicReference、AtomicReferenceFieldUpdater、AtomicMarkableReference```
字段类型对应：```AtomicIntegerFieldUpdater、AtomicLongFieldUpdater、AtomicStampedReference```



### 线程池

类似数据库连接池的作用，线程池是用来重复管理线程避免创建大量线程增加开销。

合理的使用线程池：

1. 降低创建线程和销毁线程的性能开销
2. 合理设置线程池的大小可以避免线程数超出硬件资源瓶颈带来的问题（类似限流的作用）
3. 线程是稀缺资源，如果无效的创建，会影响系统的稳定性

#### summit和execute的区别

1. ```execute``` 只能接受```Runable```类型的任务
2. ```submit```不管是```Runable```还是```Callable```类型的任务都可以接受，但是```Runable```返回值均为```void```，所以使用```Future```的```get()```获得的还是```null```



#### 最大线程数设置

1. 工作线程不是越多越好
2. 线程切换有开销，频换切换会使性能下降
3. 调用sleep()函数不会占用CPU,accept()阻塞和recv()阻塞时也会让出cpu
4. 影响线程数量的因素：**Java虚拟机本身：-Xms，-Xmx，-Xss**，**系统限制**

### 线程池创建

**【强制】**线程池不允许使用Executors去创建，而是通过```ThreadPoolExecutor```的方式，这样的处理方式让写的同学更明确线程池的运行规则，规避资源耗尽的风险。
 说明：```Executors```返回的线程池对象弊端如下：
 1）```FixedThreadPool和SingleThreadPool```：允许的请求队列长度为```Integer.MAX_VALUE```，可能会堆积大量的请求，从而导致```OOM```。
 2）```CacheThreadPool和ScheduledThreadPool```：允许创建线程数量为```Integer.MAX_VALUE```，可能会创建大量线程，从而导致```OOM```。

使用**ThreadPoolExecutor** 创建线程

```java
public class executors {
    /** 核心线程数 */
    private static final int corePoolSize = 5;
    /** 最大线程数 */
    private static final int maximumPoolSize = 10;
    /** 超时时间，特指超出核心线程数量以外的线程空余存活时间 */
    private static final long keepAliveTime = 60L;
    /** 存活时间单位*/
    private static final TimeUnit unit = TimeUnit.SECONDS;
    /** 保存执行任务的队列*/

    public static void main(String[] args)  {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

}
```

```java

public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              RejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             Executors.defaultThreadFactory(), handler);
    }

```

```

```

- **corePoolSize** :线程池的核心池大小，在创建线程池之后，线程池默认没有任何线程。当有任务过来的时候才会去创建创建线程执行任务。换个说法，线程池创建之后，线程池中的线程数为0，当任务过来就会创建一个线程去执行，直到线程数达到corePoolSize 之后，就会被到达的任务放在队列中（说明：除非调用了prestartAllCoreThreads()或者prestartCoreThread()方法，从这2个方法的名字就可以看出，是预创建线程的意思，即在没有任务到来之前就创建corePoolSize个线程或者一个线程）。

- **maximumPoolSize** :线程池允许的最大线程数，表示最大能创建多少个线程。maximumPoolSize>=corePoolSize。

- **keepAliveTime** :表示线程没有任务执行时最多保持多久时间会终止。默认情况下，只有当线程池中的线程数大于corePoolSize时，keepAliveTime才会起作用，直到线程池中的线程数不大于corePoolSize，即当线程池中的线程数大于corePoolSize时，如果一个线程空闲的时间达到keepAliveTime，则会终止，直到线程池中的线程数不超过corePoolSize。但是如果调用了allowCoreThreadTimeOut(boolean)方法，在线程池中的线程数不大于corePoolSize时，keepAliveTime参数也会起作用，直到线程池中的线程数为0；

- **workQueue** ：一个阻塞队列，用来存储等待执行的任务，当线程池中的线程数超过它的corePoolSize的时候，线程会进入阻塞队列进行阻塞等待。通过workQueue，线程池实现了阻塞功能

  - BlockingQueue 都可用于传输和保持提交的任务。可以使用此队列与池大小进行交互：
  - 如果运行的线程少于 corePoolSize，则 Executor 始终首选添加新的线程，而不进行排队。
  - 如果运行的线程等于或多于 corePoolSize，则 Executor 始终首选将请求加入队列，而不添加新的线程。
  - 如果无法将请求加入队列，则创建新的线程，除非创建此线程超出 maximumPoolSize，在这种情况下，任务将被拒绝。
    - 排队有三种通用策略：
    - 直接提交。工作队列的默认选项是 ```SynchronousQueue```，它将任务直接提交给线程而不保持它们。在此，如果不存在可用于立即运行任务的线程，则试图把任务加入队列将失败，因此会构造一个新的线程。此策略可以避免在处理可能具有内部依赖性的请求集时出现锁。直接提交通常要求无界 ```maximumPoolSizes ```以避免拒绝新提交的任务。当命令以超过队列所能处理的平均数连续到达时，此策略允许无界线程具有增长的可能性。
    - 无界队列。使用无界队列（例如，不具有预定义容量的 ```LinkedBlockingQueue```）将导致在所有 ```corePoolSize``` 线程都忙时新任务在队列中等待。这样，创建的线程就不会超过 ```corePoolSize```。（因此，```maximumPoolSize``` 的值也就无效了。）当每个任务完全独立于其他任务，即任务执行互不影响时，适合于使用无界队列；例如，在 Web 页服务器中。这种排队可用于处理瞬态突发请求，当命令以超过队列所能处理的平均数连续到达时，此策略允许无界线程具有增长的可能性。
    - 有界队列。当使用有限的 ```maximumPoolSizes``` 时，有界队列（如``` ArrayBlockingQueue```）有助于防止资源耗尽，但是可能较难调整和控制。队列大小和最大池大小可能需要相互折衷：使用大型队列和小型池可以最大限度地降低 CPU 使用率、操作系统资源和上下文切换开销，但是可能导致人工降低吞吐量。如果任务频繁阻塞（例如，如果它们是 I/O 边界），则系统可能为超过您许可的更多线程安排时间。使用小型队列通常要求较大的池大小，CPU 使用率较高，但是可能遇到不可接受的调度开销，这样也会降低吞吐量。

- **threadFactory** ：线程工厂，用来创建线程。

- **handler** :表示当拒绝处理任务时的策略。

  - handler有四个选择：

    - ThreadPoolExecutor.AbortPolicy()   		抛出java.util.concurrent.RejectedExecutionException异常
    - ThreadPoolExecutor.CallerRunsPolicy()          重试添加当前的任务，他会自动重复调用execute()方法
    - ThreadPoolExecutor.DiscardOldestPolicy()     抛弃旧的任务

    - ThreadPoolExecutor.DiscardPolicy()                抛弃当前的任务

    

##### 参数设置：任务的角度、空间时间

1. 任务一般分为：**CPU密集型**、**IO密集型**、**混合型**，对于不同类型的任务需要分配不同大小的线程池

   - CPU密集型：尽量使用较小的线程池，一般Cpu核心数+1；因为CPU密集型任务CPU的使用率很高，若开过多的线程，只能增加线程上下文的切换次数，带来额外的开销
   - IO密集型：
     - 方法一：可以使用较大的线程池，一般CPU核心数 * 2；IO密集型CPU使用率不高，可以让CPU等待IO的时候处理别的任务，充分利用cpu时间；
     - 方法二：线程等待时间所占比例越高，需要越多线程。线程CPU时间所占比例越高，需要越少线程。举个例子：比如平均每个线程CPU运行时间为0.5s，而线程等待时间（非CPU运行时间，比如IO）为1.5s，CPU核心数为8，那么根据上面这个公式估算得到：((0.5+1.5)/0.5)*8=32。这个公式进一步转化为：
       最佳线程数目 = （线程等待时间与线程CPU时间之比 + 1）* CPU数目
       如何设置合理的队列大小

2. 时间空间的限制

   - 基于空间 ：比如队列可以占用10M内存，每个请求大小10K ，那么**workQueue**队列长度为1000合适

   - 基于时间 ：对于单个线程，如果请求超时时间为1s，单个请求平均处理时间10ms，那么队列长度为100合适





```


```

