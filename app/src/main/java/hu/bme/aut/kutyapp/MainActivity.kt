package hu.bme.aut.kutyapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import hu.bme.aut.kutyapp.adapter.DogAdapter
import hu.bme.aut.kutyapp.data.DogItem
import hu.bme.aut.kutyapp.data.DogDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.concurrent.thread

fun dogBreed(url: String): String {
    return url.split("/")[4].split('-').map { it.capitalize() }.joinToString(separator = " ")
}

class MainActivity : AppCompatActivity(), DogAdapter.DogItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DogAdapter

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
        adapter = DogAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.dogDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemChanged(item: DogItem) {
        thread {
            database.dogDao().update(item)
            Log.d("MainActivity", "ShoppingItem update was successful")
        }
    }

    override fun onItemDelete(item: DogItem) {
        thread {
            database.dogDao().deleteItem(item)
            Log.d("MainActivity", "ShoppingItem deleted was successful")

            runOnUiThread {
                adapter.removeItem(item)
            }
        }
    }

    override fun onItemSelected(item: DogItem?) {
        val intent = Intent(this, ViewActivity::class.java)
        intent.putExtra("DogUrl", item!!.url)
        startActivity(intent)
    }
}