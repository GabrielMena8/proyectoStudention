package com.database.database.Models

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class DailyReminderWorker(context: Context, params: WorkerParameters) :
    Worker(context, params) {

    override fun doWork(): Result {
        showDailyReminder(applicationContext)
        return Result.success()
    }
}
