package com.example.multithread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.example.multithread.databinding.ActivityMainBinding;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int threadSpeed = 500;
    private int threadSpeed2 = 500;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private boolean isEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Random randomCreate = new Random();
        Random randomSale = new Random();

        AtomicInteger count = new AtomicInteger();

        binding.run.setOnClickListener(btn -> {
            isEnabled = true;

            executorService.submit(() -> {
                while(isEnabled) {
                    try {
                        Thread.sleep(threadSpeed2);
                    } catch (InterruptedException ignored) {}
                    int random = randomCreate.nextInt(5);
                    count.addAndGet(random);

                    generateReport("Создано товаров " + random + ". Всего товаров " + count);
                }
            });

            executorService.submit(() -> {
                while (isEnabled) {
                    try {
                        Thread.sleep(threadSpeed);
                    } catch (InterruptedException ignored) {}
                    int random = randomSale.nextInt(5);
                    count.set(count.get() - random);

                    if (count.get() < 0) {
                        count.set(0);
                    }

                    generateReport("Продано товаров " + random + ". Всего товаров " + count);
                }
            });
        });

        binding.stop.setOnClickListener(btn -> {
            isEnabled = false;
        });

        binding.seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int speed, boolean b) {
                threadSpeed2 = speed;
                binding.textView3.setText("Задержка создания: " + speed);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int speed, boolean b) {
                threadSpeed = speed;
                binding.textView2.setText("Задержка продажи: " + speed);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void generateReport(String message) {
        String historyText = String.valueOf(binding.textView.getText());
        historyText = message + "\n" + historyText;
        binding.textView.setText(historyText);
    }
}