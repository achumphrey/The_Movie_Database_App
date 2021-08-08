package com.example.themoviedatabaseapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.themoviedatabaseapp.model.tvdetails.TVShowDetails

@Database(entities = [TVShowDetails::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TVShowDatabase : RoomDatabase() {

    abstract fun tvDao(): TVDAO

    companion object {

        @Volatile
        private var INSTANCE: TVShowDatabase? = null

        fun getDatabase(context: Context): TVShowDatabase?{

            val tempInstances = INSTANCE
            if(INSTANCE != null){
                return tempInstances
            }
            synchronized(this){
                val instances = Room.databaseBuilder(
                    context.applicationContext,
                    TVShowDatabase::class.java,
                    "show_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instances
            }
            return INSTANCE
        }
    }
}