package com.kob.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//多线程需要继承自thread
public class BotPool extends Thread {
//    定义一个锁
    private final ReentrantLock lock = new ReentrantLock();
//    队列空的话阻塞，有新的任务唤醒，所以需要一个条件变量
    private final Condition condition = lock.newCondition();
//    定义一个队列，里面存bot
    private final Queue<Bot> bots=new LinkedList<>();

//    队列里插入一个bot
    public void addBot(Integer userId,String botCode,String input){
        lock.lock();//涉及对q的修改 ，需要加锁
        try{
            bots.add(new Bot(userId,botCode,input));
            condition.signalAll();//唤醒其他线程
        }finally{
            lock.unlock();
        }
    }

    private void consume(Bot bot) {
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000,bot);
    }


    @Override
    public void run() {
        while(true){
//            队列涉及两个线程的读写冲突，一个是生产者加任务，一个是消费者做任务，需要加锁
            lock.lock();
            if(bots.isEmpty()){//队列空，要把当前线程阻塞,用condition.await()
                try {
                    condition.await();//await默认包含一个锁释放
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            }else{//队列非空
                Bot bot=bots.remove();//取出队头并删除
                lock.unlock();

                consume(bot);//放在unlock后面，因为比较耗时需要执行几秒钟
            }
        }
    }
}
