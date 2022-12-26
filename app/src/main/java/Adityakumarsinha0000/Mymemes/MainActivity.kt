package Adityakumarsinha0000.Mymemes

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var currentmemeurl:String?=null

//    linked list for list of prev 50 memes
    var plist =LinkedList<String>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loadmeme()
    }

    private fun loadmeme(){

        progress.visibility=View.VISIBLE
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
        val JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url ,null,
            { response ->
            currentmemeurl=response.getString("url")
            Glide.with(this).load(currentmemeurl).listener(object :RequestListener<Drawable>
            {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility=View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility=View.GONE
                    return false
                }
            }).into(memeimage)
            },
            {
                Response.ErrorListener {
                    Toast.makeText(this , "Something went wrong",Toast.LENGTH_LONG).show()
                }
            })
//
// Add the request to the RequestQueue.
        queue.add(JsonObjectRequest)

//        updating the linked list with every new meme
        if(plist.size>100)
        {
            plist.removeFirst()
        }
        plist.addLast(currentmemeurl)


    }


    fun sharememe(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"check this meme out!  "+currentmemeurl)
        val chooser=Intent.createChooser(intent,"share this meme using...")
        startActivity(chooser)
    }



    fun nextmeme(view: View) {

        loadmeme()

    }


    @SuppressLint("SuspiciousIndentation")
    fun prevmeme(view: View) {
//        val uri: Uri = Uri.parse(plist.last)
//        val intent = Intent(Intent.ACTION_VIEW, uri)
//        val intent=Intent(Intent.ACTION_VIEW, plist.last)
//        startActivity(intent)

        if (plist.size == 0) {
            loadmeme()
            Toast.makeText(this, "cant go back anymore", Toast.LENGTH_LONG).show()
        } else {
            Glide.with(this).load(plist.last).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility = View.GONE
                    return false
                }
            }).into(memeimage)
            plist.removeLast()
        }
    }
}


