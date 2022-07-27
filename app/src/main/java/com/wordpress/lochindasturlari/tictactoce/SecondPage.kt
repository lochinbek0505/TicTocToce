package com.wordpress.lochindasturlari.tictactoce
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_second_page.*
class SecondPage : AppCompatActivity() {

    var Online = true;
    lateinit var sharedPreference:SharedPreferences
    lateinit var sharedPreference2:SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_page)

        sharedPreference=getSharedPreferences("Username",Context.MODE_PRIVATE)
        sharedPreference2=getSharedPreferences("TEKSHIR",Context.MODE_PRIVATE)

        buttonOnline.setOnClickListener {
            val tekshir=sharedPreference2.getString("boolean","")
            if(tekshir=="true") {
                startActivity(Intent(this, CodeActivity::class.java))
                Online = true;
            }
            else{
                alert()
            }
        }
        buttonOffline.setOnClickListener {
            startActivity(Intent(this , MainActivity::class.java))
            Online = false;
        }
    }

    fun alert(){

        val inflate=layoutInflater
        val inflate_view=inflate.inflate(R.layout.alerd_layout,null)
        val username=inflate_view.findViewById(R.id.username_alert) as EditText

        val alertDialog=AlertDialog.Builder(this)
        alertDialog.setTitle("Username kiriting")
        alertDialog.setView(inflate_view)
        alertDialog.setCancelable(false)

        alertDialog.setNegativeButton("chiqish"){dialog,which ->

           dialog.cancel()

        }
        alertDialog.setPositiveButton("Tasdiqlash"){dialog,which ->

            val user=username.text.toString()

            val editor=sharedPreference.edit()
            val editor2=sharedPreference2.edit()


            editor.putString("Username",user)
            editor2.putString("boolean","true")

            editor.commit()
            editor2.commit()
        }

        val dialog=alertDialog.create()
        dialog.show()
    }
}