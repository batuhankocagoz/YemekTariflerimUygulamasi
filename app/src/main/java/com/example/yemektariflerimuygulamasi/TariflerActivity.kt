package com.example.yemektariflerimuygulamasi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.yemektariflerimuygulamasi.databinding.ActivityTariflerBinding


class TariflerActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTariflerBinding
    private lateinit var tarifList : ArrayList<Tarifler>
    private lateinit var tarifAdapter : TarifAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTariflerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        tarifList = ArrayList<Tarifler>()

        tarifAdapter = TarifAdapter(this,tarifList)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = tarifAdapter

        tarifAdapter.onItemClick = {
            val intent = Intent(this,DetayActivity::class.java)
            intent.putExtra("tarif",it)
            startActivity(intent)
        }

        try {
            val database = this.openOrCreateDatabase("Tarifler", MODE_PRIVATE,null)

            val cursor = database.rawQuery("SELECT * FROM tarifler",null)
            val tarifAdiIx = cursor.getColumnIndex("tarifadi")
            val tarifMalzIx = cursor.getColumnIndex("tarifmalzeme")
            val tarifYapIx = cursor.getColumnIndex("tarifyapilis")
            val imageIx = cursor.getColumnIndex("image")
            val idIx = cursor.getColumnIndex("id")

            while (cursor.moveToNext()) {
                val ad = cursor.getString(tarifAdiIx)
                val malz = cursor.getString(tarifMalzIx)
                val yapilis = cursor.getString(tarifYapIx)
                val id = cursor.getInt(idIx)
                val resim = cursor.getBlob(imageIx)
                val tarif = Tarifler(ad,id,resim,malz,yapilis)
                tarifList.add(tarif)
            }
            tarifAdapter.notifyDataSetChanged()

            cursor.close()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}