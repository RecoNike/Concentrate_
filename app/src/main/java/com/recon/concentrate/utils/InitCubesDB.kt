package com.recon.concentrate.utils

import com.recon.concentrate.DB.CubeDao
import com.recon.concentrate.DB.CubeEntity

//
// Run Once
//

class InitCubesDB(private val cubeDao: CubeDao) {
    suspend fun initializeCubes() {
        val cubes = listOf(
            CubeEntity("Basic", false, "Common"),
            CubeEntity("Softy", false, "Common"),
            CubeEntity("Eye", false, "Common"),
            CubeEntity("Jelly", false, "Common"),
            CubeEntity("Sapphire", false, "Common"),
            CubeEntity("Dirt", false, "Common"),
            CubeEntity("Scratched", false, "Common"),


            CubeEntity("Cactus", false, "Rare"),
            CubeEntity("Coffee Cup", false, "Rare"),
            CubeEntity("Iron", false, "Rare"),
            CubeEntity("Water Glass", false, "Rare"),
            CubeEntity("Dissolved", false, "Rare"),
            CubeEntity("Apple", false, "Rare"),


            CubeEntity("Magma", false, "Epic"),
            CubeEntity("Crystal", false, "Epic"),
            CubeEntity("Ice-Cream", false, "Epic"),
            CubeEntity("Neon", false, "Epic"),


            CubeEntity("Elf`s Stone", false, "Legendary"),
            CubeEntity("Mechanical", false, "Legendary"),
            CubeEntity("Sci-Fi", false, "Legendary")
        )

        cubes.forEach { cube ->
            cubeDao.insertCube(cube)
        }
    }
}