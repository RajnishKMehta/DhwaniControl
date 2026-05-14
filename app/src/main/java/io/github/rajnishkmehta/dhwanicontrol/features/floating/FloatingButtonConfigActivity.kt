package io.github.rajnishkmehta.dhwanicontrol.features.floating

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private var showingAllIcons = false

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
        refreshIconList()

        binding.colorDefaultButton.setOnClickListener {
            selectedColor = -1
            binding.hexColorInput.setText("")
            updatePreview()
        }

        binding.pickColorButton.setOnClickListener {
            showColorPickerDialog()
        }

        binding.hexColorInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val hex = s.toString()
                if (hex.isEmpty()) return
                
                runCatching {
                    val color = Color.parseColor(if (hex.startsWith("#")) hex else "#$hex")
                    selectedColor = color
                    binding.hexColorInputLayout.error = null
                    updatePreview()
                }.onFailure {
                    binding.hexColorInputLayout.error = getString(R.string.floating_config_invalid_hex)
                }
            }
        })

        if (selectedColor != -1) {
            binding.hexColorInput.setText(String.format("#%06X", 0xFFFFFF and selectedColor))
        }

        binding.saveConfigButton.setOnClickListener {
            AppPreferences.setFloatingIconName(this, selectedIconName)
            AppPreferences.setFloatingIconColor(this, selectedColor)
            
            // Restart feature if enabled
            if (AppPreferences.isFloatingEnabled(this)) {
                FloatingButtonRuntime.sync(this)
            }
            
            Toast.makeText(this, R.string.floating_config_saved, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun refreshIconList() {
        val displayIcons = if (showingAllIcons) {
            OverlayIconRegistry.allIcons
        } else {
            // Show up to 3 icons + more button
            OverlayIconRegistry.allIcons.take(3) + OverlayIconRegistry.getMoreIconName()
        }

        binding.iconRecyclerView.adapter = IconAdapter(displayIcons) { iconName ->
            if (iconName == OverlayIconRegistry.getMoreIconName()) {
                showingAllIcons = true
                refreshIconList()
            } else {
                selectedIconName = iconName
                updatePreview()
            }
        }
    }

    private fun updatePreview() {
        val iconResId = resources.getIdentifier(selectedIconName, "drawable", packageName)
            .takeIf { it != 0 } ?: resources.getIdentifier(OverlayIconRegistry.getDefaultIconName(), "drawable", packageName)
        
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
                    binding.hexColorInput.setText(String.format("#%06X", 0xFFFFFF and color))
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
            val isMore = name == OverlayIconRegistry.getMoreIconName()
            
            val resId = resources.getIdentifier(name, "drawable", packageName)
                .takeIf { it != 0 }
                ?: resources.getIdentifier(OverlayIconRegistry.getDefaultIconName(), "drawable", packageName)
            
            holder.iconImage.setImageResource(resId)
            
            if (isMore) {
                holder.iconText.text = "More"
            } else {
                // Format name: ic_1_volume-up -> 1. volume up
                val parts = name.split("_")
                val place = parts.getOrNull(1) ?: ""
                val iconName = parts.getOrNull(2)?.replace("-", " ") ?: ""
                holder.iconText.text = if (place.isNotEmpty()) "$place. $iconName" else iconName
            }
            
            holder.itemView.setOnClickListener {
                onIconSelected(name)
            }
        }

        override fun getItemCount() = iconNames.size
    }
}
