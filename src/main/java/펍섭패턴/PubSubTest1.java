package 펍섭패턴;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PubSubTest1 {
    public static void main(String[] args) throws InterruptedException {
        FoodTable foodTable = new FoodTable();
        Cook cook = new Cook(foodTable);
        Customer customer1 = new Customer(foodTable, "drink");
        Customer customer2 = new Customer(foodTable, "burger");

        //요리사 2명
        Thread t1 = new Thread(cook,"요리사1");

        Thread t3 = new Thread(customer1, "드링크매니아1");
        Thread t4 = new Thread(customer1, "드링크매니아2");
        Thread t5 = new Thread(customer1, "드링크매니아3");

        Thread t6 = new Thread(customer2, "버거매니아1");
        Thread t7 = new Thread(customer2, "버거매니아2");
        Thread t8 = new Thread(customer2, "버거매니아3");

        t1.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();

        Thread.sleep(10000);
        System.out.println("총 만든 음식 개수 : " + foodTable.getMakeFood().get());
        System.exit(0);
    }
}


class FoodTable {

    public String[] allNames = {"치즈버거", "불고기버거", "비건버거"
                                , "콜라", "환타", "물"};

    private List<String> burgers = new ArrayList<>();
    private List<String> drinks = new ArrayList<>();

    private ReentrantLock lock = new ReentrantLock();
    private Condition forDrink = lock.newCondition();
    private Condition forBurger = lock.newCondition();
    private Condition condition = lock.newCondition();

    private AtomicInteger makeFood = new AtomicInteger();

    //요리사들이 음식을 충전한다.
    public void add(String food) {
        lock.lock();
        try {
            if (food == "콜라" || food == "환타" || food == "물") {
                drinks.add(food);
                //총 만든 요리 갯수 atomic으로 계산.
                makeFood.getAndIncrement();
                System.out.println(Thread.currentThread().getName()
                    + "drink추가 ->" + drinks);
                //대기하고 있는 드링크 매니아를 깨운다.
                forDrink.signal();
                try {
                    Thread.sleep(500);
                }catch (InterruptedException e) {}
            } else {
                burgers.add(food);
                //총 만든 요리 갯수 atomic으로 계산.
                makeFood.getAndIncrement();
                System.out.println(Thread.currentThread().getName()
                    + "burget추가 ->"  + burgers);
                //대기하고 있는 버거 매니아를 깨운다.
                forBurger.signal();
                try {
                    Thread.sleep(500);
                }catch (InterruptedException e) {}
            }
        }finally {
            lock.unlock();
        }


    }
    //소비자가 음식을 소비한다.
    public void remove(String topic) {
        //드링크 매니아닌지 버거 매니아인지
        String setTopic = topic;
        lock.lock();
        String name = Thread.currentThread().getName();
        if (setTopic == "drink") {
            try {
                while (drinks.isEmpty()) {
                    System.out.println("-------"+name + "is waiting");
                    try {
                        //드링크 매니아를 잠재운다.
                        forDrink.await();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
                while (true) {
                    String eatFood = drinks.remove(0);
                    System.out.println(name + "ate" + eatFood);
                    return;
                }

            } finally {
                lock.unlock();
            }
        } else {
            try {
                while (burgers.isEmpty()) {
                    System.out.println("-------"+name + "is waiting");
                    try {
                        //버거 매니아를 잠재운다.
                        forBurger.await();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
                while (true) {
                    String eatFood = burgers.remove(0);
                    System.out.println(name + "ate" + eatFood);
                    return;
                }

            } finally {
                lock.unlock();
            }

        }

    }

    public AtomicInteger getMakeFood() {
        return makeFood;
    }




}

class Customer implements Runnable {
    private FoodTable foodTable;

    private String topic;

    Customer(FoodTable foodTable, String topic) {
        this.foodTable = foodTable;
        this.topic = topic;
    }


    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            }catch (InterruptedException e) {}

            foodTable.remove(topic);
        }

    }
}

class Cook implements Runnable {
    private FoodTable foodTable;

    Cook(FoodTable foodTable) {
        this.foodTable = foodTable;
    }
    @Override
    public void run() {
        while (true) {
            int idx = (int) (Math.random() * 6);
            foodTable.add(foodTable.allNames[idx]);
        }
    }
}