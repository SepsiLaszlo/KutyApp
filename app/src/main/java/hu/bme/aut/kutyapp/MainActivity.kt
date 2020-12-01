package hu.bme.aut.kutyapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import hu.bme.aut.kutyapp.adapter.ShoppingAdapter
import hu.bme.aut.kutyapp.data.DogItem
import hu.bme.aut.kutyapp.data.DogDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.concurrent.thread

fun dogBreed(url: String): String {
    return url.split("/")[4].split('-').map { it.capitalize() }.joinToString(separator = " ")
}

class MainActivity : AppCompatActivity(), ShoppingAdapter.ShoppingItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShoppingAdapter

    companion object {
        lateinit var database: DogDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
//        applicationContext.deleteDatabase("shopping-list")

        database = Room.databaseBuilder(
                applicationContext,
                DogDatabase::class.java,
                "shopping-list"
        ).build()
        initRecyclerView()
        fab.setOnClickListener {
            val intent = Intent(this, BrowserActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        loadItemsInBackground()
    }


    private fun initRecyclerView() {
        recyclerView = MainRecyclerView
        adapter = ShoppingAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.shoppingItemDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemChanged(item: DogItem) {
        thread {
            database.shoppingItemDao().update(item)
            Log.d("MainActivity", "ShoppingItem update was successful")
        }
    }

    override fun onItemDelete(item: DogItem) {
        thread {
            database.shoppingItemDao().deleteItem(item)
            Log.d("MainActivity", "ShoppingItem deleted was successful")

            runOnUiThread {
                adapter.removeItem(item)
            }
        }
    }

    override fun onItemSelected(item: DogItem?) {
        Log.d("MainActivity", "ShoppingItem update was successful")
    }


}