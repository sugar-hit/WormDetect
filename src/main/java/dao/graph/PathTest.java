package dao.graph;

import dao.PathList;

import java.util.ArrayList;

public class PathTest {
    public void test() {
        ArrayList<String> arr = PathList.list();
        for (String str : arr) {
            System.out.println(str);
        }
    }
}
