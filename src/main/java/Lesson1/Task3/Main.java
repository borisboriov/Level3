package Lesson1.Task3;

import Lesson1.Task3.Fruits.Apple;
import Lesson1.Task3.Fruits.Orange;


public class Main {

    public static void main(String[] args) {


        Apple apl1 = new Apple();
        Apple apl2 = new Apple();
        Apple apl3 = new Apple();

        Orange org1 = new Orange();
        Orange org2 = new Orange();
        Orange org3 = new Orange();


        BOX<Apple> appleBOX = new BOX<Apple>(apl1, apl2, apl3);
        BOX<Orange> orangeBOX = new BOX<Orange>(org1, org2, org3);


        System.out.println("Вес коробки яблок - " + appleBOX.getWeight());
        System.out.println("Вес коробки апльсинов - " + orangeBOX.getWeight());
        System.out.println("Вес коробока одинков?- " +appleBOX.compare(orangeBOX));

        BOX<Orange> box3 = new BOX<>();
        orangeBOX.transfer(box3);

    }
}
