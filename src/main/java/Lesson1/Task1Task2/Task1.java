package Lesson1.Task1Task2;

import java.util.ArrayList;
import java.util.Arrays;

public class Task1 {

    public static void main(String[] args) {

        Integer[] arr = new Integer[2];

        arr[0] = 1;
        arr[1] = 2;

        System.out.println(Arrays.deepToString(arr));
        transfer(arr, 0, 1);
        System.out.println(Arrays.deepToString(arr));


        ArrayList<Integer> arrList = toArrayList(arr);

    }

    public static <T> void transfer(T[] arr, int a, int b){
        T tmp = arr[a];
        arr[b] = arr[b];
        arr[b] = tmp;
    }

    public static <T> ArrayList<T> toArrayList(T[] arr) {
        return new ArrayList<T>(Arrays.asList(arr));
    }
}
