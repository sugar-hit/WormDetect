package dao.graph.path;

import dao.graph.GraphStatistics;

import java.util.ArrayList;

public class PathTest {
    public void test() {
        ArrayList<String> arr = PathList.list();
        for (String str : arr) {
            System.out.println(str);
        }
        GraphStatistics gs = new GraphStatistics();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Total Length:" + gs.getPathLengthSub());
        System.out.println("Path Counts:" + gs.getPathCount());
        if (gs.getPathCount() == 0) {
            System.out.println("Avg path length: 0");
            return;
        }
        System.out.println("Avg path length: " + gs.getPathLengthSub() / gs.getPathCount());
    }
}
