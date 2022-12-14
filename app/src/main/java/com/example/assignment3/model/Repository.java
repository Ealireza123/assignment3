package com.example.assignment3.model;

import static java.lang.String.valueOf;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class Repository {
    private static Repository instance;

    private Repository() {
    }

    //singleton design pattern
    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void saveFile(List<AnglePerMilliSecondModelItem> list, String fileName) {
        File saveFilePath = new File(
                commonDownloadDirPath(),
                (fileName + ".txt"));

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(saveFilePath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write("                " + fileName + "        \n");
            outputStreamWriter.append("   Time(MilliSecond)   ||   Angle(degree)   ");
            outputStreamWriter.append("\n================================================\n");

            for (AnglePerMilliSecondModelItem anglePerMilliSecondModelItem : list) {
                outputStreamWriter.append(valueOf(anglePerMilliSecondModelItem.getTime()));
                outputStreamWriter.append("         ||       ");
                outputStreamWriter.append(valueOf(anglePerMilliSecondModelItem.getAngle()));
                outputStreamWriter.append("\n================================================\n");
            }

            outputStreamWriter.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private File commonDownloadDirPath() {
        File dir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "assignment3");

        // Make sure the path directory exists.
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            boolean success = dir.mkdirs();
            if (!success) {
                dir = null;
            }
        }
        return dir;
    }
}
