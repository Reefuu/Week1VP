package com.example.week1vp

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import Database.GlobalVar
import Model.User
import androidx.activity.result.contract.ActivityResultContracts
import com.example.week1vp.databinding.ActivityAddEditMovieBinding

class AddEditMovie : AppCompatActivity() {
    private lateinit var viewBind: ActivityAddEditMovieBinding
    private lateinit var movie: User
    var position = -1
    var image: String = ""

    private val GetResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val uri = it.data?.data
            if(uri != null){
                baseContext.getContentResolver().takePersistableUriPermission(uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            viewBind.imageView2.setImageURI(uri)
            image = uri.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBind = ActivityAddEditMovieBinding.inflate(layoutInflater)
        setContentView(viewBind.root)
        supportActionBar?.hide()
        getintent()
        listener()
    }
    private fun getintent(){
        position = intent.getIntExtra("position", -1)
        if(position != -1){
            val movie = GlobalVar.listDataMovie[position]
            viewBind.toolbar2.title = "Edit Hewan"
            viewBind.movieAdd.text = "Edit"
            viewBind.imageView2.setImageURI(Uri.parse(GlobalVar.listDataMovie[position].imageUri))
            viewBind.Rating.editText?.setText(movie.rating.toString())
            viewBind.Title.editText?.setText(movie.title)
            viewBind.Genre.editText?.setText(movie.genre)
        }
    }

    private fun listener(){
        viewBind.imageView2.setOnClickListener{
            val myIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            myIntent.type = "image/*"
            GetResult.launch(myIntent)
        }

        viewBind.movieAdd.setOnClickListener{
            var title = viewBind.Title.editText?.text.toString().trim()
            var rating = viewBind.Rating.editText?.text.toString().trim()
            var genre = viewBind.Genre.editText?.text.toString().trim()

            movie = User(title, rating, genre)
            checker()
        }

        viewBind.toolbar2.getChildAt(1).setOnClickListener {
            finish()
        }
    }

    private fun checker()
    {
        var isCompleted:Boolean = true

        if(movie.title!!.isEmpty()){
            viewBind.Title.error = "this cannot be empty"
            isCompleted = false
        }else{
            viewBind.Title.error = ""
        }

        if(movie.genre!!.isEmpty()){
            viewBind.Genre.error = "this cannot be empty"
            isCompleted = false
        }else{
            viewBind.Genre.error = ""
        }




        movie.imageUri = image

        if(viewBind.Rating.editText?.text.toString().isEmpty() || viewBind.Rating.editText?.text.toString().toInt() < 0)
        {
            viewBind.Rating.error = "this cannot be empty"
            isCompleted = false
        }

        if(isCompleted == true)
        {
            if(position == -1)
            {
                movie.rating = viewBind.Rating.editText?.text.toString()
                GlobalVar.listDataMovie.add(movie)

            }else
            {
                movie.rating = viewBind.Rating.editText?.text.toString()
                GlobalVar.listDataMovie[position] = movie
            }
            finish()
        }
    }
}