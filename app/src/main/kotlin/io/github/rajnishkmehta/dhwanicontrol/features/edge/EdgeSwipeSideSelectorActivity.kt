package io.github.rajnishkmehta.dhwanicontrol.features.edge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.rajnishkmehta.dhwanicontrol.Constants
import io.github.rajnishkmehta.dhwanicontrol.core.preferences.AppPreferences
import io.github.rajnishkmehta.dhwanicontrol.databinding.ActivityEdgeSwipeSideSelectorBinding

class EdgeSwipeSideSelectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEdgeSwipeSideSelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEdgeSwipeSideSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.helpButton.setOnClickListener {
            val url = getString(io.github.rajnishkmehta.dhwanicontrol.R.string.edge_swipe_help_url)
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
            runCatching {
                startActivity(intent)
            }.onFailure {
                android.widget.Toast.makeText(this, io.github.rajnishkmehta.dhwanicontrol.R.string.app_info_open_link_failed, android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        val selectedSide = AppPreferences.getEdgeSelectedSide(this)
        if (selectedSide == Constants.SIDE_LEFT) {
            binding.leftSideRadio.isChecked = true
        } else {
            binding.rightSideRadio.isChecked = true
        }

        binding.saveEdgeButton.setOnClickListener {
            val side = if (binding.leftSideRadio.isChecked) {
                Constants.SIDE_LEFT
            } else {
                Constants.SIDE_RIGHT
            }

            AppPreferences.setEdgeSelectedSide(this, side)
            AppPreferences.setEdgeConfigured(this, true)
            EdgeOverlayRuntime.sync(this)
            finish()
        }
    }
}
