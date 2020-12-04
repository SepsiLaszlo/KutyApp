package hu.bme.aut.kutyapp.data

import androidx.room.*

@Dao
interface DogDao {
    @Query("SELECT * FROM dogs")
    fun getAll(): List<DogItem>

    @Query("SELECT * FROM dogs WHERE imageUrl = :url")
    fun getByUrl(url:String): List<DogItem>

    @Insert
    fun insert(dogItems: DogItem): Long

    @Update
    fun update(dogItem: DogItem)

    @Delete
    fun deleteItem(dogItem: DogItem)
}