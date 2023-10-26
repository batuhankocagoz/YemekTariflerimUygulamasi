package com.example.yemektariflerimuygulamasi

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.yemektariflerimuygulamasi.databinding.ActivityDetayBinding
import com.example.yemektariflerimuygulamasi.databinding.ActivityMainBinding

class DetayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val tarif = intent.getParcelableExtra<Tarifler>("tarif")
        if (tarif != null) {
            val imageView : ImageView = binding.detayImage
            val tarifAdi : TextView = binding.detayText
            val tarifMalzemesi : TextView = binding.malzText
            val tarifYapilisi : TextView = binding.yapilisText

            tarifAdi.text = tarif.ad
            tarifMalzemesi.text = tarif.malz
            tarifYapilisi.text = tarif.yapilis
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(tarif.image, 0, tarif.image.size))
        }
    }
}