package com.recon.concentrate.utils

import android.util.Log
import com.recon.concentrate.DB.CubeDao
import com.recon.concentrate.DB.CubeEntity


class ChestOpener(private val coef: Float, private val cubeDao: CubeDao) {

    suspend fun OpenChest(): CubeEntity? {
        val randomVal: Int = (0..10000).random()
        val coefRandVal: Float = randomVal * coef
        return when {
            coefRandVal <= 6000 -> getRandomCubeByRarity("Common")
            coefRandVal <= 8500 -> getRandomCubeByRarity("Rare")
            coefRandVal <= 9500 -> getRandomCubeByRarity("Epic")
            else -> getRandomCubeByRarity("Legendary")
        }?.apply {
            this.isOpen = true
            cubeDao.updateCube(this)
            Log.d("", "Name $name\nRarity $rarity\nIsOpen $isOpen\n")
        }
    }

    private suspend fun getRandomCubeByRarity(rarity: String): CubeEntity? {
        val cubesByRarity = cubeDao.getCubesByRarity(rarity)
        return cubesByRarity.randomOrNull()
    }

}