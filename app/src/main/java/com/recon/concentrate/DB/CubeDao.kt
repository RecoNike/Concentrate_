package com.recon.concentrate.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CubeDao {

    @Query("SELECT * FROM cubes")
    suspend fun getAllCubes(): List<CubeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCube(cube: CubeEntity)

    @Update
    suspend fun updateCube(cube: CubeEntity)
}
