package pt.isec.a2022147797.p03teste

import android.app.Application
import android.content.res.Configuration
import android.util.Log

class MyApp : Application() {

    companion object{
        const val TAG = "MyApp"
    }

    private var _my_value = 0
    val my_value : Int
        get() = ++_my_value

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG,"*** onCreate: $my_value")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.i(TAG,"onTrimMemory: $my_value")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i(TAG,"onConfigurationChanged: $my_value")
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.i(TAG,"onTerminate: $my_value")
    }
}