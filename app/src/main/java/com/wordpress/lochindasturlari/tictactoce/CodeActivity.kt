package com.wordpress.lochindasturlari.tictactoce
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_code.*

var isCodeMaker = true;
var username = "null";
var codeFound = false
var checkTemp = true
var keyValue:String = "null"
class CodeActivity : AppCompatActivity() {
    lateinit var sharedPreference: SharedPreferences
    var user:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)

        sharedPreference=getSharedPreferences("Username", Context.MODE_PRIVATE)


        Create.setOnClickListener{
            username = "null";
            codeFound = false
            checkTemp = true
            keyValue= "null"
            username = sharedPreference.getString("Username","")!!
            Create.visibility = View.GONE
            Join.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            if(username != "null" && username != null && username != "") {

                isCodeMaker = true;
                FirebaseDatabase.getInstance().reference.child("data").addValueEventListener(object  :ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                            var check = isValueAvailable(snapshot , username)

                            Handler().postDelayed({
                                if(check == true) {
                                    Create.visibility = View.VISIBLE
                                    Join.visibility = View.VISIBLE
                                    progressBar.visibility = View.GONE

                                }
                                else{
                                    FirebaseDatabase.getInstance().reference.child("data").push().setValue(username)
                                    isValueAvailable(snapshot,username)
                                    checkTemp = false
                                    Handler().postDelayed({
                                        accepted()
                                        errorMsg("Iltimos qayta urinib ko'ring")
                                    } , 300)

                                }
                            }, 4000)



                    }

                })
            }
            else
            {
                Create.visibility = View.VISIBLE
                Join.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
        Join.setOnClickListener{

            alert()
            username = "null";
            codeFound = false
            checkTemp = true
            keyValue= "null"
            username =user
            if(username != "null" && username != null && username != "") {
                Create.visibility = View.GONE
                Join.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                isCodeMaker = false;
                FirebaseDatabase.getInstance().reference.child("data").addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                        TODO("Not yet implemented")

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        var data:Boolean = isValueAvailable(snapshot , username)

                            Handler().postDelayed({
                                if(data == true) {
                                    codeFound = true
                                    accepted()
                                    Create.visibility = View.VISIBLE
                                    Join.visibility = View.VISIBLE
                                    progressBar.visibility = View.GONE
                                }
                                else{
                                    Create.visibility = View.VISIBLE
                                    Join.visibility = View.VISIBLE
                                    progressBar.visibility = View.GONE
                                    errorMsg("Username xato !!!")
                                }
                            } , 4000)


                    }


                })

            }

        }

    }

    fun accepted() {
        startActivity(Intent(this, ThirdPage::class.java));
        Create.visibility = View.VISIBLE
        Join.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        
    }

    fun errorMsg(value : String){
        Toast.makeText(this , value  , Toast.LENGTH_SHORT).show()
    }

    fun isValueAvailable(snapshot: DataSnapshot , user_name : String): Boolean {
        var data = snapshot.children
        data.forEach{
            var value = it.getValue().toString()
            if(value == user_name)
            {
                 keyValue = it.key.toString()
                return true;
            }
        }
        return false
    }
    fun alert(){

        val inflate=layoutInflater
        val inflate_view=inflate.inflate(R.layout.alerd_layout,null)
        val username=inflate_view.findViewById(R.id.username_alert) as EditText

        val alertDialog= AlertDialog.Builder(this)
        alertDialog.setTitle("Username kiriting")
        alertDialog.setView(inflate_view)
        alertDialog.setCancelable(false)
        alertDialog.setNegativeButton("chiqish"){dialog,which ->

            dialog.cancel()

        }
        alertDialog.setPositiveButton("Tasdiqlash"){dialog,which ->

             user=username.text.toString()




        }

        val dialog=alertDialog.create()
        dialog.show()

    }

}