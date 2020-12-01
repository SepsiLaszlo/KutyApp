package hu.bme.aut.kutyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.bumptech.glide.Glide

class ViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val dogUrl = intent.getStringExtra("DogUrl");
         Glide.with(this)
                .load(dogUrl)
                .into(findViewById(R.id.viewDogImage))
        findViewById<TextView>(R.id.viewDogName).text = dogUrl?.let { dogBreed(it) };
        findViewById<Button>(R.id.viewBackButton).setOnClickListener {
             val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}