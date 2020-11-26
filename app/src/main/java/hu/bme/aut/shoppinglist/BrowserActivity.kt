package hu.bme.aut.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import hu.bme.aut.shoppinglist.data.ShoppingItem
import hu.bme.aut.shoppinglist.model.DogData
import hu.bme.aut.shoppinglist.network.NetworkManager
import kotlin.concurrent.thread

class BrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        findViewById<Button>(R.id.loadNewDogButton).setOnClickListener {
            buttonFnc()
        }


    }
    
    git add . g

    fun buttonFnc() {
        NetworkManager.getDog(::displayDogData, ::showError)
    }


    private fun displayDogData(receivedDogData: DogData) {
        System.out.println(receivedDogData.message)
        thread {
            MainActivity.database.shoppingItemDao().insert(ShoppingItem(null,receivedDogData.message))
        }

        findViewById<TextView>(R.id.dogName).text = receivedDogData.message
        Glide.with(this)
                .load(receivedDogData.message)
                .into(findViewById(R.id.dogImageButton))
    }

    private fun showError(throwable: Throwable) {
        throwable.printStackTrace()
        Toast.makeText(
                this,
                "Network request error occurred, check LOG",
                Toast.LENGTH_SHORT
        ).show()
    }
}