package com.example.yemektariflerimuygulamasi

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.yemektariflerimuygulamasi.databinding.RecyclerRowBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.NonDisposableHandle.parent

class TarifAdapter(var context: Context, val tarifList:ArrayList<Tarifler>) : RecyclerView.Adapter<TarifAdapter.TarifHolder>() {

    class TarifHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    var onItemClick : ((Tarifler) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarifHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TarifHolder(binding)
    }

    override fun getItemCount(): Int {
        return tarifList.size
    }

    override fun onBindViewHolder(holder: TarifHolder, position: Int) {
        holder.binding.recyclerTextView.text = tarifList.get(position).ad
        holder.binding.itemImage.setImageBitmap(BitmapFactory.decodeByteArray(tarifList.get(position).image, 0, tarifList.get(position).image.size))
        holder.binding.seceneklerImage.setOnClickListener{popUpMenu(it, viewHolder = holder)}
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(tarifList.get(position))
        }
    }

    fun popUpMenu(view: View, viewHolder: RecyclerView.ViewHolder) {
        val popupMenu = PopupMenu(context,view)
        var pos = viewHolder.adapterPosition
        popupMenu.inflate(R.menu.show_menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.btnSil -> {
                    val alert = AlertDialog.Builder(context)
                    alert.setTitle("Tarif Silinecek")
                    alert.setMessage("Silmek istediğinize emin misiniz?")
                    alert.setPositiveButton("Evet") {dialog, which ->

                        try {
                            val database = context.openOrCreateDatabase("Tarifler", AppCompatActivity.MODE_PRIVATE,null)
                            val sqlString = "DELETE FROM tarifler WHERE id = (?)"
                            val statement = database.compileStatement(sqlString)
                            statement.bindLong(1,tarifList.get(pos).id.toLong())
                            statement.execute()

                        }catch (e: java.lang.Exception){
                            e.printStackTrace()
                        }

                        val intent = Intent(context,TariflerActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(context,intent, bundleOf())
                    }
                    alert.setNegativeButton("Hayır") {dialog, which ->
                        Toast.makeText(context,"Silme işlemi iptal edildi!",Toast.LENGTH_LONG).show()
                    }
                    alert.show()
                    true
                }
                else -> true
            }

        }
        popupMenu.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenu)
        menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java).invoke(menu,true)
    }
}