package com.example.turkmen_mehmetcan;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainApplication extends Application {
    ExecutorService srv = Executors.newCachedThreadPool();
}
