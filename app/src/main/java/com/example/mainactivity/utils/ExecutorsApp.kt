package com.example.mainactivity.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ExecutorsApp {
    val diskIO: Executor = Executors.newSingleThreadExecutor()
    val mainThread: Executor = MainThreadExecutor()

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

}