package io.github.rajnishkmehta.dhwanicontrol.features.floating

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import io.github.rajnishkmehta.dhwanicontrol.R
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityFloatingButtonConfigBinding

class FloatingButtonConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFloatingButtonConfigBinding
    private var selectedIconName: String = ""
    private var selectedColor: Int = -1

    private val icons = listOf(
        "ic_1_volume-up",
        "ic_2_volume-down",
        "ic_3_volume-medium",
        "ic_4_sound"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFloatingButtonConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedIconName = AppPreferences.getFloatingIconName(this)
        selectedColor = AppPreferences.getFloatingIconColor(this)

        setupUI()
        updatePreview()
    }

    private fun setupUI() {
        binding.iconRecyclerView.adapter = IconAdapter(icons) { iconName ->
            selectedIconName = iconName
            updatePreview()
        }

        binding.colorDefaultButton.setOnClickListener {
            selectedColor = -1
            updatePreview()
        }

        binding.pickColorButton.setOnClickListener {
            showColorPickerDialog()
        }

        binding.saveConfigButton.setOnClickListener {
            AppPreferences.setFloatingIconName(this, selectedIconName)
            AppPreferences.setFloatingIconColor(this, selectedColor)
            FloatingButtonRuntime.sync(this)
            Toast.makeText(this, R.string.floating_config_saved, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updatePreview() {
        val iconResId = resources.getIdentifier(selectedIconName, "drawable", packageName)
            .takeIf { it != 0 } ?: R.drawable.ic_overlay
        
        binding.iconPreview.setImageResource(iconResId)
        
        val tintColor = if (selectedColor == -1) {
            getColor(R.color.colorPrimary)
        } else {
            selectedColor
        }
        binding.iconPreview.setColorFilter(tintColor)
        binding.currentColorDisplay.backgroundTintList = android.content.res.ColorStateList.valueOf(tintColor)
    }

    private fun showColorPickerDialog() {
        val colors = intArrayOf(
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA,
            Color.BLACK, Color.WHITE, Color.GRAY, Color.parseColor("#FF5722"), 
            Color.parseColor("#4CAF50"), Color.parseColor("#2196F3")
        )

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_color_picker, null)
        val grid = dialogView.findViewById<android.widget.GridLayout>(R.id.colorGrid)
        
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(R.string.floating_config_choose_color)
            .setView(dialogView)
            .create()

        colors.forEach { color ->
            val colorView = View(this).apply {
                layoutParams = android.widget.GridLayout.LayoutParams().apply {
                    width = dpToPx(48)
                    height = dpToPx(48)
                    setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8))
                }
                setBackgroundResource(R.drawable.bg_status_dot)
                backgroundTintList = android.content.res.ColorStateList.valueOf(color)
                setOnClickListener {
                    selectedColor = color
                    updatePreview()
                    dialog.dismiss()
                }
            }
            grid.addView(colorView)
        }
        dialog.show()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    inner class IconAdapter(
        private val iconNames: List<String>,
        private val onIconSelected: (String) -> Unit
    ) : RecyclerView.Adapter<IconAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val iconImage: ImageView = view.findViewById(R.id.itemIconImage)
            val iconText: TextView = view.findViewById(R.id.itemIconText)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_floating_icon, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val name = iconNames[position]
            val resId = resources.getIdentifier(name, "drawable", packageName)
            
            holder.iconImage.setImageResource(resId)
            
            // Format name: ic_1_volume-up -> 1. volume up
            val parts = name.split("_")
            val place = parts.getOrNull(1) ?: ""
            val iconName = parts.getOrNull(2)?.replace("-", " ") ?: ""
            
            holder.iconText.text = "$place. $iconName"
            
            holder.itemView.setOnClickListener {
                onIconSelected(name)
            }
        }

        override fun getItemCount() = iconNames.size
    }
}
