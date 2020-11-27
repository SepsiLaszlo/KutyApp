package hu.bme.aut.kutyapp.data

import androidx.room.*

@Dao
interface DogDao {
    @Query("SELECT * FROM shoppingitem")
    fun getAll(): List<DogItem>

    @Insert
    fun insert(dogItems: DogItem): Long

    @Update
    fun update(dogItem: DogItem)

    @Delete
    fun deleteItem(dogItem: DogItem)
}