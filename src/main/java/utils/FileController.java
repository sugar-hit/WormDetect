package utils;

import java.io.*;
import java.util.ArrayList;

public class FileController {
    synchronized public static void output(String str) {
        String filePath = "D:\\Project\\2020\\dig-lib\\output.iedb";
        File file = new File(filePath);
        try
        {
            if (!file.exists()) {
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.flush();
                writer.close();
            }
            else {
                RandomAccessFile writerReader = new RandomAccessFile(filePath, "rw");
                long fileLength = writerReader.length();
                writerReader.seek(fileLength);
                writerReader.writeBytes(str);
                writerReader.close();
            }

        }
        catch (IOException e) {
            System.out.println("IOE error :" + Time.dateTimeFormat(Time.now()));
            e.printStackTrace();
        }
    }

    public static void loader (ArrayList<ArrayList<String>> array) {
//        String
    }
}
