package util;

import java.io.Serializable;
import java.util.HashMap;

public class DataModel implements Serializable {
    public static HashMap<Long, Integer> hashMap () {
        return new HashMap<Long, Integer>();
    }
}
