package com.test;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ScrollView;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final String LOG_DIR = Environment.getExternalStorageDirectory().getPath() + "/TestLogs/";

    private TextView statusTextView;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.statusTextView);
        scrollView = findViewById(R.id.scrollView);

        
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        } else {
            startExecution();
        }
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

    
        executor.submit(() -> executeCommand("sh -c 'for i in {1..1000000}; do echo $i; done; id'", 1000000));
        executor.submit(() -> executeCommand("sh -c 'for i in {1..500000}; do echo $i; done; ls'", 500000));
    }

    
    private void executeCommand(String command, int stopAt) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
    
                runOnUiThread(() -> {
                    statusTextView.append(line + "\n");
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);  
                });
                count++;

                if (count == stopAt) {
            
                    Process finalProcess = Runtime.getRuntime().exec("id");
                    BufferedReader finalReader = new BufferedReader(new InputStreamReader(finalProcess.getInputStream()));
                    while ((line = finalReader.readLine()) != null) {
                        runOnUiThread(() -> {
                            statusTextView.append(line + "\n");
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);  
                        });
                    }
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
            File logDir = new File(LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            File logFile = new File(logDir, getCurrentDateTime() + ".txt");
            if (!logFile.exists()) {
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

    
    private void requestPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("This app requires storage permission to save logs.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
