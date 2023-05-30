package pw.react.backend.utils;

import java.util.ArrayList;
import java.util.List;

public class MySimpleUtils {
    public static <T> List<T> getPage(List<T> list, Integer pageNumber, Integer pageSize) {
        int size = list.toArray().length;
        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = fromIndex + pageSize < size ? fromIndex + pageSize : size;

        if ((fromIndex < 0 || fromIndex > toIndex)) {
            return new ArrayList<>();
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }

    public static <T extends Number> boolean intervalIsValid(T startA, T endA) {
        return startA.doubleValue() <= endA.doubleValue();
    }

    // both assume valid intervals?
    public static <T extends Number, U extends Number, V extends Number, W extends Number>
    boolean intervalsOverlap(T startA, U endA, V startB, W endB) {
        return startA.doubleValue() <= endB.doubleValue()
        && endA.doubleValue() >= startB.doubleValue();
    }

    public static <T extends Number, V extends Number, W extends Number>
    boolean valueIsInInterval(T startA, V startB, W endB) {
        return intervalsOverlap(startA, startA, startB, endB);
    }

    public static <T extends Number, U extends Number, V extends Number, W extends Number>
    boolean intervalIsASubset(T startA, U endA, V startB, W endB) {
        return startB.doubleValue() <= startA.doubleValue()
        && endA.doubleValue() <= endB.doubleValue();
    }
}
