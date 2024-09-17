package com.example.bai2_thuchanh1;

import static android.os.Looper.getMainLooper;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView tvThread1;
    TextView tvThread2;
    TextView tvThread3;
    Button btnThread1, btnThread2, btnThread3;
    Handler thread1Handler, thread2Handler, thread3Handler;
    Handler mainHandler;
    HandlerThread handlerThread1, handlerThread2, handlerThread3;
    private boolean isThread1Running = false;
    private boolean isThread2Running = false;
    private boolean isThread3Running = false;
    private static final int MSG_THREAD1 = 1;
    private static final int MSG_THREAD2 = 2;
    private static final int MSG_THREAD3 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gán các TextView và Button
        tvThread1 = findViewById(R.id.tvThread1);
        tvThread2 = findViewById(R.id.tvThread2);
        tvThread3 = findViewById(R.id.tvThread3);
        btnThread1 = findViewById(R.id.btnThread1);
        btnThread2 = findViewById(R.id.btnThread2);
        btnThread3 = findViewById(R.id.btnThread3);

        // Main handler để cập nhật UI từ luồng khác
        mainHandler = new Handler(getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_THREAD1:
                        tvThread1.setText("Thread 1: " + msg.arg1);
                        return true;
                    case MSG_THREAD2:
                        tvThread2.setText("Thread 2: " + msg.arg1);
                        return true;
                    case MSG_THREAD3:
                        tvThread3.setText("Thread 3: " + msg.arg1);
                        return true;
                    default:
                        return false;
                }
            }
        });

        btnThread1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isThread1Running) {
                    stopThread1();
                } else {
                    startThread1();
                }
            }
        });

        btnThread2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isThread2Running) {
                    stopThread2();
                } else {
                    startThread2();
                }
            }
        });

        btnThread3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isThread3Running) {
                    stopThread3();
                } else {
                    startThread3();
                }
            }
        });
    }

    private void startThread1() {
        handlerThread1 = new HandlerThread("Thread1");
        handlerThread1.start();
        thread1Handler = new Handler(handlerThread1.getLooper());
        isThread1Running = true;

        thread1Handler.post(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                while (isThread1Running) {
                    int randomNumber = random.nextInt(51) + 50;
                    Message msg = mainHandler.obtainMessage(MSG_THREAD1);
                    msg.arg1 = randomNumber;
                    mainHandler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void stopThread1() {
        isThread1Running = false;
        if (handlerThread1 != null) {
            handlerThread1.quitSafely();
        }
    }

    private void startThread2() {
        handlerThread2 = new HandlerThread("Thread2");
        handlerThread2.start();
        thread2Handler = new Handler(handlerThread2.getLooper());
        isThread2Running = true;

        thread2Handler.post(new Runnable() {
            @Override
            public void run() {
                int oddNumber = 1;
                while (isThread2Running) {
                    Message msg = mainHandler.obtainMessage(MSG_THREAD2);
                    msg.arg1 = oddNumber;
                    mainHandler.sendMessage(msg);
                    oddNumber += 2;
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void stopThread2() {
        isThread2Running = false;
        if (handlerThread2 != null) {
            handlerThread2.quitSafely();
        }
    }

    private void startThread3() {
        handlerThread3 = new HandlerThread("Thread3");
        handlerThread3.start();
        thread3Handler = new Handler(handlerThread3.getLooper());
        isThread3Running = true;

        thread3Handler.post(new Runnable() {
            @Override
            public void run() {
                int number = 0;
                while (isThread3Running) {
                    Message msg = mainHandler.obtainMessage(MSG_THREAD3);
                    msg.arg1 = number;
                    mainHandler.sendMessage(msg);
                    number++;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void stopThread3() {
        isThread3Running = false;
        if (handlerThread3 != null) {
            handlerThread3.quitSafely();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThread1();
        stopThread2();
        stopThread3();
    }
}

