package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    private static final String LOG_DIR = "/data/data/com.test/cache/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExecutorService executor = Executors.newFixedThreadPool(2);

      
        executor.submit(() -> executeCommand("sh -c 'for i in {1..1000000}; do echo $i; done; id'", 1000000));
        executor.submit(() -> executeCommand("sh -c 'for i in {1..500000}; do echo $i; done; ls'", 500000));

      
        TextView statusTextView = findViewById(R.id.statusTextView);
        statusTextView.setText("Processing started...");
    }

    
    private void executeCommand(String command, int stopAt) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            int count = 0;
            while (count < stopAt) {
                count++;
                if (count == stopAt) {
                  
                    Process finalProcess = Runtime.getRuntime().exec("id");
                    logToFile("ID Command executed after " + stopAt + " iterations.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private void logToFile(String message) {
        try {
            File logFile = new File(LOG_DIR, getCurrentDateTime() + ".txt");
            if (!logFile.exists()) {
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            writer.append(message);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date());
    }
}
