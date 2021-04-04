package Lesson5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MainClass {
    public static final int CARS_COUNT = 4;
    public static CyclicBarrier barrier = new CyclicBarrier(CARS_COUNT);
    static final CountDownLatch cdl = new CountDownLatch(CARS_COUNT);
    static final CountDownLatch cdl2 = new CountDownLatch(CARS_COUNT);
     public static Semaphore semaphore = new Semaphore(CARS_COUNT/2);


    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }

        for (int i = 0; i < cars.length; i++) {
            int finalI = i;
            new Thread(() -> {
                cars[finalI].run();
                cdl2.countDown();
            }).start();
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        try {
            cdl2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
//Организуем гонки:
//Все участники должны стартовать одновременно, несмотря на то, что на подготовку у каждого из них уходит разное время.
//В туннель не может заехать одновременно больше половины участников (условность).
//Попробуйте всё это синхронизировать.
//Только после того как все завершат гонку, нужно выдать объявление об окончании.
//Можете корректировать классы (в т.ч. конструктор машин) и добавлять объекты классов из пакета util.concurrent.
//Пример выполнения кода до корректировки:а
