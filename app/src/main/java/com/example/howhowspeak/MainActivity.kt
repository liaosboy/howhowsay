package com.example.howhowspeak

import android.content.pm.PackageManager
import android.media.session.MediaController
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.net.Socket
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    lateinit var videoView:VideoView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val permissioncheck1 = ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissioncheck1 != PackageManager.PERMISSION_GRANTED){
            askpermission1()
        }

        val permissioncheck2 = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissioncheck2 != PackageManager.PERMISSION_GRANTED){
            askpermission2()
        }

        var menu = findViewById<ConstraintLayout>(R.id.menu)
        var btn =findViewById<Button>(R.id.button)
        var text = findViewById<EditText>(R.id.text)
        videoView=findViewById(R.id.videoView)
        videoView.setMediaController(android.widget.MediaController(this))

        btn.setOnClickListener(View.OnClickListener {
            start(text.text.toString())
        })

    }
    fun start(str:String){
        var send_socket=Socket_Client()
        send_socket.setact(this)
        send_socket.execute(str)
    }
    fun play() {
        var uri = Uri.parse( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath+"/test.mp4")
        Logger.getLogger("Video URI").warning(uri.toString())
        videoView.setVideoURI(uri)
        videoView.start()
    }
    fun askpermission1(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
    }
    fun askpermission2(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 100)
    }
}
