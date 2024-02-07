package com.recon.concentrate.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cubes")
data class CubeEntity(
    @PrimaryKey(autoGenerate = true)
    //val id: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "is_open")
    var isOpen: Boolean = false,

    @ColumnInfo(name = "rarity")
    var rarity: String,

)
