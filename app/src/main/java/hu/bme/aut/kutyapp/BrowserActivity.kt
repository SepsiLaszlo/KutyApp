package hu.bme.aut.kutyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import hu.bme.aut.kutyapp.data.DogItem
import hu.bme.aut.kutyapp.model.DogData
import hu.bme.aut.kutyapp.network.NetworkManager
import kotlin.concurrent.thread

class BrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        findViewById<Button>(R.id.loadNewDogButton).setOnClickListener {
            loadDog()
        }

        findViewById<Button>(R.id.saveDogButton).setOnClickListener {
            saveDog()
        }

        findViewById<Button>(R.id.backButton).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        loadDog()
    }

    var currentDog: DogData? = null;

    fun loadDog() {
        NetworkManager.getDog(::displayDogData, ::showError)
    }

    private fun showError(throwable: Throwable) {
        throwable.printStackTrace()
        Toast.makeText(
            this,
            "Network request error occurred, check LOG",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun saveDog() {
        if (currentDog == null) return

        thread {
            if (MainActivity.database.dogDao().getByUrl(currentDog!!.message).size == 0) {
                MainActivity.database.dogDao().insert(DogItem(null, currentDog!!.message))
            } else {
                runOnUiThread {

                    var toast = Toast.makeText(
                        applicationContext,
                        "Nem lehet többször kedvelni :(",
                        Toast.LENGTH_SHORT
                    )
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show()
                }
            }
        }
    }

    private fun displayDogData(receivedDogData: DogData) {
        currentDog = receivedDogData;
        findViewById<TextView>(R.id.dogName).text = dogBreed(receivedDogData.message)
        Glide.with(this)
            .load(receivedDogData.message)
            .into(findViewById(R.id.dogImageButton))
    }

}