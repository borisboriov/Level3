package Lesson5;


public class Tunnel extends Stage {
    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }


    @Override
    public void go(Car c) {
        try {
            try {
//                System.out.println(c.getName() + " подъехал к тоннелю");
                if (MainClass.semaphore.availablePermits() == 0) {
                    System.out.println(c.getName() + " готовится к этапу(!ЖДЕТ ОЧЕРЕДЬ!!): " + description);
                }
                MainClass.semaphore.acquire();
                System.out.println(c.getName() + " З А Е Х А Л начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                MainClass.semaphore.release();
                System.out.println(c.getName() + " ------------------закончил этап: " + description);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
