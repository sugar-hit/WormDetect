package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileController {
    public static void output(String str) {
        String filePath = Config.getLearningModeSavingPath();
        File file = new File(filePath + "rule" + Time.dateTimeFormat(Time.now()) + ".iedb");
        try
        {
            if (!file.exists())
                file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.append(str).append("\r\n");
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            System.out.println("IOE error" + Time.dateTimeFormat(Time.now()));
        }
    }
}
