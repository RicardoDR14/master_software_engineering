package pt.isec.a2022147797.aula03

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pt.isec.a2022147797.aula03.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
    //    enableEdgeToEdge()
       //setContentView(R.layout.activity_main)
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

//        val tv : TextView = findViewById(R.id.tvTexto)
//        val tv = findViewById<TextView>(R.id.tvTexto)
//        tv.text = getString(R.string.amov)

        binding.tvTexto.text = getString(R.string.amov)

        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(ProcBtn(binding.tvTexto,"Button 2"))
        binding.btn3.setOnClickListener(procBtn3)
        binding.btn4.setOnClickListener{
            binding.tvTexto.text = "Button 4"
        }
    }

    val procBtn3 = object : View.OnClickListener{
        override fun onClick(p0: View?) {
            binding.tvTexto.text="Button 3"
        }
    }

//    val procBtn3a = object : View.OnClickListener{
//        binding.tvTexto.text="Button 3"
//    }

    class ProcBtn(val tv : TextView, val msg: String): View.OnClickListener{
        override fun onClick(p0: View?) {
            tv.text = msg
        }
    }

    override fun onClick(p0: View?) {
        binding.tvTexto.text = "Button 1"
    }

    fun onButton5(view: View) {
        val btn = view as Button
        binding.tvTexto.text = "Button 5 ${btn.text}"
    }
}