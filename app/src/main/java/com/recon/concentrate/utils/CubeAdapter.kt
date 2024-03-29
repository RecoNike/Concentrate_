package com.recon.concentrate.utils

import SharedPreferencesManager
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.recon.concentrate.DB.CubeEntity
import com.recon.concentrate.R
import com.recon.concentrate.ShopActivity


class CubeAdapter(private val cubes: List<CubeEntity>) :
    RecyclerView.Adapter<CubeAdapter.CubeViewHolder>() {

    val cubeImages = mapOf(
        "Basic" to R.drawable.block_default,
        "Softy" to R.drawable.block_softy,
        "Jelly" to R.drawable.block_jelly,
        "Sapphire" to R.drawable.block_saphire,
        "Eye" to R.drawable.block_eye,
        "Dirt" to R.drawable.block_dirty,
        "Scratched" to R.drawable.block_scratched,

        "Cactus" to R.drawable.block_cactus,
        "Coffee Cup" to R.drawable.block_coffee_cup,
        "Iron" to R.drawable.block_iron,
        "Water Glass" to R.drawable.block_water_glass,
        "Dissolved" to R.drawable.block_dissolved,
        "Apple" to R.drawable.block_apple,

        "Magma" to R.drawable.block_magma,
        "Crystal" to R.drawable.block_crystal,
        "Ice-Cream" to R.drawable.block_ice_creampng,
        "Neon" to R.drawable.block_neon,

        "Elf`s Stone" to R.drawable.block_elf_stone,
//        "Mechanical" to R.drawable.block_eye,
        "Sci-Fi" to R.drawable.block_sci_fi,

        // Добавьте другие кубы и их изображения
    )
    lateinit var vibrationHelper: VibrationHelper

    // ViewHolder для элемента списка
    class CubeViewHolder(itemView: View, private val cubes: List<CubeEntity>) : RecyclerView.ViewHolder(itemView) {


        val nameTextView: TextView = itemView.findViewById(R.id.nameTV)
        val rarityTextView: TextView = itemView.findViewById(R.id.rarityTV)
        val imageView: ImageView = itemView.findViewById(R.id.cardImage)
        init{
            itemView.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val cube = cubes[position]
                    // Проверяем значение переменной isOpen
                    if (cube.isOpen) {
                        Snackbar.make(
                            imageView,
                            "Opened",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        Snackbar.make(
                            imageView,
                            "You need to find it",
                            Snackbar.LENGTH_SHORT
                        ).setAction("Shop") {
                            val intent = Intent(itemView.context, ShopActivity::class.java)
                            itemView.context.startActivity(intent)
                            (itemView.context as Activity).finish()
                        }.show()
                    }
                }
            }
        }
    }

    // Создание нового ViewHolder (вызывается LayoutManager'ом)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CubeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.collection_item_layout, parent, false)
        return CubeViewHolder(itemView, cubes)
    }

    // Связывание данных с ViewHolder'ом (вызывается LayoutManager'ом)
    override fun onBindViewHolder(holder: CubeViewHolder, position: Int) {
        val currentItem = cubes[position]
        if (currentItem.isOpen) {
            holder.nameTextView.text = currentItem.name
            holder.rarityTextView.text = currentItem.rarity
            val imageResource = cubeImages[currentItem.name]
            if (imageResource != null) {
                holder.imageView.setImageResource(imageResource)
            } else {
                // Если изображение не найдено, загрузите дефолтное изображение
                holder.imageView.setImageResource(R.drawable.block_placeholder)
            }
        } else {
            holder.nameTextView.text = "???????"
            holder.rarityTextView.text = currentItem.rarity
            holder.imageView.setImageResource(R.drawable.block_placeholder)
        }
    }

    override fun getItemCount() = cubes.size
}
