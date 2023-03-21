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

    public static <T extends Number> boolean intevalIsValid(T startA, T endA) {
        return startA.doubleValue() <= endA.doubleValue();
    }

    // both assume valid intervals?
    public static <T extends Number> boolean intervalsOverlap(T startA, T endA, T startB, T endB) {
        return startA.doubleValue() <= endB.doubleValue()
                && endA.doubleValue() >= startB.doubleValue();
    }
    public static <T extends Number> boolean inveralIsASubset(T startA, T endA, T startB, T endB) {
        return startB.doubleValue() <= startA.doubleValue()
                && endA.doubleValue() <= endB.doubleValue();
    }
}
