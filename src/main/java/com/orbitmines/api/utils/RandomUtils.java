package com.orbitmines.api.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/*
* OrbitMines - @author Fadi Shawki - 2017
*/
public class RandomUtils {

    public static Random RANDOM = new Random();

    /* Returns a random int between 0 and (max-1), e.g. setting max to 100 will return 0-99 */
    public static int i(int max) {
        return RANDOM.nextInt(max);
    }

    /* Returns a random int between min and max */
    public static int i(int min, int max) {
        return min + i(max - min);
    }

    public static boolean chance(int percentage) {
        return percentage >= 100 || percentage < i(100);
    }

    public static boolean nextBoolean() {
        return RANDOM.nextBoolean();
    }

//    public static <T> void placeInRandomSlot(T object, T[] array) {
//        List<Integer> emptyIndexes = new ArrayList<>();
//
//        for (int i = 0; i < array.length; i++) {
//            if (array[i] == null)
//                emptyIndexes.add(i);
//        }
//
//        if (emptyIndexes.isEmpty())
//            throw new ArrayIndexOutOfBoundsException("there are no free slots in the array");
//
//        array[randomFrom(emptyIndexes)] = object;
//    }

    /* Returns a random object from a collection */
    public static <T> T randomFrom(Collection<T> collection) {
        if (collection.isEmpty())
            return null;
        int random = i(collection.size());
        int step = 0;
        for (T obj : collection) {
            if (random == step)
                return obj;
            step++;
        }
        return null;
    }

    /* Returns a random object from an array */
    public static <T> T randomFrom(T... objects) {
        return objects[i(objects.length)];
    }

    public static <T> List<T> randomFrom(List<T> list, int count) {
        List<T> selected = new ArrayList<>();
        List<T> newList = new ArrayList<>(list);

        for (int i = 0; i < count; i++) {
            T obj = randomFrom(newList);
            newList.remove(obj);
            selected.add(obj);
        }

        return selected;
    }
}
