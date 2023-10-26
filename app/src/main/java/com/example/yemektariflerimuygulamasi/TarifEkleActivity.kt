package com.example.yemektariflerimuygulamasi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.yemektariflerimuygulamasi.databinding.ActivityMainBinding
import com.example.yemektariflerimuygulamasi.databinding.ActivityTarifEkleBinding
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream

class TarifEkleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTarifEkleBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var secilenBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTarifEkleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        registerLauncher()
    }

    fun tarifKaydet(view: View) {

        val tarifAdi = binding.tarifAdi.text.toString()
        val tarifMalzeme = binding.tarifMalzeme.text.toString()
        val tarifYapilis = binding.tarifYapilis.text.toString()

        if (secilenBitmap != null){
            val kucukBitmap = bitmapOlustur(secilenBitmap!!,300)

            val outputStream = ByteArrayOutputStream()
            kucukBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()

            try {
                val database = this.openOrCreateDatabase("Tarifler", MODE_PRIVATE,null)
                database.execSQL("CREATE TABLE IF NOT EXISTS tarifler(id INTEGER PRIMARY KEY, tarifadi VARCHAR, tarifmalzeme VARCHAR, tarifyapilis VARCHAR, image BLOB)")

                val sqlString = "INSERT INTO tarifler(tarifadi, tarifmalzeme, tarifyapilis, image) VALUES(?, ?, ?, ?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1,tarifAdi)
                statement.bindString(2,tarifMalzeme)
                statement.bindString(3,tarifYapilis)
                statement.bindBlob(4,byteArray)
                statement.execute()

            }catch (e: java.lang.Exception){
                e.printStackTrace()
            }

            Toast.makeText(applicationContext,"Tarif Başarıyla Eklendi",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }
    }

    fun bitmapOlustur(image : Bitmap, maxSize : Int,) : Bitmap {
        var width = image.width
        var height = image.height

        val bitmapOrani : Double = width.toDouble() / height.toDouble()

        if (bitmapOrani > 1) {
            //yatay görsel için
            width = maxSize
            val degisenHeight = width / bitmapOrani
            height = degisenHeight.toInt()

        }else{
            //dikey görsel için
            height = maxSize
            val degisenWidth = height * bitmapOrani
            width = degisenWidth.toInt()
        }
        return Bitmap.createScaledBitmap(image,width,height,true)
    }

    fun fotografEkle(view: View) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES)) {
                    Snackbar.make(view,"Galeriye gitmek için izin gerekli!",Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver", View.OnClickListener {
                        //izin istenecek
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }).show()

                }else{
                    //izin istenecek
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }

            }else {
                val intentToGaleri = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGaleri)
                //galeriye yönlendirip fotoğraf seçilecek
            }

        }else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view,"Galeriye gitmek için izin gerekli!",Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver", View.OnClickListener {
                        //izin istenecek
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()

                }else{
                    //izin istenecek
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            }else {
                val intentToGaleri = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGaleri)
                //galeriye yönlendirip fotoğraf seçilecek
            }
        }
    }

    private fun registerLauncher(){

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    val imageData = intentFromResult.data
                    if (imageData != null) {
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(this@TarifEkleActivity.contentResolver,imageData)
                                secilenBitmap = ImageDecoder.decodeBitmap(source)
                                binding.imageView.setImageBitmap(secilenBitmap)
                            }else{
                                secilenBitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageData)
                                binding.imageView.setImageBitmap(secilenBitmap)
                            }
                        }catch (e:Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                val intentToGaleri = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGaleri)
                //galeriye yönlendirip fotoğraf seçilecek
            }else{
                //permission denied
                Toast.makeText(this@TarifEkleActivity,"İzin gerekli!",Toast.LENGTH_LONG).show()
            }
        }
    }
}