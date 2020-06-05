package com.example.howhowspeak

import android.app.Activity
import android.os.AsyncTask
import android.os.Environment
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import java.io.BufferedOutputStream
import java.io.DataOutputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import java.util.logging.Logger

class Socket_Client: AsyncTask<String, Float, String>() {
    lateinit var act:MainActivity
    lateinit var bar:ProgressBar
    fun setact(act:MainActivity) {
        this.act=act
    }

    override fun onPreExecute() {
        var menu = act.findViewById<ConstraintLayout>(R.id.menu)
        bar= ProgressBar(act)
        bar.x=500F
        bar.y=1000F
        menu.addView(bar)
        bar.visibility=View.VISIBLE
    }
    override fun doInBackground(vararg params: String?): String {
        var flag="Fail"
        var serveradd:InetAddress
        var socket_add:SocketAddress
        var socket:Socket
        val file_add= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath+"/test.mp4"
        try {
            serveradd= InetAddress.getByName("192.168.100.102")
            socket_add = InetSocketAddress(serveradd,5555)
            socket= Socket()
            socket.connect(socket_add,10000)

            var out = DataOutputStream(socket.getOutputStream())
            out.writeUTF(params[0])
            out.flush()



            var byteArray=ByteArray(14000000)
            var IS = socket.getInputStream()
            var bos = BufferedOutputStream(FileOutputStream(file_add))
            var bytesRead = IS.read(byteArray,0,byteArray.size)
            var current = bytesRead;

            do {
                bytesRead=IS.read(byteArray,current,(byteArray.size-current))
                if (bytesRead>=0){
                    current+=bytesRead
                }
            }while (bytesRead>-1)

            bos.write(byteArray,0,current)
            bos.flush()

            flag="success"

            socket.close()
            bos.close()

        }catch (e:Exception) {
            Logger.getLogger("Socket Error").warning(e.toString())

        }
        return flag
    }

    override fun onPostExecute(result: String?) {
        bar.visibility=View.INVISIBLE
        act.play()
    }
}