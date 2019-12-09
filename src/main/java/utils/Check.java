package utils;

import java.util.HashMap;

public class Check {
    public static class Empty {
        public static <K, V> void HashMap (HashMap<K, V> hashMap) {
            if (hashMap == null)
                hashMap = new HashMap<>();
        }
    }

    public static class InitialOrNot {
        public static <K, V> void HashMap (HashMap<K, V> hashMap) {
            Empty.HashMap(hashMap);
        }
    }
}
