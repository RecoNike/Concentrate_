package com.recon.concentrate.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.recon.concentrate.R

class CustomDialogFragment : DialogFragment() {

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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chest_open_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_block)

        val greatBtn: TextView = view.findViewById(R.id.greatBtn)
        greatBtn.setOnClickListener {
            dismiss()
        }

        val cubeName = arguments?.getString("cubeName")
        val nameField: TextView = view.findViewById(R.id.cubeNameTextView)
        nameField.text = cubeName

        val cubeRarity = arguments?.getString("cubeRarity")
        val rarityField: TextView = view.findViewById(R.id.cubeRarityTextView)
        rarityField.text = cubeRarity

        greatBtn.setOnClickListener {
            dismiss()
        }
        val imageField: ImageView = view.findViewById(R.id.cubeImageView)
        imageField.startAnimation(animation)
        val imageResource = cubeImages[cubeName]
        if (imageResource != null) {
            imageField.setImageResource(imageResource)
        } else {
            // Если изображение не найдено, загрузите дефолтное изображение
            imageField.setImageResource(R.drawable.block_placeholder)
        }

    }

    companion object {
        fun newInstance(cubeName: String, cubeRarity: String) = CustomDialogFragment().apply {
            arguments = Bundle().apply {
                putString("cubeName", cubeName)
                putString("cubeRarity", cubeRarity)
            }
        }
    }


}

