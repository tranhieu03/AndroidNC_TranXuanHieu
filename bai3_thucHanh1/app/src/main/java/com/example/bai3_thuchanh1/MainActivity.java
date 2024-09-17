package com.example.bai3_thuchanh1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView timeTextView, countTime;
    private Button setTimeButton, startButton, stopButton;
    private int selectedHour, selectedMinute;
    private Vibrator vibrator;
    private CountDownTimer timer;
    private int vibrationSeconds = 0; // Biến để đếm thời gian rung

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTimeButton = findViewById(R.id.setTimeButton);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        countTime = findViewById(R.id.timeCountTextView);
        timeTextView = findViewById(R.id.timeTextView);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Thiết lập thời gian
        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedHour = hourOfDay;
                        selectedMinute = minute;
                        timeTextView.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });

        // Xử lý khi bấm nút Start
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTimeMillis = System.currentTimeMillis();

                Calendar now = Calendar.getInstance();
                Calendar alarmTime = Calendar.getInstance();
                alarmTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                alarmTime.set(Calendar.MINUTE, selectedMinute);
                alarmTime.set(Calendar.SECOND, 0);

                long alarmTimeMillis = alarmTime.getTimeInMillis();

                // Nếu thời gian đã chọn trước thời gian hiện tại, cộng thêm 1 ngày
                if (alarmTimeMillis <= currentTimeMillis) {
                    alarmTimeMillis += 24 * 60 * 60 * 1000;
                }

                long delay = alarmTimeMillis - currentTimeMillis;


                new CountDownTimer(delay, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        startVibration();
                    }
                }.start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibrator != null) {
                    vibrator.cancel();
                }
                if (timer != null) {
                    timer.cancel();
                }
                vibrationSeconds = 0;
                countTime.setText("");
                Toast.makeText(MainActivity.this, "dừng báo thức", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Bắt đầu quá trình rung và đếm thời gian rung
    private void startVibration() {
        vibrationSeconds = 0;

        // Đếm rung theo giây
        timer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                vibrator.vibrate(1000);
                vibrationSeconds++; // Tăng số giây đã rung
                countTime.setText("báo thức đã rung trong " + vibrationSeconds + " giây");
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
}