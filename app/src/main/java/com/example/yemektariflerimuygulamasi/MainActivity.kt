package com.example.yemektariflerimuygulamasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.yemektariflerimuygulamasi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tarifler.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,TariflerActivity::class.java)
            startActivity(intent)
        })

        binding.tarifekle.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,TarifEkleActivity::class.java)
            startActivity(intent)
        })


    }
}