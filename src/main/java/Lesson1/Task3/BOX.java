package Lesson1.Task3;

import Lesson1.Task3.Fruits.Fruit;

import java.util.ArrayList;
import java.util.Arrays;

public class BOX<T extends Fruit> {

    private ArrayList<T> fruits;

    public BOX(T... fruits) {

        this.fruits = new ArrayList<T>(Arrays.asList(fruits));
    }

    public float getWeight() {
        float sum = 0;
        for (T fruit : fruits) {
            sum += fruit.getWeight();
        }
        return sum;
    }


    public boolean compare(BOX<?> another) {
        if (this.getWeight() == another.getWeight()) {
            return true;
        } else return false;
    }

    public void transfer(BOX<? super T> box) {
        box.fruits.addAll(this.fruits);
        clear();
    }

    public void clear() {
        fruits.clear();
    }

    public void add(T... items) {
        this.fruits.addAll(Arrays.asList(items));
    }
}

