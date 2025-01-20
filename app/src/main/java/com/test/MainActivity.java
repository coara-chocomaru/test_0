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

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
    private static final String LOG_DIR = Environment.getExternalStorageDirectory().getPath() + "/TestLogs/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ここで final を使って定義
        final TextView statusTextView = findViewById(R.id.statusTextView);
        final ScrollView scrollView = findViewById(R.id.scrollView);

        // ストレージの権限を確認
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        } else {
            startExecution(statusTextView, scrollView);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
                // 再度 final 変数を使って実行
                final TextView statusTextView = findViewById(R.id.statusTextView);
                final ScrollView scrollView = findViewById(R.id.scrollView);
                startExecution(statusTextView, scrollView);
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startExecution(final TextView statusTextView, final ScrollView scrollView) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> executeCommand(1000000, statusTextView, scrollView));
        executor.submit(() -> executeCommandForLs(500000, statusTextView, scrollView));
    }

    private void executeCommand(int maxIterations, final TextView statusTextView, final ScrollView scrollView) {
        StringBuilder commandBuilder = new StringBuilder("/system/bin/sh -c \"");

        for (int i = 1; i <= maxIterations; i++) {
            commandBuilder.append("/system/bin/sh -c \"");
        }

        commandBuilder.append("id\"");

        executeShellCommand(commandBuilder.toString(), statusTextView, scrollView);
    }

    private void executeCommandForLs(int maxIterations, final TextView statusTextView, final ScrollView scrollView) {
        StringBuilder commandBuilder = new StringBuilder("/system/bin/sh -c \"");

        for (int i = 1; i <= maxIterations; i++) {
            commandBuilder.append("/system/bin/sh -c \"");
        }

        commandBuilder.append("ls\"");

        executeShellCommand(commandBuilder.toString(), statusTextView, scrollView);
    }

    private void executeShellCommand(String command, final TextView statusTextView, final ScrollView scrollView) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                // ラムダ式内で statusTextView を使用するため、final にする
                runOnUiThread(() -> {
                    statusTextView.append(line + "\n");
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                });
            }

            logToFile("Executed command: " + command);
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
}
