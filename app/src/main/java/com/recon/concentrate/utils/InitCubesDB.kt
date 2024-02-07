package com.recon.concentrate.utils

import com.recon.concentrate.DB.CubeDao
import com.recon.concentrate.DB.CubeEntity

class InitCubesDB (private val cubeDao: CubeDao){
    suspend fun initializeCubes(){
        val cubes = listOf(
            CubeEntity("Basic", false, "Common"),
            CubeEntity("Softy", false, "Common"),
            CubeEntity("Eye", false, "Common"),
            CubeEntity("Jelly", false, "Common"),
            CubeEntity("Sapphire", false, "Common"),
            CubeEntity("Basic", false, "Common"),
            CubeEntity("Basic", false, "Common"),

            CubeEntity("Basic", false, "Rare"),
            CubeEntity("Basic", false, "Rare"),
            CubeEntity("Basic", false, "Rare"),
            CubeEntity("Basic", false, "Rare"),
            CubeEntity("Basic", false, "Rare"),
            CubeEntity("Basic", false, "Rare"),

            CubeEntity("Basic", false, "Epic"),
            CubeEntity("Basic", false, "Epic"),
            CubeEntity("Basic", false, "Epic"),
            CubeEntity("Basic", false, "Epic"),

            CubeEntity("Basic", false, "Legendary"),
            CubeEntity("Basic", false, "Legendary"),
            CubeEntity("Basic", false, "Legendary")
        )

        cubes.forEach{ cube->
            cubeDao.insertCube(cube)
        }
    }
}