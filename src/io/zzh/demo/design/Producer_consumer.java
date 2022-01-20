/**
 * @author ：zzh
 * @description ：Producer-consumer Problem
 * @date ：Created in 2021/12/12 14:40
 */
package io.zzh.demo.design;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;

class StorageTest {
    @Test
    public void test(){
        // 仓库对象
        Storage storage = new Storage();
        // 生产者对象
        Producer[] p = new Producer[7];
        for (int i=0;i<p.length;i++){
            p[i] = new Producer(storage);
            // 设置生产者产品生产数量
            p[i].setNum(100);
        }
        // 消费者对象
        Consumer[] c = new Consumer[3];
        for (int i=0;i<c.length;i++){
            c[i] = new Consumer(storage);
            c[i].setNum(50);
        }
        // 线程开始执行
        for (int i=0;i<c.length;i++){
            c[i].start();
        }
        for (int i=0;i<p.length;i++){
            p[i].start();
        }
    }
}
class Storage   {
    // 仓库最大存储量
    private final int MAX_SIZE = 100;
    // 仓库存储的载体
    private LinkedList<Object> list = new LinkedList<Object>();
    // 生产num个产品
    public void produce(int num){           // 同步代码段
        synchronized (list){               // 如果仓库剩余容量不足
            while (list.size() + num > MAX_SIZE){
                System.out.println("【要生产的产品数量】:" + num + "\t\t【库存量】:"+ list.size() + "\t\t暂时不能执行生产任务!");
                try{
                    // 由于条件不满足，生产阻塞
                    list.wait();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }                  // 生产条件满足情况下，生产num个产品
            for (int i = 1; i <= num; ++i){
                list.add(new Object());
            }
            System.out.println("【已经生产产品数】:" + num + "\t\t【现仓储量为】:" + list.size());
            list.notifyAll();
        }
    }
    // 消费num个产品
    public void consume(int num){
        // 同步代码段
        synchronized (list) {
            // 如果仓库存储量不足
            while (list.size() < num){
                System.out.println("【要消费的产品数量】:" + num + "\t\t【库存量】:"+ list.size() + "\t\t暂时不能执行消费需求!");
                try{
                    // 由于条件不满足，消费阻塞
                    list.wait();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            // 消费条件满足情况下，消费num个产品
            for (int i = 1; i <= num; ++i){
                list.remove();
            }
            System.out.println("【已经消费产品数】:" + num + "\t\t【现仓储量为】:" + list.size());
            list.notifyAll();           }       }
    // get/set方法
    public LinkedList<Object> getList(){
        return list;
    }
    public void setList(LinkedList<Object> list){
        this.list = list;
    }
    public int getMAX_SIZE(){
        return MAX_SIZE;
    }
}
class Producer extends Thread   {
    // 每次生产的产品数量
    private int num;
    // 所在放置的仓库
    private Storage storage;
    // 构造函数，设置仓库
    public Producer(Storage storage){
        this.storage = storage;
    }
    // 线程run函数
    public void run(){
        produce(num);
    }
    // 调用仓库Storage的生产函数
    public void produce(int num){
        storage.produce(num);
    }
    // get/set方法
    public int getNum(){
        return num;
    }
    public void setNum(int num){
        this.num = num;
    }
    public Storage getStorage(){
        return storage;
    }
    public void setStorage(Storage storage)       {
        this.storage = storage;
    }
}
class Consumer extends Thread   {
    // 每次消费的产品数量
    private int num;
    // 所在放置的仓库
    private Storage storage;
    // 构造函数，设置仓库
    public Consumer(Storage storage){
        this.storage = storage;
    }
    // 线程run函数
    public void run(){
        consume(num);
    }
    // 调用仓库Storage的生产函数
    public void consume(int num){
        storage.consume(num);
    }
    // get/set方法
    public int getNum(){
        return num;
    }
    public void setNum(int num){
        this.num = num;
    }
    public Storage getStorage(){
        return storage;
    }
    public void setStorage(Storage storage){
        this.storage = storage;
    }
}
