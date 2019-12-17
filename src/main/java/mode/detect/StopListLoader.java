package mode.detect;

import utils.Config;

import java.io.*;
import java.util.ArrayList;

public class StopListLoader {
    public static ArrayList<String> getStopList() {
        return stopList;
    }

    public static void setStopList() {
        try {
            String filePath = Config.getLearningModeSavingPath() + "path.iedb";
            File file = new File(filePath);
            if (!file.exists())
                return;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String strLine = null;
            while(null != (strLine = reader.readLine()) )
                appendStopList(strLine);
        } catch (IOException e) {
        }
    }

    private static void appendStopList (String str) {
        if (str == null)
            return;
        if (str.equals(""))
            return;
        if (str.trim().equals(""))
            return;
        stopList.add(str);
    }

    private static ArrayList<String> stopList = new ArrayList<>();


}
