package utils;

import java.util.ArrayList;
import java.util.Comparator;

public class Sort {
    public static void sort (ArrayList<Long> arrayList) {
        if (arrayList == null)
            return;
        if (arrayList.isEmpty())
            return;

        arrayList.sort(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                if (String.valueOf(o1).equals(String.valueOf(o2)))
                    return 0;
                return String.valueOf(o1).compareTo(String.valueOf(o2));
            }
        });
        for (Long l: arrayList) {
            System.out.println(l);
        }
    }

}

