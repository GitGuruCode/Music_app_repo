package com.example.vmusic

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.Properties
import android.widget.Toast
import androidx.core.content.getSystemService
import com.example.vmusic.databinding.ActivityFeedbackBinding
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Session.getInstance
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class FeedbackActivity : AppCompatActivity() {
    lateinit var binding:ActivityFeedbackBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title="Feedback"
        binding.fbsend.setOnClickListener{
            val userFeedback=binding.fbtxt.text.toString()
            val subject=binding.fbs.text.toString()
            val myUsername="Carevikas25sep@gmail.com"
            val passWord="dhoni0007"
            val cm=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if(userFeedback.isNotEmpty() && subject.isNotEmpty() && cm.activeNetworkInfo?.isConnectedOrConnecting==true){
                Toast.makeText(this,"Thanks for your feedback",Toast.LENGTH_SHORT).show()
                finish()
//          Thread{
//              try {
//                  val properties=Properties()
//                  properties["mail.smtp.auth"]=true
//                  properties["mail.smtp.starttls.enable"]=true
//                  properties["mail.smtp.host"]="smtp.gmail.com"
//                  properties["mail.smtp.port"]="587"
//                  val session=Session.getInstance(properties,object:Authenticator(){
//                      override fun getPasswordAuthentication(): PasswordAuthentication {
//                          return PasswordAuthentication(myUsername,passWord)
//                      }
//                  })
//                  val mail=MimeMessage(session)
//                  mail.subject=subject
//                  mail.setText(userFeedback)
//                  mail.setFrom(InternetAddress(myUsername))
//                  mail.setRecipients(Message.RecipientType.TO,InternetAddress.parse(myUsername))
//                  Transport.send(mail)
//                  Toast.makeText(this,"Thanks for your Feedback!",Toast.LENGTH_SHORT).show()
//                  finish()
//              } catch (e:java.lang.Exception){Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()}
//          }.start()
            }
            else
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
        }
    }
}