package com.example.app

import android.app.AppOpsManager
import android.app.Application
import android.app.AsyncNotedAppOp
import android.app.SyncNotedAppOp
import android.os.Build

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val appOpsCallback = object : AppOpsManager.OnOpNotedCallback() {

                private fun logPrivateDataAccess(
                    opCode: String,
                    attributionTag: String?,
                    trace: String
                ) {
                    println("Attribution Tag: $attributionTag")
                }

                override fun onNoted(syncNotedAppOp: SyncNotedAppOp) {
                    logPrivateDataAccess(
                        syncNotedAppOp.op,
                        syncNotedAppOp.attributionTag,
                        Throwable().stackTrace.toString()
                    )
                }

                override fun onSelfNoted(syncNotedAppOp: SyncNotedAppOp) {
                    logPrivateDataAccess(
                        syncNotedAppOp.op,
                        syncNotedAppOp.attributionTag,
                        Throwable().stackTrace.toString()
                    )
                }

                override fun onAsyncNoted(asyncNotedAppOp: AsyncNotedAppOp) {
                    logPrivateDataAccess(
                        asyncNotedAppOp.op,
                        asyncNotedAppOp.attributionTag,
                        asyncNotedAppOp.message
                    )
                }

            }
            val appOpsManager = getSystemService<AppOpsManager>(AppOpsManager::class.java)!!
            appOpsManager.setOnOpNotedCallback(mainExecutor, appOpsCallback)
        }
    }
}
