package pt.isec.a2022147797.p03teste

import android.app.Activity
import android.os.Bundle
import android.util.Log

const val TAG = "MyActivity"

class MyActivity : Activity() {
    companion object{
        const val TAG = "MyActivity"
    }

    private var _my_value = 0
    val my_value : Int
        get() = ++_my_value

    val app : MyApp by lazy { application as MyApp }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_activity)

        Log.i(TAG,"onCreate: $my_value ${app.my_value}")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG,"onStart: $my_value")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG,"onRestart: $my_value")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG,"onResume: $my_value ${MyObject.my_value}")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG,"onPause: $my_value")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"onStop: $my_value")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"onDestroy: $my_value")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("mamamy_value", my_value)
        Log.i(TAG,"onSaveInstanceState: $my_value")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        _my_value = savedInstanceState.getInt("mamamy_value")
        Log.i(TAG,"onRestoreInstanceState: $my_value")
    }
}