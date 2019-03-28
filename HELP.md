# 并发编程

##### 什么是多线程

解决进程中多任务的实时性问题，及解决阻塞问题。

##### 使用场景

1. 通过并行计算提高效率	
2. 需要等待的网络
3. IO操作异步线程减少阻塞
4. ...

##### 使用多线程

1. 继承Thread

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

   

2. 实现Runable

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

   

3. Callable

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

   - 等待阻塞：运行的线程执行了wait方法，jvm会把当前线程放入到等待队列
   - 同步阻塞：运行的线程在获取对象的同步锁时，若该同步锁被其他线程锁占用，那么jvm会把当前的线程放入锁池中
   - 其他阻塞：运行的线程执行了Thread.sleep 或.join方法，或者发出了IO请求，jvm会把线程设置为阻塞状态。当sleep结束、join线程终止、io处理完毕则线程恢复

4. ###### WAITING

   Object.wait()、Object.join()、LockSupport.park() 进入等待状态

   Object.notify()、Object.notifyAll()、LockSupport.unpark(Thread) 取消等待

5. ###### TIME_WAITING

   超时等待状态Thread.sleep

6. ###### TERMINATED

   终止状态，标示线程已经执行完毕

##### 线程的终止

1. ###### 不建议使用stop去终止

   stop方法在结束一个线程时，并不会保证线程的资源正常释放

2. ###### 请使用interrupt停止线程

   



