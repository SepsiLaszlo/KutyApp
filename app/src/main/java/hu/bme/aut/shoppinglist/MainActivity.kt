package hu.bme.aut.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import hu.bme.aut.shoppinglist.adapter.ShoppingAdapter
import hu.bme.aut.shoppinglist.data.ShoppingItem
import hu.bme.aut.shoppinglist.data.ShoppingListDatabase
import hu.bme.aut.shoppinglist.fragments.NewShoppingItemDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), ShoppingAdapter.ShoppingItemClickListener,
    NewShoppingItemDialogFragment.NewShoppingItemDialogListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShoppingAdapter
    companion object{
        lateinit var database: ShoppingListDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
//        applicationContext.deleteDatabase("shopping-list")

        database = Room.databaseBuilder(
            applicationContext,
            ShoppingListDatabase::class.java,
            "shopping-list"
        ).build()
        initRecyclerView()
        fab.setOnClickListener {
            val intent = Intent(this, BrowserActivity::class.java)
            startActivity(intent)
        }
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

    override fun onItemChanged(item: ShoppingItem) {
        thread {
            database.shoppingItemDao().update(item)
            Log.d("MainActivity", "ShoppingItem update was successful")
        }
    }

    override fun onItemDelete(item: ShoppingItem) {
        thread {
            database.shoppingItemDao().deleteItem(item)
            Log.d("MainActivity", "ShoppingItem deleted was successful")

            runOnUiThread {
                adapter.removeItem(item)
            }
        }
    }


    override fun onShoppingItemCreated(newItem: ShoppingItem) {
        thread {
            val newId = database.shoppingItemDao().insert(newItem)
            val newShoppingItem = newItem.copy(
                id = newId
            )
            runOnUiThread {
                adapter.addItem(newShoppingItem)
            }
        }
    }
}