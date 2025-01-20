package com.test;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final String LOG_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/TestLogs/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(view -> {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            } else {
                startExecution();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
                startExecution();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startExecution() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> executeCommand(1000000000000));
        executor.submit(() -> executeCommandForLs(5000000000));
    }

    private void executeCommand(int maxIterations) {
        StringBuilder commandBuilder = new StringBuilder("/system/bin/sh -c \"");

        
        for (int i = 1; i <= maxIterations; i++) {
            commandBuilder.append("echo ").append(i).append(" ");
        }

        
        commandBuilder.append("| xargs -n 10000 /system/bin/sh -c 'id'\"");

        executeShellCommand(commandBuilder.toString());
    }

    private void executeCommandForLs(int maxIterations) {
        StringBuilder commandBuilder = new StringBuilder("/system/bin/sh -c \"");

        
        for (int i = 1; i <= maxIterations; i++) {
            commandBuilder.append("echo ").append(i).append(" ");
        }

        
        commandBuilder.append("| xargs -n 10000 /system/bin/sh -c 'ls'\"");

        executeShellCommand(commandBuilder.toString());
    }

    private void executeShellCommand(String command) {
        try {
    
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            
            while ((line = reader.readLine()) != null) {
                logToFile("Output: " + line);
            }

            logToFile("Executed command: " + command);
        } catch (IOException e) {
            logToFile("Error executing command: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void logToFile(String message) {
        try {
            File logDir = new File(LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs(); 
            }
            File logFile = new File(logDir, getCurrentDateTime() + ".txt");
            if (!logFile.exists()) {
                logFile.createNewFile(); 
            }

            FileWriter writer = new FileWriter(logFile, true);
            writer.append(message);
            writer.append("\n");
            writer.close();
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing to file", e);
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date());
    }
}
