package com.verticalcoding.mystudentlist.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DogEntityDao {



    @Query("SELECT * FROM dogs")
    fun getAllDogs(): Flow<List<DogEntity>>

    @Query("SELECT * FROM dogs ORDER BY uid DESC LIMIT 10")
    fun getSortedDogs(): Flow<List<DogEntity>>

    @Query("SELECT * FROM dogs WHERE isLiked = 1")
    fun getAllFavDogs(): Flow<List<DogEntity>>

    @Query("UPDATE dogs SET isLiked = CASE WHEN isLiked = 1 THEN 0 ELSE 1 END WHERE uid = :id")
    suspend fun triggerFavDog(id: Int)

    @Insert
    suspend fun insertDog(dog: DogEntity)

    @Query("DELETE FROM dogs WHERE uid = :id")
    suspend fun removeDog(id: Int)

    @Query("DELETE FROM dogs WHERE name = :name AND breed = :breed")
    suspend fun deleteDogByNameAndBreed(name: String, breed: String)

    @Query("UPDATE dogs SET isLiked = :liked WHERE name = :name AND breed = :breed")
    suspend fun updateLikeStatus(name: String, breed: String, liked: Boolean)



}