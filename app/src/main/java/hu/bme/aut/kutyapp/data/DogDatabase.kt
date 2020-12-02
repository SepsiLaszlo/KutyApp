package hu.bme.aut.kutyapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DogItem::class], version = 1)
abstract class DogDatabase : RoomDatabase() {
    abstract fun dogDao(): DogDao
}